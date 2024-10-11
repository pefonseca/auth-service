package auth.service.domain.service.impl;

import auth.service.domain.model.entity.User;
import auth.service.domain.service.AuthService;
import auth.service.infra.repository.UserRepository;
import auth.service.infra.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * @param name
     * @param email
     * @param password
     * @return
     */
    @Override
    public String register(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                        .name(name)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .build();

        userRepository.save(user);

        return jwtUtils.generateToken(user.getEmail());
    }

    /**
     * @param email
     * @param password
     * @return
     */
    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        return jwtUtils.generateToken(user.getEmail());
    }
}
