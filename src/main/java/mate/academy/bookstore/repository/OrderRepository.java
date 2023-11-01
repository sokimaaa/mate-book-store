package mate.academy.bookstore.repository;

import java.util.List;
import mate.academy.bookstore.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findAllByUserId(Long userId, Pageable pageable);
}
