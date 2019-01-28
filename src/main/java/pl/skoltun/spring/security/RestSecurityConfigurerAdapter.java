package pl.skoltun.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

public abstract class RestSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationManagerBuilder auth;

    protected <T> HttpSecurity configureSecurity(UserDetailsService detailsService, String... permitAllMatchers) throws Exception {
        configureDetailsService(detailsService);
        HttpSecurity http = getHttp();
        http
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(permitAllMatchers).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(new RestLoginSuccessHandler())
                .failureHandler(new RestLoginFailureHandler())
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
        return http;
    }


    @Autowired
    protected AuthenticationManagerBuilder authenticationManagerBuilder() {
        return auth;
    }

    protected <T> void configureDetailsService(UserDetailsService detailsService) throws Exception {
        AuthenticationManagerBuilder managerBuilder = authenticationManagerBuilder();
        managerBuilder.userDetailsService(detailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}