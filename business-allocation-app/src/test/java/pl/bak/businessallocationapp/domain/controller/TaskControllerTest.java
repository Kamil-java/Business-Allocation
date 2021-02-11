package pl.bak.businessallocationapp.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.bak.businessallocationapp.domain.app.test.ControllerTestConfig;
import pl.bak.businessallocationapp.domain.app.test.TestBodyProvider;
import pl.bak.businessallocationapp.domain.service.TaskService;
import pl.bak.businessallocationapp.dto.TaskDto;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.model.Task;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureRestDocs(outputDir = "documentation/endpoints/task")
@Import(ControllerTestConfig.class)
@AutoConfigureMockMvc
class TaskControllerTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestBodyProvider testBodyProvider;

    @BeforeEach
    public void init() {
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    }

    @Test
    void allCompletedOrNotTask() throws Exception {
        //given
        TaskDto taskDto = prepareTaskDto();
        taskDto.setReadyToBeChecked(true);
        given(taskService.getAllNotReadyToBeCheckedWithUser(true)).willReturn(List.of(taskDto));
        given(taskService.getAllNotReadyToBeCheckedWithUser(false)).willReturn(List.of());

        //when
        ResultActions perform = mockMvc.perform(get("/task/all/to/check")
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCompleted", "true")
        );

        ResultActions noContent = mockMvc.perform(get("/task/all/to/check")
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCompleted", "false")
        );

        //then
        noContent
                .andExpect(status().isNoContent());

        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].taskName").value("Task1"))
                .andExpect(jsonPath("$.[0].description").value("desc"))
                .andExpect(jsonPath("$.[0].readyToBeChecked").value(true))
                .andExpect(jsonPath("$.[0].completed").value(false))
                .andExpect(jsonPath("$.[0].workEffectRepository").value("https://www.youtube.com/"))
                .andExpect(jsonPath("$.[0].userDtos").isArray())
                .andExpect(jsonPath("$.[0].userDtos").isNotEmpty())
                .andDo(document("all-task-by-to-check"))
                .andDo(print());

    }

    @Test
    void allTaskWithoutUser() throws Exception {
        given(taskService.showAllTasksWithoutUser()).willReturn(List.of(prepareTaskDto()));

        //when
        ResultActions perform = mockMvc.perform(get("/task/all/without/user")
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].taskName").value("Task1"))
                .andExpect(jsonPath("$.[0].description").value("desc"))
                .andExpect(jsonPath("$.[0].readyToBeChecked").value(false))
                .andExpect(jsonPath("$.[0].completed").value(false))
                .andExpect(jsonPath("$.[0].workEffectRepository").value("https://www.youtube.com/"))
                .andDo(document("all-task-without-user"))
                .andDo(print());
    }

    @Test
    void addTask() throws Exception {
        //given
        given(taskService.createTask(any(TaskDto.class))).willReturn(Optional.of(prepareTaskDto()));

        //when
        String body = objectMapper.writeValueAsString(prepareTaskDto());
        ResultActions perform = mockMvc.perform(post("/task/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );

        //then
        perform
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskName").value("Task1"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.readyToBeChecked").value(false))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.workEffectRepository").value("https://www.youtube.com/"))
                .andDo(document("task-created"))
                .andDo(print());
    }

    @Test
    void updateTask() throws Exception {
        //given
        String first = "first-username";
        String second = "second-username";
        given(taskService.updateTask(1L, Arrays.asList(first, second))).willReturn(Optional.of(prepareTaskDto()));

        //when
        ResultActions perform = mockMvc.perform(put("/task/{id}/add/user", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", first + ", " + second)
        );

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskName").value("Task1"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.readyToBeChecked").value(false))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.workEffectRepository").value("https://www.youtube.com/"))
                .andDo(document("task-update"))
                .andDo(print());
    }

    @Test
    void markAsReady() throws Exception {
        //given
        TaskDto taskDto = prepareTaskDto();
        taskDto.setReadyToBeChecked(true);
        given(taskService.markTaskAsReadyToCheckById(1)).willReturn(Optional.of(taskDto));

        //when
        ResultActions perform = mockMvc.perform(put("/task/ready/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskName").value("Task1"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.readyToBeChecked").value(true))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.workEffectRepository").value("https://www.youtube.com/"))
                .andDo(document("task-mark-to-be-check"))
                .andDo(print());
    }

    @Test
    void passTask() throws Exception {
        //given
        TaskDto taskDto = prepareTaskDto();
        taskDto.setReadyToBeChecked(true);
        taskDto.setCompleted(true);
        given(taskService.markTaskAsFullyReadyById(1)).willReturn(Optional.of(taskDto));

        //when
        ResultActions perform = mockMvc.perform(put("/task/accept/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskName").value("Task1"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.readyToBeChecked").value(true))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.workEffectRepository").value("https://www.youtube.com/"))
                .andDo(document("task-mark-as-pass"))
                .andDo(print());
    }

    @Test
    void deleteTask() throws Exception {
        //given
        Task task = testBodyProvider.prepareTask();
        task.setCompleted(true);
        given(taskService.getTaskById(1)).willReturn(Optional.of(task));
        doNothing().when(taskService).removeCompletedTaskById(1);

        //when
        ResultActions perform = mockMvc.perform(delete("/task/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        perform
                .andExpect(status().isNoContent())
                .andDo(document("task-remove"))
                .andDo(print());
    }

    private TaskDto prepareTaskDto() {
        return testBodyProvider.prepareTaskDto();
    }
}