package mate.academy.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@SQLDelete(sql = "UPDATE shopping_carts SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(name = "shopping_carts")
public class ShoppingCart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private User user;

  @OneToMany(mappedBy = "shoppingCart", orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<CartItem> cartItems = new ArrayList<>();

  @Column(nullable = false)
  private boolean deleted = false;
}
