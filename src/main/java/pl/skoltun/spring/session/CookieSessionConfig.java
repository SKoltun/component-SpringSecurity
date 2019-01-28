package pl.skoltun.spring.session;

import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class CookieSessionConfig {
    private static final String DEFAULT_COOKIE_NAME = "Authentication";
    private static final String DEFAULT_COOKIE_PATH = "/";
    private static final Boolean DEFAULT_HTTP_ONLY = true;

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(getCookieName());
        serializer.setCookiePath(getCookiePath());
        serializer.setUseHttpOnlyCookie(getDefaultHttpOnly());
        return serializer;
    }

    protected String getCookieName(){
        return DEFAULT_COOKIE_NAME;
    }

    protected String getCookiePath(){
        return DEFAULT_COOKIE_PATH;
    }

    protected Boolean getDefaultHttpOnly() {
        return DEFAULT_HTTP_ONLY;
    }
}
