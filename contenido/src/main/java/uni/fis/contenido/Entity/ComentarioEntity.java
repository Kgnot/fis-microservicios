package uni.fis.contenido.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.contenido.entity.ContenidoEntity;
import lombok.Data;


@Entity
@Data
@Table(name = "comentario")
public class ComentarioEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Comentario comentarioPadre;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private PublicacionEntity publicacion;

    @OneToOne
    @JoinColumn(name = "contenido_id")
    private ContenidoEntity contenido;


}