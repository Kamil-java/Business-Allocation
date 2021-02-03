package pl.bak.businessallocationapp.domain.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.bak.businessallocationapp.domain.dao.UserRepository;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
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
        return user.map(value -> modelMapper.map(value, UserDto.class));
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (getUserByUsername(userDto.getUsername()).isPresent()) {
            return null;
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
        return userDto;
    }

    @Transactional
    public Optional<UserDto> updateUser(long id, UserDto userDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            modelMapper.map(userDto, user.get());
            userRepository.save(user.get());
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Transactional
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

    public boolean userExistById(long id) {
        return userRepository.existsById(id);
    }


}
