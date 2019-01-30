package pl.skoltun.spring.session.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.session.MapSessionRepository;

import java.util.concurrent.ConcurrentHashMap;

public interface MapSessionRepositoryConfig {
    @Bean
    default MapSessionRepository sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }
}