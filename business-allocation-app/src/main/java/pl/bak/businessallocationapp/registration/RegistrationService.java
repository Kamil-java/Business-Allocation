package pl.bak.businessallocationapp.registration;

import org.springframework.stereotype.Service;
import pl.bak.businessallocationapp.domain.service.UserService;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.email.EmailSender;
import pl.bak.businessallocationapp.email.templete.MessageTemplate;
import pl.bak.businessallocationapp.registration.token.domain.service.ConfirmationTokenService;
import pl.bak.businessallocationapp.registration.token.model.ConfirmationToken;

import java.time.LocalDateTime;

@Service
public class RegistrationService {
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final UserService userService;

    public RegistrationService(ConfirmationTokenService confirmationTokenService, EmailSender emailSender, UserService userService) {
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
        this.userService = userService;
    }

    public String register(UserDto userDto) {

        String token = userService.singUp(userDto);

        String link = "http://localhost:8080/registration/confirm?token=" + token;

        String messageTemplate = new MessageTemplate().buildEmail(userDto.getUsername(), link);

        emailSender.send(userDto.getEmail(), messageTemplate);

        return token;
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        confirmationTokenService.serConfirmedAt(token);

        userService.enableAccount(confirmationToken.getUser().getEmail());

        return "Confirmed";
    }


}
