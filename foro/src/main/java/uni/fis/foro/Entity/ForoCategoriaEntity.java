package uni.fis.foro.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import uni.fis.foro.Entity.CategoriaEntity;
import uni.fis.foro.Entity.ForoEntity;
import lombok.Data;


@Entity
@Data
@Table(name = "foroCategoria")
public class ForoCategoriaEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaEntity categoria;

    @ManyToOne
    @JoinColumn(name = "foro_id")
    private ForoEntity foro;


}