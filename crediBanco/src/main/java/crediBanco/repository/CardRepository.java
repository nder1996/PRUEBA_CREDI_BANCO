package crediBanco.repository;



import crediBanco.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, String> {



    @Modifying
    @Query(value = "UPDATE CARD SET state = 'A' WHERE id_card = :idCard", nativeQuery = true)
    Integer activeCard(@Param("idCard") String idCard);

    @Modifying
    @Query(value = "UPDATE CARD SET state = 'B' WHERE id_card = :idCard", nativeQuery = true)
    Integer blockCard(@Param("idCard") String idCard);

}
