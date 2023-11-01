package mate.academy.bookstore.repository;

import java.util.Optional;
import mate.academy.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
  Optional<ShoppingCart> findByUserId(Long userId);

  @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
  @Query("SELECT sc FROM ShoppingCart sc WHERE sc.user.id = :userId")
  Optional<ShoppingCart> findByUserIdWithCartItems(@Param("userId") Long userId);
}
