package pl.skoltun.spring.session.cookie;

import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public interface CookieSerializerConfig {
    String DEFAULT_COOKIE_NAME = "Authentication";
    String DEFAULT_COOKIE_PATH = "/";
    Boolean DEFAULT_HTTP_ONLY = true;

    @Bean
    default CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(getCookieName());
        serializer.setCookiePath(getCookiePath());
        serializer.setUseHttpOnlyCookie(getDefaultHttpOnly());
        return serializer;
    }

    default String getCookieName() {
        return DEFAULT_COOKIE_NAME;
    }

    default String getCookiePath() {
        return DEFAULT_COOKIE_PATH;
    }

    default Boolean getDefaultHttpOnly() {
        return DEFAULT_HTTP_ONLY;
    }
}
