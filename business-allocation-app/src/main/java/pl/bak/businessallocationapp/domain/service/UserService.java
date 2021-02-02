package pl.bak.businessallocationapp.domain.service;

import org.springframework.stereotype.Service;
import pl.bak.businessallocationapp.domain.dao.UserRepository;
import pl.bak.businessallocationapp.model.User;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

}
