package crediBanco.service;

import crediBanco.model.response.ApiResponse;

import java.util.Map;

public interface ITransactionService {


    public ApiResponse<String> createTransactionCard(Map<String, Object> requestData);

    public ApiResponse<String> getByTransactionCard(Integer idTransaction);

    public ApiResponse<String> anulateTransaction(Map<String, Object> requestData);
}
