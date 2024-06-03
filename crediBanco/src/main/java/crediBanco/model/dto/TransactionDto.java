package crediBanco.model.dto;


import crediBanco.entity.CardEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDto {

    private Integer idTransaction;

    private Date transactionDate;

    private String state;

    private String Idcard;

    private Double price;

    private Date createdAt;

    private Date updatedAt;
}
