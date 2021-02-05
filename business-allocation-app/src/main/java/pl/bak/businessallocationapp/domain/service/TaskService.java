package pl.bak.businessallocationapp.domain.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bak.businessallocationapp.domain.dao.TaskRepository;
import pl.bak.businessallocationapp.domain.dao.UserRepository;
import pl.bak.businessallocationapp.dto.TaskDto;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.model.Task;
import pl.bak.businessallocationapp.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public List<TaskDto> getAllNotReadyToBeCheckedWithUser(boolean flag) {
        List<Task> allTask = taskRepository.findAllTaskToBeCheckFlag(flag);
        List<TaskDto> taskDtos = new LinkedList<>();

        allTask.forEach(task -> {
            TaskDto taskDto = modelMapper.map(task, TaskDto.class);

            userRepository.findAllByTasksIsFalse().forEach(user -> {
                boolean isBelong = user.getTasks().stream()
                        .anyMatch(task1 -> task1.getId().equals(task.getId()));

                if (isBelong) {
                    UserDto userDto = modelMapper.map(user, UserDto.class);
                    userDto.setSkills(Collections.emptySet());
                    userDto.setId(null);
                    userDto.setBirthDate(null);

                    taskDto.getUserDtos().add(userDto);
                }
            });

            taskDtos.add(taskDto);
        });

        return taskDtos;
    }

    public List<TaskDto> showAllTasksWithoutUser() {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getUsers().size() == 0)
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());

    }

    public Optional<Task> getTaskById(long id){
        return taskRepository.findById(id);
    }

    @Transactional
    public Optional<TaskDto> markTaskAsFullyReadyById(long id) {
        Optional<Task> taskById = taskRepository.findById(id);

        if (taskById.isPresent()) {
            taskById.get().setCompleted(true);
            taskRepository.save(taskById.get());
            return Optional.of(modelMapper.map(taskById.get(), TaskDto.class));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<TaskDto> createTask(TaskDto taskDto) {
        Optional<Task> taskByTaskName = taskRepository.findTaskByTaskName(taskDto.getTaskName());

        if (taskByTaskName.isPresent()) {
            return Optional.empty();
        }

        taskRepository.save(modelMapper.map(taskDto, Task.class));

        return Optional.of(taskDto);
    }

    @Transactional
    public Optional<TaskDto> updateTask(long id, List<Integer> pin) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            User userToSave = new User();
            Task taskToSave = new Task();

            TaskDto taskDto = modelMapper.map(task.get(), TaskDto.class);

            for (Integer pinCode : pin) {
                Optional<User> user = userRepository.findUserByPinCode(pinCode);

                if (user.isEmpty()) {
                    return Optional.empty();
                }

                userToSave = user.get();
                taskToSave = task.get();

                taskToSave.getUsers().add(userToSave);
                userToSave.getTasks().add(taskToSave);

                UserDto userDto = new UserDto();
                userDto.setFirstName(userToSave.getFirstName());
                userDto.setLastName(userToSave.getLastName());
                userDto.setUsername(userToSave.getUsername());

                taskDto.getUserDtos().add(userDto);
            }

            taskRepository.save(taskToSave);
            userRepository.save(userToSave);

            return Optional.of(taskDto);
        }

        return Optional.empty();
    }

    @Transactional
    public void removeCompletedTask(long id){
        taskRepository.deleteIfTaskIsCompleted(id);
    }
}
