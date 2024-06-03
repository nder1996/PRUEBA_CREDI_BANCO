package crediBanco.repository;


import crediBanco.entity.CardEntity;
import crediBanco.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {


    @Query(value = "select * from TRANSACTION where id_transaction = :idTransaction and  state = 'S'", nativeQuery = true)
    TransactionEntity getByIdTransaction(@Param("idTransaction") Integer idTransaction);


}
