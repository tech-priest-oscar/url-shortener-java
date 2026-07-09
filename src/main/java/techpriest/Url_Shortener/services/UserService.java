package techpriest.Url_Shortener.services;

import techpriest.Url_Shortener.dto.CreateUserDto;
import techpriest.Url_Shortener.models.User;

public interface UserService {
    User create(CreateUserDto createUserDto);
    boolean emailExists(String email);
}
