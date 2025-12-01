package uni.fis.contenido.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "comentario")
public class ComentarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "comentario_padre_id")
    private ComentarioEntity comentarioPadre;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private PublicacionEntity publicacion;

    @ManyToOne
    @JoinColumn(name = "contenido_id")
    private ContenidoEntity contenido;
}
