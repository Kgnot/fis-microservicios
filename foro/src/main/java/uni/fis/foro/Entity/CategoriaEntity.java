package uni.fis.foro.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "categoria")
public class CategoriaEntity {
    @Id @GeneratedValue
    private Long id;
    private String nombre;

}
