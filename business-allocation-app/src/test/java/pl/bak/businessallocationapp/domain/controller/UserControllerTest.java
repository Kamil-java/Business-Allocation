package pl.bak.businessallocationapp.domain.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.bak.businessallocationapp.domain.service.UserService;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.security.jwt.JwtConfig;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs(outputDir = "documentation/endpoints/user")
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private SecretKey secretKey;

    @MockBean
    private JwtConfig jwtConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    }

    @Test
    void getAllUser() throws Exception {
        //given
        given(userService.getAllUsers()).willReturn(List.of(prepareUserDto()));

        //when
        ResultActions perform = mockMvc.perform(get("/user/all").contentType(MediaType.APPLICATION_JSON));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].firstName").value("Jon"))
                .andExpect(jsonPath("$.[0].lastName").value("Smith"))
                .andExpect(jsonPath("$.[0].username").value("SJ"))
                .andExpect(jsonPath("$.[0].email").value("jon@gmail.com"))
                .andExpect(jsonPath("$.[0].birthDate").value("2000-02-01"))
                .andDo(document("all-user"))
                .andDo(print());
    }

    @Test
    void getUserById() throws Exception {
        //given
        given(userService.getUserById(1)).willReturn(Optional.of(prepareUserDto()));

        //when
        ResultActions perform = mockMvc.perform(get("/user/{id}", 1).contentType(MediaType.APPLICATION_JSON));
        ResultActions notFound = mockMvc.perform(get("/user/{id}", 2).contentType(MediaType.APPLICATION_JSON));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("Jon"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.username").value("SJ"))
                .andExpect(jsonPath("$.email").value("jon@gmail.com"))
                .andExpect(jsonPath("$.birthDate").value("2000-02-01"))
                .andDo(document("user-by-id"))
                .andDo(print());

        notFound
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void addUser() throws Exception {
        //given
        given(userService.createUser(any(UserDto.class))).willReturn(prepareUserDto());

        //when
        String body = objectMapper.writeValueAsString(prepareUserDto());
        ResultActions perform = mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .with(csrf())
        );

        //then
        perform
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("Jon"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.username").value("SJ"))
                .andExpect(jsonPath("$.email").value("jon@gmail.com"))
                .andExpect(jsonPath("$.birthDate").value("2000-02-01"))
                .andDo(document("add-new-user"))
                .andDo(print());

    }

    @Test
    void updateUser() throws Exception {
        //given
        given(userService.updateUser(1, prepareUserDto())).willReturn(Optional.of(prepareUserDto()));

        //when
        String body = objectMapper.writeValueAsString(prepareUserDto());
        ResultActions perform = mockMvc.perform(put("/user/update/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .with(csrf())
        );
        ResultActions notFound = mockMvc.perform(put("/user/update/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .with(csrf())
        );

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("Jon"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.username").value("SJ"))
                .andExpect(jsonPath("$.email").value("jon@gmail.com"))
                .andExpect(jsonPath("$.birthDate").value("2000-02-01"))
                .andDo(document("add-new-user"))
                .andDo(print());

        notFound
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteById() throws Exception {
        //given
        given(userService.userExistById(1)).willReturn(true);
        doNothing().when(userService).deleteUserById(1);

        //when
        ResultActions perform = mockMvc.perform(delete("/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        ResultActions notFound = mockMvc.perform(delete("/user/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        perform
                .andExpect(status().isNoContent())
                .andDo(document("user-delete-by-id"))
                .andDo(print());

        verify(userService).deleteUserById(1);

        notFound
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private UserDto prepareUserDto() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Jon");
        userDto.setLastName("Smith");
        userDto.setUsername("SJ");
        userDto.setPassword("pass");
        userDto.setBirthDate(LocalDate.of(2000, 2, 1));
        userDto.setEmail("jon@gmail.com");
        return userDto;
    }
}