package uni.fis.contenido.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


import lombok.Data;


@Entity
@Data
@Table(name = "publicacion")
public class PublicacionEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ManyToOne
    @JoinColumn(name = "contenido_id")
    private ContenidoEntity contenido;

    @ManyToOne
    @JoinColumn(name = "multimedia_id")
    private Long multimedia;

    @ManyToOne
    @JoinColumn(name = "foro_id")
    private Long foro;

    private int likes;
}