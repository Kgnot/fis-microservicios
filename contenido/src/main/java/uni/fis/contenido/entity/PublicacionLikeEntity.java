package uni.fis.contenido.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "publicacion_like")
public class PublicacionLikeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idUsuario;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private PublicacionEntity publicacion;

    @Column(name = "likes")
    private int likes; // Lombok generará getLikes() y setLikes()
}
