package com.furkan.ecommerce.config;

import com.furkan.ecommerce.config.service.UserDetailsImpl;
import com.furkan.ecommerce.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityAuditorAware implements AuditorAware<User> {

    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof UserDetailsImpl)
                .map(UserDetailsImpl.class::cast)
                .map(UserDetailsImpl::getUser);
    }
}
