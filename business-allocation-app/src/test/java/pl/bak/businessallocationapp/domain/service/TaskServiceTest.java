package pl.bak.businessallocationapp.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.bak.businessallocationapp.domain.app.test.TestBodyProvider;
import pl.bak.businessallocationapp.domain.dao.TaskRepository;
import pl.bak.businessallocationapp.domain.dao.UserRepository;
import pl.bak.businessallocationapp.dto.TaskDto;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.model.Task;
import pl.bak.businessallocationapp.model.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private TaskService taskService;

    private TestBodyProvider testBodyProvider;

    @BeforeEach
    void init() {
        testBodyProvider = new TestBodyProvider();
        ModelMapper modelMapper = new ModelMapper();
        taskService = new TaskService(taskRepository, modelMapper, userRepository);
    }

    @Test
    void shouldReturnListAllNotReadyToCheckedTask() {
        //given
        given(taskRepository.findAllTaskToBeCheckFlag(false)).willReturn(List.of(testBodyProvider.prepareTask()));
        given(userRepository.findAllByTasksIsFalse()).willReturn(List.of(new User(), new User()));

        //when
        List<TaskDto> taskDtoList = taskService.getAllNotReadyToBeCheckedWithUser(false);

        //then
        assertThat(taskDtoList)
                .isNotNull()
                .asList()
                .isNotEmpty()
                .hasSize(1)
                .hasOnlyElementsOfType(TaskDto.class);

    }

    @Test
    void shouldReturnAllTasksWithoutUser() {
        //given
        given(taskRepository.findAll()).willReturn(List.of(testBodyProvider.prepareTask()));

        //when
        List<TaskDto> taskDtos = taskService.showAllTasksWithoutUser();

        //then
        assertThat(taskDtos)
                .isNotNull()
                .asList()
                .isEmpty();
    }

    @Test
    void shouldReturnTaskById() throws MalformedURLException {
        //given
        given(taskRepository.findById(1L)).willReturn(Optional.of(testBodyProvider.prepareTask()));

        //when
        Optional<Task> taskById = taskService.getTaskById(1L);

        //then
        assertThat(taskById.isPresent()).isTrue();
        assertThat(taskById)
                .isNotNull()
                .isNotEmpty()
                .get()
                .hasFieldOrPropertyWithValue("taskName", "Task1")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("completed", false)
                .hasFieldOrPropertyWithValue("readyToBeChecked", false)
                .hasFieldOrPropertyWithValue("workEffectRepository", new URL("https://www.youtube.com/"))
                .hasFieldOrProperty("users");
    }

    @Test
    void shouldChangeTaskAsReadyToCheckById() throws MalformedURLException {
        //given
        givenTaskToMark();

        //when
        Optional<TaskDto> taskDto = taskService.markTaskAsReadyToCheckById(1L);

        //then
        assertThat(taskDto.isPresent()).isTrue();
        assertThat(taskDto.get())
                .hasFieldOrPropertyWithValue("taskName", "Task1")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("completed", false)
                .hasFieldOrPropertyWithValue("readyToBeChecked", true)
                .hasFieldOrPropertyWithValue("workEffectRepository", new URL("https://www.youtube.com/"));
    }

    @Test
    void shouldChangeTaskAsFullyReadyById() throws MalformedURLException {
        //given
        givenTaskToMark();

        //when
        Optional<TaskDto> taskDto = taskService.markTaskAsFullyReadyById(1L);

        //then
        assertThat(taskDto.isPresent()).isTrue();
        assertThat(taskDto.get())
                .hasFieldOrPropertyWithValue("taskName", "Task1")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("completed", true)
                .hasFieldOrPropertyWithValue("readyToBeChecked", true)
                .hasFieldOrPropertyWithValue("workEffectRepository", new URL("https://www.youtube.com/"));
    }

    @Test
    void shouldCreateTaskIfTaskByNameDosntExist() throws MalformedURLException {
        //given
        given(taskRepository.findTaskByTaskName(testBodyProvider.prepareTaskDto().getTaskName())).willReturn(Optional.empty());
        given(taskRepository.save(any())).willReturn(testBodyProvider.prepareTask());

        //when
        Optional<TaskDto> task = taskService.createTask(testBodyProvider.prepareTaskDto());

        //then
        assertThat(task.isPresent()).isTrue();
        assertThat(task.get())
                .hasFieldOrPropertyWithValue("taskName", "Task1")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("completed", false)
                .hasFieldOrPropertyWithValue("readyToBeChecked", false)
                .hasFieldOrPropertyWithValue("workEffectRepository", new URL("https://www.youtube.com/"));

    }

    @Test
    void shouldUpdateTaskAndAdditionalAddUserToTask() throws MalformedURLException {
        //given
        given(taskRepository.findById(1L)).willReturn(Optional.of(testBodyProvider.prepareTask()));
        given(taskRepository.save(any())).willReturn(testBodyProvider.prepareTask());
        given(userRepository.save(any())).willReturn(new User());
        given(userRepository.findUserByPinCode(1234)).willReturn(Optional.of(new User()));

        //when
        Optional<TaskDto> taskDto = taskService.updateTask(1L, List.of(1234));

        //then
        assertThat(taskDto.isPresent()).isTrue();
        assertThat(taskDto.get())
                .hasFieldOrPropertyWithValue("taskName", "Task1")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("completed", false)
                .hasFieldOrPropertyWithValue("readyToBeChecked", false)
                .hasFieldOrPropertyWithValue("workEffectRepository", new URL("https://www.youtube.com/"));
    }

    @Test
    void shouldRemoveCompletedTaskById() {
        //given
        doNothing().when(taskRepository).deleteIfTaskIsCompleted(1L);

        //when
        taskService.removeCompletedTaskById(1L);

        //then
        verify(taskRepository).deleteIfTaskIsCompleted(1L);
    }

    private void givenTaskToMark() {
        Task task = testBodyProvider.prepareTask();
        task.setReadyToBeChecked(true);
        given(taskRepository.findById(1L)).willReturn(Optional.of(task));
        given(taskRepository.save(any())).willReturn(task);
    }


}