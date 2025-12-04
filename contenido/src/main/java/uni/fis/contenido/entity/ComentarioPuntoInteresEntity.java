package uni.fis.contenido.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;



@Entity
@Data
@Table(name = "comentarioPuntoInteres")
public class ComentarioPuntoInteresEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contenido_id")
    private ContenidoEntity contenido;

    private Long idPunto;
}