package mate.academy.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "cart_items")
@SQLDelete(sql = "UPDATE cart_items SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Data
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shopping_cart_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private ShoppingCart shoppingCart;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private boolean deleted = false;
}
