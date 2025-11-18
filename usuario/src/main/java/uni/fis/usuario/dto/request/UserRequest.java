package uni.fis.usuario.dto.request;

public record UserRequest (
        String name,
        String apellido1,
        String apellido2,
        String fechaNacimiento, // esta debe ser año-mes-dia | debe ser enviado como LocalDate
        String documento,
        Integer imgPerfil, // porque numero?
        String email,
        Integer strikes, // por defecto 0 al crear
        Integer idRol,
        Integer idMultimedia // que diferencia hay con el img
){
}
