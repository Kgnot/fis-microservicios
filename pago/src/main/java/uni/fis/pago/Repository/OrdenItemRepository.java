package uni.fis.pago.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fis.pago.Entity.OrdenItem;

@Repository
public interface OrdenItemRepository extends JpaRepository<OrdenItem, Integer> {
    List<OrdenItem> findByIdOrdenCompra(Integer idOrdenCompra);
    boolean existsByIdOrdenCompra(Integer idOrdenCompra);
}
