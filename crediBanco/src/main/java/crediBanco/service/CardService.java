package crediBanco.service;


import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;

import crediBanco.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import crediBanco.util.ValidationUtil;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService implements ICardService{

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ResponseApiBuilderService apiBuilderService;

  //  ResponseApiBuilder responseBuilder = new ResponseApiBuilder();


    private final ValidationUtil validationUtil;

    // Constructor donde se inyecta la dependencia de ValidationUtil
    public CardService(ValidationUtil validationUtil) {
        this.validationUtil = validationUtil;
    }


    @Override
    public ApiResponse<String> getAllCard() {
        ApiResponse<String> response = new ApiResponse<>();
        Map<String, Object> data = new HashMap<>();
        try {
            List<CardEntity> cardEntityList = this.cardRepository.findAll();
            data = this.apiBuilderService.convertEntityToMap(cardEntityList,"cards");
            response = this.apiBuilderService.successRespuesta(data);
        } catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor : " + ex.getMessage());
        }
        return response;
    }



    @Override
    public ApiResponse<String> getCardById(String idCard) {
        ApiResponse<String> response = new ApiResponse<>();
        Map<String, Object> data = new HashMap<>();
        try {
            CardEntity card  = this.cardRepository.findById(idCard).get();
            data = this.apiBuilderService.convertEntityToMap(card,"card");
            response = this.apiBuilderService.successRespuesta(data);
        } catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor : "+ex.getMessage());
            response = this.apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER");
        }
        return response;
    }

    @Override
    public ApiResponse<String> generateCard(String productId) {
        String cardNumber = this.validationUtil.generateCardNumber(productId);
        ApiResponse<String> response = new ApiResponse<>();
        Map<String, Object> data = new HashMap<>();
        CardEntity card = new CardEntity();
        CardEntity cardNew = new CardEntity();
        card.setIdCard(cardNumber);
        card.setOwnerName("PRUEBA JUANITO");
        card.setBalance(0.0);
        card.setState("I");
        card.setExpirationDate(this.validationUtil.getFutureDateWithFormat()); // Expiry date set to 3 years from now
        cardNew = cardRepository.saveAndFlush(card);
        data = this.apiBuilderService.convertEntityToMapSinCambios(cardNew,"card");
        response = this.apiBuilderService.successRespuesta(data);
        return response;
    }

    @Transactional
    public ApiResponse<String> activateCard(Map<String, Object> requestData){
        String idCard = (String) requestData.get("cardId");
       Integer filasModificas = this.cardRepository.activeCard(idCard);
        ApiResponse<String> response = new ApiResponse<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> data1 = new HashMap<>();
       if(filasModificas!=null && filasModificas>0){
           data1.put("mensaje","la tarjeta se actualizo con exito");
           data = this.apiBuilderService.convertEntityToMapSinCambios(data1,"card_active");
           response = this.apiBuilderService.successRespuesta(data);
       }else{
           response = this.apiBuilderService.errorRespuesta("UPDATE_RECORD_ERROR");
       }
        return response;
    }

}
