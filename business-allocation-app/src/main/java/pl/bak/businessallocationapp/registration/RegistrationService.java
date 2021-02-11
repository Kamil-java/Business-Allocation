package pl.bak.businessallocationapp.registration;

import org.springframework.stereotype.Service;
import pl.bak.businessallocationapp.domain.service.UserService;
import pl.bak.businessallocationapp.dto.UserDto;
import pl.bak.businessallocationapp.email.EmailSender;
import pl.bak.businessallocationapp.email.templete.MessageTemplate;
import pl.bak.businessallocationapp.model.User;
import pl.bak.businessallocationapp.registration.token.domain.service.ConfirmationTokenService;
import pl.bak.businessallocationapp.registration.token.model.ConfirmationToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

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

        String token = singUp(userDto);

        String link = "http://localhost:8080/registration/confirm?token=" + token;

        String messageTemplate = new MessageTemplate().buildEmail(userDto.getUsername(), link);

        emailSender.send(userDto.getEmail(), messageTemplate);

        return "Token was sent";
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

        User user = confirmationToken.getUser();

        userService.getUserByUsername(user.getUsername()).ifPresent(u -> {
            u.setEnable(true);
            u.setLocked(false);
            userService.addUser(u);
        });

        return "Confirmed";
    }

    public String singUp(UserDto userDto) {
        User user = userService.createUser(userDto);

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


}
