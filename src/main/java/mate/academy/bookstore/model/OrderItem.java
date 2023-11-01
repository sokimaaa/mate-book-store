package mate.academy.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@SQLDelete(sql = "UPDATE order_items SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(name = "order_items")
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Order order;

  @ManyToOne
  @JoinColumn(name = "book_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Book book;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private boolean deleted = false;
}
