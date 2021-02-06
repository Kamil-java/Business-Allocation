package pl.bak.businessallocationapp.domain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.bak.businessallocationapp.domain.service.UserService;
import pl.bak.businessallocationapp.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<UserDto> getAllUser() {
        List<UserDto> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return allUsers;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{id}")
    public UserDto updateUser(@PathVariable("id") long id, @RequestBody @Valid UserDto userDto) {
        return userService.updateUser(id, userDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") long id) {
        if (!userService.userExistById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userService.deleteUserById(id);
    }

}
