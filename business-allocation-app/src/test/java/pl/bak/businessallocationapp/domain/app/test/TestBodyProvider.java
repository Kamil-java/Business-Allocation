package pl.bak.businessallocationapp.domain.app.test;

import org.springframework.stereotype.Component;
import pl.bak.businessallocationapp.dto.TaskDto;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.model.Role;
import pl.bak.businessallocationapp.model.Task;
import pl.bak.businessallocationapp.model.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TestBodyProvider {

    public User prepareUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Jon");
        user.setLastName("Smith");
        user.setUsername("SJ");
        user.setPassword("pass");
        user.setBirthDate(LocalDate.of(2000, 2, 1));
        user.setEmail("jon@gmail.com");
        user.setRole(Role.ROLE_EMPLOYEE);
        return user;
    }

    public UserDto prepareUserDto() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Jon");
        userDto.setLastName("Smith");
        userDto.setUsername("SJ");
        userDto.setPassword("pass");
        userDto.setBirthDate(LocalDate.of(2000, 2, 1));
        userDto.setEmail("jon@gmail.com");
        userDto.setPinCode(1234);
        return userDto;
    }

    public Task prepareTask() {
        Task task = new Task();

        task.setId(1L);
        task.setTaskName("Task1");
        task.setDescription("desc");
        task.setCompleted(false);
        task.setReadyToBeChecked(false);
        try {
            task.setWorkEffectRepository(new URL("https://www.youtube.com/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Set<User> users = new HashSet<>();
        users.add(new User());
        users.add(new User());
        task.setUsers(users);

        return task;
    }

    public TaskDto prepareTaskDto() {
        TaskDto taskDto = new TaskDto();

        taskDto.setId(1L);
        taskDto.setTaskName("Task1");
        taskDto.setDescription("desc");
        taskDto.setCompleted(false);
        taskDto.setReadyToBeChecked(false);
        try {
            taskDto.setWorkEffectRepository(new URL("https://www.youtube.com/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        taskDto.setUserDtos(List.of(new UserDto(), new UserDto()));

        return taskDto;
    }
}
