package crediBanco.service;


import com.sun.tools.javac.Main;
import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;

import crediBanco.model.response.ApiResponseJson;
import crediBanco.repository.CardRepository;
import crediBanco.util.ResponseApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import crediBanco.util.ValidationUtil;

import static crediBanco.util.ValidationUtil.generateRandomDouble;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class CardService implements ICardService{

    @Autowired
    CardRepository cardRepository;

    ResponseApiBuilder responseBuilder = new ResponseApiBuilder();

    ValidationUtil validationUtil = new ValidationUtil();

    @Override
    public ApiResponse<String> getAllCard() {
        ApiResponse<String> response = new ApiResponse<>();
        Map<String, Object> data = new HashMap<>();
        try {
            List<CardEntity> cardEntityList = this.cardRepository.findAll();
            data = validationUtil.convertEntityToMap(cardEntityList,"cards");
            //data.put("cards",cardEntityList);
           /* data = validationUtil.convertValidatorDTOToMap(cardEntityList);*/
            response = this.responseBuilder.successRespuesta(data);
        } catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor : " + ex.getMessage());
        }
        return response;
    }



    @Override
    public ApiResponse<String> myMethod() {
        return null;
    }

    @Override
    public ApiResponse<String> getCardById(String idCard) {
        ApiResponse<String> response = new ApiResponse<>();
        Map<String, Object> data = new HashMap<>();
        try {
            CardEntity card  = this.cardRepository.findById(idCard).get();
            data =  this.validationUtil.convertEntityToMap(card,"card");
            response = this.responseBuilder.successRespuesta(data);
        } catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor : "+ex.getMessage());
            response = this.responseBuilder.errorRespuesta("INVALID_CARD_NUMBER");
        }
        return response;
    }

}
