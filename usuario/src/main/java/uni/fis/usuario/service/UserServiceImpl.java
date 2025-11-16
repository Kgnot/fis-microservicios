package uni.fis.usuario.service;

import org.springframework.stereotype.Service;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.entity.UserEntity;
import uni.fis.usuario.mapper.UserMapper;
import uni.fis.usuario.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::entityToDto)
                .toList();
    }

    @Override
    public Optional<UserDto> findById(int id) {
        return Optional.ofNullable(
                repository.findById(id)
                        .map(UserMapper::entityToDto)
                        .orElseThrow(() -> new RuntimeException("no esta el usuario")));
    }

    @Override
    public boolean save(UserDto user) {
        UserEntity entity = UserMapper.dtoToEntity(user);
        repository.save(entity);
        return true;
    }




}
