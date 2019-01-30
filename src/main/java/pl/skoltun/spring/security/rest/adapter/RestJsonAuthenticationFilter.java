package pl.skoltun.spring.security.rest.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.skoltun.spring.security.rest.login.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class RestJsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger logger = Logger.getLogger(RestJsonAuthenticationFilter.class);

    private AuthenticationManager authenticationManager;

    public RestJsonAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = getToken(request);
        setDetails(request, token);
        return authenticationManager.authenticate(token);
    }

    private UsernamePasswordAuthenticationToken getToken(HttpServletRequest request) {
        Optional<LoginRequest> loginRequest = parseRequest(request);
        if (loginRequest.isPresent()) {
            LoginRequest req = loginRequest.get();
            return new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
        } else {
            return new UsernamePasswordAuthenticationToken("", "");
        }
    }

    private Optional<LoginRequest> parseRequest(HttpServletRequest request) {
        try (InputStream is = request.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            LoginRequest parsed = mapper.reader()
                    .forType(LoginRequest.class)
                    .readValue(is);
            return Optional.of(parsed);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            logger.debug(e);
            return Optional.empty();
        }
    }

}