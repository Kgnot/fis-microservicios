package uni.fis.usuario.service;

import uni.fis.usuario.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();

    Optional<UserDto> findById(int id);

    boolean save(UserDto user);


}
