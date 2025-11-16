package uni.fis.usuario.mapper;

import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.response.UserResponse;
import uni.fis.usuario.entity.UserEntity;

public class UserMapper {

    public static UserDto entityToDto(UserEntity entity) {
        return new UserDto(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido_1(),
                entity.getApellido_2()
        );
    }

    public static UserEntity dtoToEntity(UserDto userDto) {
        return new UserEntity();
    }

    public static UserResponse dtoToResponse(UserDto userDto){
        return new UserResponse(
                userDto.id(),
                userDto.nombre(),
                userDto.apellido_1()
        );
    }

}
