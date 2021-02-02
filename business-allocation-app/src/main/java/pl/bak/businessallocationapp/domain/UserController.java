package pl.bak.businessallocationapp.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.bak.businessallocationapp.domain.service.UserService;
import pl.bak.businessallocationapp.dto.UserDto;

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

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) {
        UserDto dto = userService.createUser(userDto);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return dto;
    }

    @PutMapping("/update/{id}")
    public UserDto updateUser(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        UserDto dto = userService.updateUser(id, userDto);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return dto;
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
