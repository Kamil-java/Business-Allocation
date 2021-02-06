package pl.bak.businessallocationapp.registration.token.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bak.businessallocationapp.registration.token.domain.dao.ConfirmationTokenRepository;
import pl.bak.businessallocationapp.registration.token.model.ConfirmationToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Transactional
    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }

    @Transactional
    public void serConfirmedAt(String token){
        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }
}
