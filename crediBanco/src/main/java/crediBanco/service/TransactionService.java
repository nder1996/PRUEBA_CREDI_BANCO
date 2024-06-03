package crediBanco.service;


import crediBanco.entity.CardEntity;
import crediBanco.entity.TransactionEntity;
import crediBanco.model.dto.TransactionDto;
import crediBanco.model.response.ApiResponse;
import crediBanco.repository.CardRepository;
import crediBanco.repository.TransactionRepository;
import crediBanco.util.ValidationUtil;

import crediBanco.util.ValidationUtilCard;
import crediBanco.util.ValidationUtilTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransactionService extends ValidationUtilTransaction  implements ITransactionService {


    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final ResponseApiBuilderService apiBuilderService;




    @Autowired
    public TransactionService(CardRepository cardRepository,
                              TransactionRepository transactionRepository,
                              ResponseApiBuilderService apiBuilderService) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.apiBuilderService = apiBuilderService;
    }




    private final ValidationUtilCard validationUtilCard = new ValidationUtilCard();





    @Override
    @Transactional
    public  ApiResponse<String> createTransactionCard(Map<String, Object> requestData){
        try {
            if (!this.validateMapJson(requestData, "cardId","price")) {
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }
            String idCard = this.convertObjectToString(requestData.get("cardId"));
            String price = this.convertObjectToDouble(requestData.get("price"));

            if (!this.validationUtilCard.validateFormatIdCard(idCard) || !this.validationUtilCard.validateFormateDouble(price)) {
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }
            Double priceTotal = Double.parseDouble(price);
            CardEntity card = this.cardRepository.getByIdCard(idCard);

            if(card!=null && card.getIdCard()!=null){
                TransactionEntity transactionEntity = new TransactionEntity();
                transactionEntity.setCard(card);
                transactionEntity.setCreatedAt(new Date());
                transactionEntity.setState("S");
                transactionEntity.setPrice(priceTotal);
                transactionEntity.setTransactionDate(new Date());
                if(card.getBalance()>0 && card.getBalance()>priceTotal){
                    TransactionEntity transaction =  this.transactionRepository.saveAndFlush(transactionEntity);
                    if(transaction!=null && transaction.getIdTransaction()!=null){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        System.out.println("fecha transaction : "+this.parseDateFormat(transaction.getTransactionDate()));
                        TransactionDto transactionDto = new TransactionDto(transaction.getIdTransaction(),this.parseDateFormat(transaction.getTransactionDate()),transaction.getState(),
                                this.validationUtilCard.privateNumberCard(transaction.getCard().getIdCard()),transaction.getPrice(),transaction.getCreatedAt(),transaction.getUpdatedAt());
                        Map<String, Object> data = this.apiBuilderService.convertEntityToMap(transactionDto,"transaction");
                        return this.apiBuilderService.successRespuesta(data);
                    }else{
                        return this.apiBuilderService.errorRespuesta("ERROR_CREATE_TRANSACTION");
                    }
                }else{
                    return this.apiBuilderService.errorRespuesta("INSUFFICIENT_FUNDS");
                }
            }else{
                return this.apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER");
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }

    }


    @Override
    public ApiResponse<String> getByTransactionCard(Integer idTransaction){
        try{
            if(idTransaction!=null){
                TransactionEntity transaction = this.transactionRepository.findById(idTransaction).get();
                if(transaction!=null && transaction.getIdTransaction()!=null){
                    TransactionDto transactionDto = new TransactionDto(transaction.getIdTransaction(),transaction.getTransactionDate(),transaction.getState(),this.validationUtilCard.privateNumberCard(transaction.getCard().getIdCard()),transaction.getPrice(),transaction.getCreatedAt(),transaction.getUpdatedAt());
                    Map<String, Object> data = this.apiBuilderService.convertEntityToMap(transactionDto,"transaction");
                    return this.apiBuilderService.successRespuesta(data);
                }else{
                    return this.apiBuilderService.errorRespuesta("NO_DATA_AVAILABLE");
                }
            }else{
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }


    }



    public ApiResponse<String> anulateTransaction(Map<String, Object> requestData){
        try {

            // Validar que requestData contenga los campos necesarios
            if (!this.validateMapJson(requestData, "cardId", "transactionId")) {
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }

            String idCard = this.convertObjectToString(requestData.get("cardId"));
            Integer transactionId = this.convertObjectToInteger(requestData.get("transactionId")) != null && !this.convertObjectToInteger(requestData.get("transactionId")).isEmpty() ?
                    Integer.valueOf(this.convertObjectToInteger(requestData.get("transactionId"))) : 0;

            if(idCard==null || idCard.equals("vacio")==true || idCard.equals("undefined")==true ){
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }
            TransactionEntity transaction = this.transactionRepository.getByIdTransaction(transactionId);
            if(transaction!=null && transaction.getIdTransaction()!=null){
                Map<String, Object> data1 = new HashMap<>();
                if (this.tiene24Horas(transaction.getTransactionDate())==false) {
                    data1.put("mensaje","la transaction tiene mas de 24 horas , no se puede anular");
                    Map<String, Object>  data = this.apiBuilderService.convertEntityToMapSinCambios(data1,"transaction_anuled");
                    return this.apiBuilderService.successRespuesta(data);
                }else{
                    transaction.setState("N");
                    transaction.setUpdatedAt(new Date());
                    CardEntity card = this.cardRepository.getByIdCard(idCard);
                    Double devolucion = card.getBalance() + transaction.getPrice();
                    card.setBalance(devolucion);
                    this.cardRepository.saveAndFlush(card);
                    TransactionEntity transactionEntity = transactionRepository.save(transaction);
                    data1.put("mensaje","la transaction se anulo de forma correcta");
                    data1.put("anulation_date",this.parseDateFormat(new Date()));
                    Map<String, Object>  data = this.apiBuilderService.convertEntityToMapSinCambios(data1,"transaction_anuled");
                    return this.apiBuilderService.successRespuesta(data);
                }
            }else{
                return this.apiBuilderService.errorRespuesta("NO_DATA_AVAILABLE");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }
    }




}
