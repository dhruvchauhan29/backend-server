package in.software.billingSoftware.Repository;

import in.software.billingSoftware.Entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity,Long> {

    Optional<OrderEntity> findByOrderId(String orderId);

    List<OrderEntity> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COALESCE(SUM(o.grandTotal), 0) FROM OrderEntity o WHERE o.createdAt >= :start AND o.createdAt <= :end")
    Double sumSalesByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.createdAt >= :start AND o.createdAt <= :end")
    Long countByOrderDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("SELECT o FROM OrderEntity o ORDER BY o.createdAt DESC")
    List<OrderEntity> findRecentOrders();
}
