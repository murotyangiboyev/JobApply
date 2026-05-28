package org.example.jobapply.service;

import org.example.jobapply.dto.AuthRequest;
import org.example.jobapply.dto.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    AuthResponse register(AuthRequest request);
    AuthResponse login(AuthRequest request);
}
