package uni.fis.pago.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fis.pago.Entity.OrdenItem;

@Repository
public interface OrdenItemRepository extends JpaRepository<OrdenItem, Integer> {
    List<OrdenItem> findById_orden_compra(Integer idOrdenCompra);
    boolean existsById_orden_compra(Integer idOrdenCompra);
}
