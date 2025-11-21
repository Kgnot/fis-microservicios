package uni.fis.multimedia.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Data
@Table(name = "multimedia")
public class MultimediaEntity {
    @Id @GeneratedValue
    private Long id;

    private String url;
    private String tipoArchivo;

}