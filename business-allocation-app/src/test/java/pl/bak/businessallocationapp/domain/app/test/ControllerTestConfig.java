package pl.bak.businessallocationapp.domain.app.test;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pl.bak.businessallocationapp.domain.service.UserService;
import pl.bak.businessallocationapp.security.jwt.JwtConfig;

import javax.crypto.SecretKey;

@TestConfiguration
public class ControllerTestConfig {

    @Bean
    public SecretKey secretKey() {
        return Mockito.mock(SecretKey.class);
    }

    @Bean
    public JwtConfig jwtConfig() {
        return Mockito.mock(JwtConfig.class);
    }

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public TestBodyProvider testBodyProvider() {
        return new TestBodyProvider();
    }
}
