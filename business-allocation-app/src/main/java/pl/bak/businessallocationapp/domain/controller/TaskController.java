package pl.bak.businessallocationapp.domain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.bak.businessallocationapp.domain.service.TaskService;
import pl.bak.businessallocationapp.dto.TaskDto;
import pl.bak.businessallocationapp.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all/not/completed")
    public List<TaskDto> allNotCompletedTask(){
        return checkList(taskService.getAllNotCompletedTaskWithUser());
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto addTask(@RequestBody @Valid TaskDto taskDto){
        return taskService.createTask(taskDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT));
    }

    @GetMapping("/all/without/user")
    public List<TaskDto> allTaskWithoutUser(){
        return checkList(taskService.showAllTasksWithoutUser());
    }

    @PutMapping("/{id}/add/user")
    public TaskDto updateTask(@PathVariable("id") long id,@RequestBody @Valid List<UserDto> userDtos){
        return taskService.updateTask(id, userDtos)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private List<TaskDto> checkList(List<TaskDto> taskDtos) {
        if (taskDtos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return taskDtos;
    }


}
