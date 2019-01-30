package pl.skoltun.spring.security.rest.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

public abstract class RestSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public HttpSecurity configureSecurity(String... permitAllMatchers) throws Exception {
        HttpSecurity http = getHttp();
        http
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .addFilterAt(restCustomAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(permitAllMatchers).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
        return http;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    protected abstract <T extends PasswordEncoder> T passwordEncoder();

    @Bean
    protected abstract UserDetailsService detailsService();

    private RestJsonAuthenticationFilter restCustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        RestJsonAuthenticationFilter filter = new RestJsonAuthenticationFilter(authenticationManager);
        filter.setAuthenticationSuccessHandler(new RestLoginSuccessHandler());
        filter.setAuthenticationFailureHandler(new RestLoginFailureHandler());
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl("/login");
        return filter;
    }

}