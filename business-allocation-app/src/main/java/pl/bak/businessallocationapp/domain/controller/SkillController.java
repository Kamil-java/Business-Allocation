package pl.bak.businessallocationapp.domain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.bak.businessallocationapp.domain.service.SkillService;
import pl.bak.businessallocationapp.domain.service.UserService;
import pl.bak.businessallocationapp.dto.SkillDto;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.model.SeniorityLevel;

import javax.validation.Valid;

@RestController
@RequestMapping("/skill")
public class SkillController {
    private final SkillService skillService;
    private final UserService userService;

    public SkillController(SkillService skillService, UserService userService) {
        this.skillService = skillService;
        this.userService = userService;
    }

    @PostMapping("/add/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addSkillToUser(@PathVariable("username") String username, @RequestBody @Valid SkillDto skillDto){
        userService.getUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return skillService.addSkillToUser(skillDto, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT));
    }

    @PutMapping(value = "/update/description/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SkillDto updateDescription(@PathVariable("id") long id, @RequestBody String description){
        return skillService.updateDescriptionById(id, description)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/level/{username}/skill/{id}")
    public UserDto updateLevel(@PathVariable("username") String username, @PathVariable("id") long id, @RequestParam SeniorityLevel seniorityLevel){
        return skillService.updateSeniorityLevel(username, id, seniorityLevel)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
