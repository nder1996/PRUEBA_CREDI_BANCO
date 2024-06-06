package crediBanco.service;


import crediBanco.model.response.ApiResponse;
import crediBanco.model.response.ErrorDetailApiResponse;
import crediBanco.repository.CardRepository;
import crediBanco.repository.TransactionRepository;
import crediBanco.util.ValidationUtil;
import crediBanco.util.ValidationUtilCard;
import crediBanco.util.ValidationUtilTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class ResponseApiBuilderService implements IResponseApiBuilderService{



    public final ValidationUtilCard validationUtilCard = new ValidationUtilCard();
    public final ValidationUtilTransaction ValidationUtilTransaction = new ValidationUtilTransaction();



    public List<ErrorDetailApiResponse> getErrorDetails() {
        return ErrorDetailApiResponse.ERRORS;
    }


    public ErrorDetailApiResponse byErrorDetailsListXCode(String codeName , List<ErrorDetailApiResponse> errorDetailsList){
        try {
            Optional<ErrorDetailApiResponse> optionalErrorDetail = errorDetailsList.stream()
                    .filter(errorDetail -> errorDetail.getCodeName().equals(codeName))
                    .findFirst();
            if (optionalErrorDetail.isPresent()) {
                ErrorDetailApiResponse errorDetail = optionalErrorDetail.get();
                return errorDetail;
            }
        } catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor : "+ex.getMessage());
            return null;
        }
        return null;
    }



    @Override
    public ApiResponse<String> successRespuesta(Map<String, Object> data){
        Map<String, Object> metaJson = new HashMap<>();
        Map<String, Object> dataJson = new HashMap<>();
        try {
            metaJson = this.metaRespuestaServidor("Operación Exitosa",200);
            ApiResponse<String> successResponse = new ApiResponse<>(data, metaJson, null);
            return successResponse;
        } catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor: " + ex.getMessage());
            return null;
        }
    }

    @Override
    public ApiResponse<String> errorRespuesta(String code){
        Map<String, Object> meta = new HashMap<>();
        try {
            List<ErrorDetailApiResponse> errorDetailsList = getErrorDetails();
            ErrorDetailApiResponse errorDetail = this.byErrorDetailsListXCode(code,errorDetailsList);
            ApiResponse.ErrorDetails error = new ApiResponse.ErrorDetails(errorDetail.getCodeName(), errorDetail.getCodeDescripcion());
            meta = this.metaRespuestaServidor("Solicitud incorrecta",errorDetail.getCodeHttp());
            ApiResponse<String> response = new ApiResponse<>(null, meta, error);
            return response;
        } catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor : "+ex.getMessage());
            return null;
        }
    }




    public Map<String, Object> metaRespuestaServidor(String message , Integer status){
        Map<String, Object> meta = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        try {
            meta.put("message", message);
            meta.put("status", status);
            response.put("meta", meta);

            return meta;
        }catch (Exception ex) {
            System.out.println("Error al crear el json Meta en la respuesta del servidor : "+ex.getMessage());
            return null;
        }
    }




    public  Map<String, Object> convertEntityToMap(Object obj , String nameData) {
        Map<String, Object> result = new HashMap<>();
        if (obj instanceof List<?>) {
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Object entity : (List<?>) obj) {
                dataList.add(convertSingleEntityToMap(entity));
            }
            result.put(nameData, dataList);
        } else {
            result.put(nameData, convertSingleEntityToMap(obj));
        }

        return result;
    }

    public Map<String, Object> convertSingleEntityToMap(Object obj) {
        Map<String, Object> entityMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                String key = field.getName();
                if (!shouldExcludeFieldFecha(key) && value != null) {
                    if ("state".equals(key)) {
                        String estadoConvertido = convertirEstado(value.toString());
                        entityMap.put(key, estadoConvertido);
                    } else if (!shouldExcludeFieldFecha(key)) {
                        entityMap.put(key, value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return entityMap;
    }


    public String convertEntityToMapSinCambiosString(Object obj, String nameData) {
        Map<String, Object> result = new HashMap<>();

        if (obj instanceof List<?>) {
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Object entity : (List<?>) obj) {
                dataList.add(convertSingleEntityToMapSinCambios(entity));
            }
            result.put(nameData, dataList);
        } else if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            if (map.keySet().stream().allMatch(key -> key instanceof String) &&
                    map.values().stream().allMatch(value -> value instanceof Object)) {
                result.put(nameData, map);
            }
        } else {
            result.put(nameData, convertSingleEntityToMapSinCambios(obj));
        }

        return result.toString();
    }





    @Override
    public Map<String, Object> convertEntityToMapSinCambios(Object obj, String nameData) {
        Map<String, Object> result = new HashMap<>();

        if (obj instanceof List<?>) {
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Object entity : (List<?>) obj) {
                dataList.add(convertSingleEntityToMapSinCambios(entity));
            }
            result.put(nameData, dataList);
        } else if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            if (map.keySet().stream().allMatch(key -> key instanceof String) &&
                    map.values().stream().allMatch(value -> value instanceof Object)) {
                result.put(nameData, map);
            }
        } else {
            result.put(nameData, convertSingleEntityToMapSinCambios(obj));
        }

        return result;
    }

    public Map<String, Object> convertSingleEntityToMapSinCambios(Object obj) {
        Map<String, Object> entityMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                String key = field.getName();
                    if (!shouldExcludeFieldFecha(key) && value != null) {
                        if ("state".equals(key)) {
                            String estadoConvertido = convertirEstado(value.toString());
                            entityMap.put(key, estadoConvertido);
                        } else if (!shouldExcludeFieldFecha(key)) {
                            entityMap.put(key, value);
                        }
                    }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return entityMap;
    }




    public  boolean shouldExcludeField(String fieldName) {
        return "expirationDate".equals(fieldName) || "state".equals(fieldName)
                || "createdAt".equals(fieldName) || "updatedAt".equals(fieldName);
    }

    public  boolean shouldExcludeFieldFecha(String fieldName) {
        return  "createdAt".equals(fieldName) || "updatedAt".equals(fieldName);
    }


    public String convertirEstado(String estado) {
        switch (estado) {
            case "I":
                return "INACTIVO";
            case "A":
                return "ACTIVO";
            case "B":
                return "BLOQUEADO";
            case "S":
                return "ACTIVO";
            case "N":
                return "ANULADO";
            default:
                return estado; // En caso de que el estado no sea I, A o B, se retorna el mismo valor original
        }
    }
}
