package pl.bak.businessallocationapp.domain.app.test;


import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import pl.bak.businessallocationapp.security.WebSecurityConfig;
import pl.bak.businessallocationapp.security.jwt.JwtConfig;

import javax.crypto.SecretKey;

@TestConfiguration
public class ControllerTestConfiguration {


    public WebSecurityConfig webSecurityConfig(){
        return Mockito.mock(WebSecurityConfig.class);
    }


    public SecretKey secretKey(){
        return Mockito.mock(SecretKey.class);
    }


    public JwtConfig jwtConfig(){
        return Mockito.mock(JwtConfig.class);
    }
}
