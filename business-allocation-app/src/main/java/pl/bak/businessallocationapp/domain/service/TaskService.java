package pl.bak.businessallocationapp.domain.service;

import org.modelmapper.ModelMapper;
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

    public List<TaskDto> getAllNotCompletedTaskWithUser() {
        List<Task> allTask = taskRepository.findAllTaskByCompletedFlag(false);
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

    public List<TaskDto> getAllCompletedTask(){
        List<Task> allTask = taskRepository.findAllTaskByCompletedFlag(true);
        List<TaskDto> taskDtos = new ArrayList<>();

        allTask.forEach(task -> taskDtos.add(modelMapper.map(task, TaskDto.class)));

        return taskDtos;
    }

    public List<TaskDto> showAllTasksWithoutUser() {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getUsers().size() == 0)
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());

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
    public Optional<TaskDto> updateTask(long id, List<UserDto> userDtos) {
        Optional<Task> taskById = taskRepository.findById(id);

        User userToSave = new User();

        for (UserDto userDto : userDtos) {
            Optional<User> user = userRepository.findUserByUsername(userDto.getUsername());
            if (user.isEmpty()) {
                return Optional.empty();
            }
            userToSave = user.get();
            taskById.ifPresent(task -> {
                task.getUsers().add(user.get());
                user.get().getTasks().add(task);
            });
        }

        if (taskById.isPresent()) {
            TaskDto taskDto = modelMapper.map(taskById.get(), TaskDto.class);

            userDtos.forEach(userDto -> taskDto.getUserDtos().add(userDto));

            taskRepository.save(taskById.get());
            userRepository.save(userToSave);

            return Optional.of(taskDto);
        }
        return Optional.empty();
    }


}
