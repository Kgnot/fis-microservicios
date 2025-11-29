package uni.fis.usuario.mapper;

import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.request.UserRequest;
import uni.fis.usuario.dto.response.UserResponse;
import uni.fis.usuario.entity.UsuarioEntity;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserDto entityToDto(UsuarioEntity entity) {
        return new UserDto(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido_1(),
                entity.getApellido_2(),
                entity.getIdRol()
        );
    }

    public static UsuarioEntity dtoToEntity(UserDto userDto) {
        return new UsuarioEntity(
                userDto.id(),
                userDto.nombre(),
                userDto.apellido_1(),
                userDto.apellido_2(),
                null,//fecha de nacimiento
                null, // idDocumento
                null, // email
                null, // strikes
                userDto.idRol(), // idRol
                null // imgPerfil
        );
    }

    public static UserResponse dtoToResponse(UserDto userDto) {
        return new UserResponse(
                userDto.id(),
                userDto.nombre(),
                userDto.apellido_1()
        );
    }

    public static UsuarioEntity requestToEntity(UserRequest userRequest) {

        return new UsuarioEntity(
                null, // id - se genera automáticamente
                userRequest.name(),
                userRequest.apellido1(),
                userRequest.apellido2(),
                parseFechaNacimiento(userRequest.fechaNacimiento()),
                DocumentoMapper.toEntity(userRequest.documento()),
                userRequest.email(),
                userRequest.strikes() != null ? userRequest.strikes() : 0, // default 0
                userRequest.idRol(),
                userRequest.imgPerfil()
        );
    }

    // helper para parsear la fecha
    private static LocalDateTime parseFechaNacimiento(String fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.trim().isEmpty()) {
            return null;
        }
        try {
            // Convierte "yyyy-MM-dd" a LocalDateTime
            return LocalDateTime.parse(fechaNacimiento + "T00:00:00");
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use: yyyy-MM-dd");
        }
    }

}