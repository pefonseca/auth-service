package auth.service.domain.service;

public interface AuthService {

    String register(String name, String email, String password);
    String login(String email, String password);

}
