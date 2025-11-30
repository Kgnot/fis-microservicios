package uni.fis.contenido.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;


@Entity
@Data
@Table(name = "comentario")
public class ComentarioEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private ComentarioEntity comentarioPadre;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private PublicacionEntity publicacion;

    @OneToOne
    @JoinColumn(name = "contenido_id")
    private ContenidoEntity contenido;


}