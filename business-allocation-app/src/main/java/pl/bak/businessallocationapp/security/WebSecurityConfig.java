package pl.bak.businessallocationapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.bak.businessallocationapp.domain.service.UserService;
import pl.bak.businessallocationapp.model.Role;
import pl.bak.businessallocationapp.security.jwt.JwtConfig;
import pl.bak.businessallocationapp.security.jwt.JwtTokenVerifier;
import pl.bak.businessallocationapp.security.jwt.JwtUsernameAndPasswordAuthFilter;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public WebSecurityConfig(UserService userService, SecretKey secretKey, JwtConfig jwtConfig) {
        this.userService = userService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + username + " not exist"));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable();

        http
                .authorizeRequests()
                .anyRequest().permitAll();

//        http
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/user/add")
//                .hasAnyAuthority(
//                        Role.ROLE_ADMIN.getAuthority(),
//                        Role.ROLE_BOSS.getAuthority(),
//                        Role.ROLE_EMPLOYEE.getAuthority()
//                )
//                .antMatchers(HttpMethod.DELETE, "/user/{id}")
//                .hasAnyRole(
//                        Role.ROLE_ADMIN.getAuthority(),
//                        Role.ROLE_BOSS.getAuthority()
//                )
//                .antMatchers(HttpMethod.PUT, "/user/update/{id}")
//                .hasAnyRole(
//                        Role.ROLE_ADMIN.getAuthority(),
//                        Role.ROLE_BOSS.getAuthority(),
//                        Role.ROLE_EMPLOYEE.getAuthority()
//                )
//                .anyRequest().authenticated();
//
//
//        http
//                .addFilter(
//                        new JwtUsernameAndPasswordAuthFilter(
//                                authenticationManager(),
//                                jwtConfig, secretKey
//                        )
//                )
//                .addFilterAfter(
//                        new JwtTokenVerifier(secretKey, jwtConfig),
//                        JwtUsernameAndPasswordAuthFilter.class
//                )
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(unauthorizedEntryPoint());
//
//        http
//                .rememberMe()
//                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//                .key(secretKey.toString())
//                .rememberMeParameter("remember-me")
//                .and()
//                .logout()
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("remember-me", "JSESSIONID").permitAll();
    }
}
