package crediBanco.service;

import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;


import java.util.List;
import java.util.Map;

public interface ICardService {


    public ApiResponse<String> getCardById(String idCard);

    public ApiResponse<String> getAllCard();

    public ApiResponse<String> generateCard(String productId);

    public ApiResponse<String> activateCard(Map<String, Object> requestData);

    public ApiResponse<String> blockCard(String idCard);

    public ApiResponse<String> reloadBalanceCard(Map<String, Object> requestData);

    public ApiResponse<String> getByCardBalance(String idCard);


}
