package crediBanco.service;


import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;

import crediBanco.repository.CardRepository;
import crediBanco.util.ValidationUtilCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import crediBanco.util.ValidationUtil;
import org.springframework.transaction.annotation.Transactional;



@Service
public class CardService  extends  ValidationUtilCard  implements  ICardService  {


    private final CardRepository cardRepository;
    private final ResponseApiBuilderService apiBuilderService;

    @Autowired
    public CardService(CardRepository cardRepository, ResponseApiBuilderService apiBuilderService) {
        this.cardRepository = cardRepository;
        this.apiBuilderService = apiBuilderService;
    }



    @Override
    public ApiResponse<String> getAllCard() {
        try {
            List<CardEntity> cardEntityList = this.cardRepository.findAll();
            if(cardEntityList!=null && !cardEntityList.isEmpty()){
                Map<String, Object>  data = this.apiBuilderService.convertEntityToMapSinCambios(cardEntityList,"cards");
                return this.apiBuilderService.successRespuesta(data);
            }else{
                return this.apiBuilderService.errorRespuesta("NO_DATA_AVAILABLE");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }
    }



    @Override
    public ApiResponse<String> getCardById(String idCard) {
        try {
            CardEntity card  = this.cardRepository.findById(idCard).get();
            if(card!=null && card.getIdCard()!=null){
                Map<String, Object> data = this.apiBuilderService.convertEntityToMapSinCambios(card,"card");
                return this.apiBuilderService.successRespuesta(data);
            }else{
                return this.apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }
    }

    @Override
    public ApiResponse<String> generateCard(String productId) {
        Map<String, Object> data = new HashMap<>();
        CardEntity card = new CardEntity();
        CardEntity cardNew = new CardEntity();
        try {
            if (productId.length() == 6) {
                String cardNumber = this.generateCardNumber(productId);
                card.setIdCard(cardNumber);
                card.setOwnerName("");
                card.setBalance(0.0);
                card.setState("I");
                card.setExpirationDate(this.createDateExpirationCard()); // Expiry date set to 3 years from now
                cardNew = cardRepository.saveAndFlush(card);
                if(cardNew!=null && cardNew.getIdCard()!=null){
                    data = this.apiBuilderService.convertEntityToMapSinCambios(cardNew,"card");
                    return this.apiBuilderService.successRespuesta(data);
                }else{
                    return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
                }
            }else{
                return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }

    }

    @Override
    @Transactional
    public ApiResponse<String> activateCard(Map<String, Object> requestData){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            if (!this.validateMapJson(requestData, "cardId",null)) {
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }
            String idCard = this.convertObjectToString(requestData.get("cardId"));
            if (!this.validateFormatIdCard(idCard)) {
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }
            if(this.validateFormatIdCard(idCard)==true) {
                CardEntity card = this.cardRepository.findById(idCard).get();
                if (card != null && !card.getIdCard().isEmpty()) {
                    Integer filasModificas = this.cardRepository.activeCard(idCard);
                    Map<String, Object> data1 = new HashMap<>();
                    if (filasModificas != null && filasModificas > 0) {
                        data1.put("mensaje", "La tarjeta se activó con éxito");
                        Map<String, Object> data = this.apiBuilderService.convertEntityToMapSinCambios(data1, "card_active");
                        data1.put("card", this.privateNumberCard(card.getIdCard()));
                        data1.put("activation_date", dateFormat.format(new Date()));
                        return this.apiBuilderService.successRespuesta(data);
                    } else {
                        return  this.apiBuilderService.errorRespuesta("UPDATE_RECORD_ERROR");
                    }
                } else {
                    return this.apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER");
                }
            }else{
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> blockCard(String idCard){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            if(this.validateFormatIdCard(idCard)==true) {
                CardEntity card = this.cardRepository.findById(idCard).get();
                if (card != null && card.getIdCard() != null) {
                    Integer filasModificas = this.cardRepository.blockCard(idCard);
                    if(filasModificas!=null && filasModificas>0){
                        String formattedDate = dateFormat.format(new Date());
                        Map<String, Object> data1 = new HashMap<>();
                        data1.put("mensaje","la tarjeta se bloqueo con exito");
                        data1.put("card",this.privateNumberCard(card.getIdCard()));
                        data1.put("lockout_date",dateFormat.format(new Date()));
                        Map<String, Object> data = this.apiBuilderService.convertEntityToMapSinCambios(data1,"card_blocked");
                        return this.apiBuilderService.successRespuesta(data);
                    }else{
                        return this.apiBuilderService.errorRespuesta("UPDATE_RECORD_ERROR");
                    }
                }else{
                    return this.apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER");
                }
            }else {
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> reloadBalanceCard(Map<String, Object> requestData) {
        Map<String, Object> data = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            // Validar que requestData contenga los campos necesarios
            if (!this.validateMapJson(requestData, "cardId", "balance")) {
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }

            // Convertir los datos del mapa a los tipos correspondientes
            String idCard = this.convertObjectToString(requestData.get("cardId"));
            String balanceStr = this.convertObjectToString(requestData.get("balance"));

            // Validar el formato de idCard y balanceStr
            if (!this.validateFormatIdCard(idCard) || !this.validateFormateDouble(balanceStr)) {
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }

            // Convertir balanceStr a Double y validar que sea un número positivo
            Double balance = Double.parseDouble(balanceStr);
            if (balance <= 0) {
                return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
            }
            // Recargar saldo en la tarjeta
            CardEntity card = this.cardRepository.getByIdCard(idCard);
            if(card!=null && !card.getIdCard().isEmpty()){
                Double balanceTotal = card.getBalance() + balance;
                Integer filasModificas = this.cardRepository.reloadBalance(idCard, balanceTotal);
                if (filasModificas != null && filasModificas > 0) {
                    String formattedDate = dateFormat.format(new Date());
                    data.put("mensaje", "La tarjeta se recargó con éxito.");
                    data.put("fecha_balance",formattedDate);
                    data.put("card",this.privateNumberCard(card.getIdCard()));
                    data.put("owner_name",card.getOwnerName());
                    return this.apiBuilderService.successRespuesta(data);
                } else {
                    return this.apiBuilderService.errorRespuesta("UPDATE_RECORD_ERROR_CARD");
                }
            }else{
                return this.apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER");
            }
        }catch (NumberFormatException e) {
             System.out.println(e.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }
    }



    @Override
    public ApiResponse<String> getByCardBalance(String idCard){
        try {
            if(this.validateFormatIdCard(idCard)==true){
                CardEntity card = this.cardRepository.findById(idCard).get();
                if(card!=null && card.getIdCard()!=null){
                    card.setIdCard(this.privateNumberCard(idCard));
                    Map<String, Object> data = this.apiBuilderService.convertEntityToMap(card,"card");
                    return this.apiBuilderService.successRespuesta(data);
                }else{
                    return this.apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT");
                }
            }else{
                return this.apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER");
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return this.apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR");
        }
    }

}
