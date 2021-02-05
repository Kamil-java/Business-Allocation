package pl.bak.businessallocationapp.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bak.businessallocationapp.domain.dao.SkillRepository;
import pl.bak.businessallocationapp.domain.dao.UserRepository;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.model.Role;
import pl.bak.businessallocationapp.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SkillRepository skillRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        userService = new UserService(userRepository, modelMapper, passwordEncoder, skillRepository);
    }

    @Test
    void shouldReturnUserIfUserExistAndReturnOptionalEmptyIfUserNotExist() {
        //given
        given(userRepository.findUserByUsername("Jon")).willReturn(Optional.of(prepareUser()));
        given(userRepository.findUserByUsername("Anna")).willReturn(Optional.empty());

        //when
        Optional<User> jon = userService.getUserByUsername("Jon");
        Optional<User> anna = userService.getUserByUsername("Anna");

        //then
        assertThat(jon.isPresent())
                .isTrue();
        assertThat(jon)
                .get()
                .isEqualTo(prepareUser())
                .hasFieldOrProperty("username")
                .hasFieldOrProperty("email")
                .hasFieldOrProperty("password")
                .hasFieldOrProperty("firstName")
                .hasFieldOrProperty("lastName")
                .hasFieldOrProperty("birthDate")
                .hasFieldOrProperty("role");

        assertThat(anna.isPresent()).isFalse();
    }

    @Test
    void shouldReturnListAllUsers() {
        //given
        given(userRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName"))).willReturn(List.of(prepareUser()));

        //when
        List<UserDto> allUsers = userService.getAllUsers();

        //then
        assertThat(allUsers)
                .isNotNull()
                .asList()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void shouldReturnUserByIdOrReturnOptionalEmpty() {
        //given
        given(userRepository.findById(1L)).willReturn(Optional.of(prepareUser()));
        given(userRepository.findById(2L)).willReturn(Optional.empty());

        //when
        Optional<UserDto> userExist = userService.getUserById(1L);
        Optional<UserDto> userNotExist = userService.getUserById(2L);

        //then
        assertThat(userExist.isPresent())
                .isTrue();
        assertThat(userExist)
                .get()
                .isNotNull()
                .hasFieldOrProperty("username")
                .hasFieldOrProperty("email")
                .hasFieldOrProperty("password")
                .hasFieldOrProperty("firstName")
                .hasFieldOrProperty("lastName")
                .hasFieldOrProperty("birthDate");

        assertThat(userNotExist.isPresent()).isFalse();
    }

    @Test
    void shouldConvertDTOToUserAndSaveUserAndReturnDTO() {
        //given
        given(userRepository.save(any(User.class))).willReturn(prepareUser());

        //when
        UserDto user = userService.createUser(prepareUserDto());

        //then
        assertThat(user)
                .isNotNull()
                .isNotSameAs(prepareUserDto())
                .hasFieldOrProperty("username")
                .hasFieldOrProperty("email")
                .hasFieldOrProperty("password")
                .hasFieldOrProperty("firstName")
                .hasFieldOrProperty("lastName")
                .hasFieldOrProperty("birthDate");

    }

    @Test
    void shouldConvertDTOToUserAndUpdateUserAndReturnDTO() {
        //given
        given(userRepository.save(any(User.class))).willReturn(prepareUser());
        given(userRepository.findById(1L)).willReturn(Optional.of(prepareUser()));

        //when
        Optional<UserDto> user = userService.updateUser(1L, prepareUserDto());

        //then
        assertThat(user.isPresent()).isTrue();
        assertThat(user)
                .isNotNull()
                .isNotSameAs(prepareUserDto())
                .get()
                .hasFieldOrProperty("username")
                .hasFieldOrProperty("email")
                .hasFieldOrProperty("password")
                .hasFieldOrProperty("firstName")
                .hasFieldOrProperty("lastName")
                .hasFieldOrProperty("birthDate");
    }

    @Test
    void shouldDeleteUserById() {
        //given
        doNothing().when(userRepository).deleteById(1L);

        //when
        userService.deleteUserById(1L);

        //then
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldReturnTrueIfUserExists(){
        //given
        given(userRepository.existsById(1L)).willReturn(true);
        given(userRepository.existsById(2L)).willReturn(false);

        //when
        boolean exist = userService.userExistById(1L);
        boolean noExist = userService.userExistById(2L);

        //then
        assertThat(exist).isTrue();
        assertThat(noExist).isFalse();
    }

    private User prepareUser() {
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

    private UserDto prepareUserDto(){
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