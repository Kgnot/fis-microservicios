package uni.fis.pago.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ordenItem")
public class OrdenItem{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;
    @Column()
    Integer id_item;
    @Column()
    Integer cantidad;
    @Column()
    BigDecimal valor_unitario;
    @Column()
    BigDecimal subtotal;
    @Column()
    Integer id_orden_compra;
}