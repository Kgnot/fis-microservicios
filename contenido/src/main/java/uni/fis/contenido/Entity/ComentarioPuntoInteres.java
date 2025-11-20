package uni.fis.contenido.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import uni.fis.contenido.Entity.ContenidoEntity;
import lombok.Data;



@Entity
@Data
@Table(name = "comentarioPuntoInteres")
public class ComentarioPuntoInteresEntity {
    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "contenido_id")
    private ContenidoEntity contenido;

    private Long idPunto;
}