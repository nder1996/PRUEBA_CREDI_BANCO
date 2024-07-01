package crediBanco.service;

import com.google.gson.JsonObject;
import crediBanco.model.response.ApiResponse;

import java.util.Map;

public interface IResponseApiBuilderService {


    public ApiResponse<String> successRespuesta(Map<String, Object> data);

    public ApiResponse<String> errorRespuesta(String code);

    public Map<String, Object> convertEntityToMapSinCambios(Object obj, String nameData);

    public Map<String, Object> converterAllMap(Object obj, String nameData);
}
