package uni.fis.usuario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellido_1;
    private String apellido_2;
    private LocalDateTime fechaDeNacimiento;
    @Column(name = "documento", length = 100)
    private String documento;
    private Integer imgPerfil;
    private String email;
    private Integer strikes;
    private Integer idRol;
    @Column(name = "id_multimedia")
    private Integer idMultimedia;


}
