package pl.bak.businessallocationapp.domain.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bak.businessallocationapp.domain.dao.SkillRepository;
import pl.bak.businessallocationapp.domain.dao.UserRepository;
import pl.bak.businessallocationapp.dto.SkillDto;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.model.User;
import pl.bak.businessallocationapp.registration.token.domain.service.ConfirmationTokenService;
import pl.bak.businessallocationapp.registration.token.model.ConfirmationToken;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final SkillRepository skillRepository;
    private final ConfirmationTokenService confirmationTokenService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, SkillRepository skillRepository, ConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.skillRepository = skillRepository;
        this.confirmationTokenService = confirmationTokenService;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> userDtos = new LinkedList<>();
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName"));
        users.forEach(user -> {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userDtos.add(userDto);
        });

        return userDtos;
    }

    public Optional<UserDto> getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        Optional<UserDto> userDto = user.map(value -> modelMapper.map(value, UserDto.class));

        userDto.ifPresent(dto -> dto.setSkills(user.get().getSkills()
                .stream()
                .map(skill -> {
                    SkillDto skillDto = new SkillDto();
                    skillDto.setNameSkill(skill.getNameSkill());
                    skillDto.setSeniorityLevel(skill.getSeniorityLevel());
                    return skillDto;
                })
                .collect(Collectors.toSet())));

        return userDto;
    }

    @Transactional
    public User createUser(UserDto userDto) {
        boolean userEmailExist = userRepository.findByEmail(userDto.getEmail())
                .isPresent();

        boolean userUsernameExist = getUserByUsername(userDto.getUsername())
                .isPresent();

        if (userUsernameExist || userEmailExist) {
            return null;
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User user = modelMapper.map(userDto, User.class);
        skillRepository.saveAll(user.getSkills());
        return userRepository.save(user);
    }

    @Transactional
    public Optional<UserDto> updateUser(long id, UserDto userDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            modelMapper.map(userDto, user.get());
            User save = userRepository.save(user.get());
            userDto.setId(save.getId());
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Transactional
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

    public String singUp(UserDto userDto) {
        User user = createUser(userDto);

        String token = UUID.randomUUID().toString();

        LocalDateTime timeNow = LocalDateTime.now();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                timeNow,
                timeNow.plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    @Transactional
    public void enableAccount(String email){
        userRepository.enableAppUser(email);
    }

    public boolean userExistById(long id) {
        return userRepository.existsById(id);
    }


}
