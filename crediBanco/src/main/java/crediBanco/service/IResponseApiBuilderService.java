package crediBanco.service;

import com.google.gson.JsonObject;
import crediBanco.model.response.ApiResponse;

import java.util.Map;

public interface IResponseApiBuilderService {


    public ApiResponse<String> successRespuesta(Map<String, Object> data);

    public ApiResponse<String> errorRespuesta(String code);

    public  <T> Map<String, Object> convertEntityToMapSinCambios(T objeto, String nameData );

    public Map<String, Object> converterAllMap(Object obj, String nameData);

    public  <T> Map<String, Object> convertirObjetoAMap(T objeto,String nameData);
}
