package uni.fis.catalogo.Model.ItemDto;


import java.math.BigDecimal;

import lombok.Data;

@Data

public class ProductoRequest {
    private String nombre;
    private BigDecimal precio;
    private int cantidad;
    private String tamaño;
    private String peso;
    private String color;
}