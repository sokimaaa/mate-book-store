package mate.academy.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@SQLDelete(sql = "UPDATE orders SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status;

  @Column(name = "total", nullable = false)
  private BigDecimal total;

  @CreationTimestamp
  @Column(name = "order_date", nullable = false)
  private LocalDateTime orderDate;

  @Column(name = "shipping_address", nullable = false)
  private String shippingAddress;

  @OneToMany(mappedBy = "order")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<OrderItem> orderItems = new HashSet<>();

  @Column(nullable = false)
  private boolean deleted = false;

  public enum Status {
    NEW,
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELED
  }
}
