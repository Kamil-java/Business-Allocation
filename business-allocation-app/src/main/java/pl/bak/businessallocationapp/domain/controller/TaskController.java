package pl.bak.businessallocationapp.domain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.bak.businessallocationapp.domain.service.TaskService;
import pl.bak.businessallocationapp.dto.TaskDto;
import pl.bak.businessallocationapp.model.Task;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all/to/check")
    public List<TaskDto> allCompletedOrNotTask(@RequestParam boolean isCompleted){
        return checkList(taskService.getAllNotReadyToBeCheckedWithUser(isCompleted));
    }

    @GetMapping("/all/without/user")
    public List<TaskDto> allTaskWithoutUser(){
        return checkList(taskService.showAllTasksWithoutUser());
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto addTask(@RequestBody @Valid TaskDto taskDto){
        return taskService.createTask(taskDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT));
    }

    @PutMapping("/{id}/add/user")
    public TaskDto updateTask(@PathVariable("id") long id,@RequestParam List<Integer> pin){
        return taskService.updateTask(id, pin)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/accept/{id}")
    public TaskDto passTask(@PathVariable("id") long id){
        return taskService.markTaskAsFullyReadyById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteTask(@PathVariable("id") long id){
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!task.isCompleted()){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        taskService.removeCompletedTask(id);
    }

    private List<TaskDto> checkList(List<TaskDto> taskDtos) {
        if (taskDtos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return taskDtos;
    }

}
