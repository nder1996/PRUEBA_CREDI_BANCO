package crediBanco.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "CARD")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardEntity {

    @Id // Cambiado a javax.persistence.Id
    @Column(name = "id_card", nullable = false, length = 16)
    private String idCard;

    @Column(name = "expiration_date", nullable = false, length = 7)
    private String expirationDate;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "owner_name", nullable = false, length = 260)
    private String ownerName;

    @Column(name = "state", nullable = false, length = 1)
    private String state;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}