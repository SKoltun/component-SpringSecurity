package pl.skoltun.spring.session;

import org.springframework.context.annotation.Bean;
import org.springframework.session.MapSessionRepository;

import java.util.concurrent.ConcurrentHashMap;

public abstract class MapSessionConfig {
    @Bean
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }

}