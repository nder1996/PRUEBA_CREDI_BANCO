package crediBanco.service;

import crediBanco.model.response.ApiResponse;
import crediBanco.model.response.ApiResponseJson;

import java.util.List;

public interface ICardService {


    public ApiResponse<String> myMethod();


    public ApiResponse<String> getCardById(String idCard);

    public ApiResponse<String> getAllCard();
}
