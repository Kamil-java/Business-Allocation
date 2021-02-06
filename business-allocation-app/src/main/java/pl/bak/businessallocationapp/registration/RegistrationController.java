package pl.bak.businessallocationapp.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bak.businessallocationapp.dto.UserDto;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody UserDto userDto){
        return registrationService.register(userDto);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam String token){
        return registrationService.confirmToken(token);
    }
}
