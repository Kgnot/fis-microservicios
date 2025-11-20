package uni.fis.pago.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fis.pago.Entity.OrdenItem;

import java.util.List;

@Repository
public interface OrdenItemRepository extends JpaRepository<OrdenItem, Integer> {
    List<OrdenItem> findById_orden_compra(Integer idOrdenCompra);
}
