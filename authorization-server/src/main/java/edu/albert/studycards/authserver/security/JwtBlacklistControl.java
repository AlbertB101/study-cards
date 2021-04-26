package edu.albert.studycards.authserver.security;

import edu.albert.studycards.authserver.repository.JwtBlacklistRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklistControl {
    
    private JwtBlacklistRepository jwtBlacklistRepository;
    
    public JwtBlacklistControl(JwtBlacklistRepository jwtBlacklistRepository) {
        this.jwtBlacklistRepository = jwtBlacklistRepository;
    }
    
    @Scheduled(initialDelay = 1000 * 60 * 60 * 24, fixedDelay = 1000 * 60 * 60 * 24)
    public void clearBlacklistOnceADay() {
        jwtBlacklistRepository.deleteAllExpired();
    }
}
