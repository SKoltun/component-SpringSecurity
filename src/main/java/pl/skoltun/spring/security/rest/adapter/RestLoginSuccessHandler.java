package pl.skoltun.spring.security.rest.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import pl.skoltun.spring.security.rest.login.LoginResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Collectors;

class RestLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger logger = Logger.getLogger(RestLoginSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        clearAuthenticationAttributes(request);
        LoginResponse loginResponse = buildResponse(authentication);
        try(OutputStream out = response.getOutputStream()){
            new ObjectMapper().writer()
                    .forType(LoginResponse.class)
                    .writeValue(out,loginResponse);
        }catch (IOException e){
            logger.error(e.getMessage(), e);
        }
    }

    private LoginResponse buildResponse(Authentication authentication) {
        LoginResponse response = new LoginResponse();
        response.setUsername(authentication.getName());
        response.setAuthorities(authentication.getAuthorities().stream()
                .map(String::valueOf)
                .collect(Collectors.toList()));
        return response;
    }
}