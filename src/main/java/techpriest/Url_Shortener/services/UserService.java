package techpriest.Url_Shortener.services;

import org.springframework.stereotype.Service;

import techpriest.Url_Shortener.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    

}
 