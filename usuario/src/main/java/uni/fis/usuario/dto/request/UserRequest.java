package uni.fis.usuario.dto.request;

public record UserRequest (
        String name, // necesario
        String apellido1, // necesario
        String apellido2, // no necesario
        String fechaNacimiento, // necesario
        String documento, // necesario y unico
        Integer imgPerfil, // no necesario
        String email, // necesario y unico
        Integer strikes, // por defecto 0 al crear
        Integer idRol, // no necesario
        Integer idMultimedia,// no necesario
        String password // necesario
){
}
