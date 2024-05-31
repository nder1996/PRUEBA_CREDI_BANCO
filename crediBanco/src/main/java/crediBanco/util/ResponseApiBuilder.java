package crediBanco.util;


import crediBanco.model.response.ApiResponse;
import crediBanco.model.response.ApiResponseJson;
import crediBanco.model.response.ErrorDetailApiResponse;

import java.lang.reflect.Field;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;




public class ResponseApiBuilder  {


    static ValidationUtil validationUtil = new ValidationUtil();

    public List<ErrorDetailApiResponse> getErrorDetails() {
        return ErrorDetailApiResponse.ERRORS;
    }



    public ApiResponse<String> successRespuesta(Map<String, Object> data){
        Map<String, Object> metaJson = new HashMap<>();
        Map<String, Object> dataJson = new HashMap<>();
        try {
            metaJson = this.metaRespuestaServidor("Operación exitosa",200);
            ApiResponse<String> successResponse = new ApiResponse<>(data, metaJson, null);
            return successResponse;
        } catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor: " + ex.getMessage());
            return null;
        }
    }

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

    public Map<String, Object> metaRespuestaServidor(String message , Integer status){
        Map<String, Object> meta = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        try {
            meta.put("message", message);
            meta.put("status", status);
            response.put("meta", meta);
            return meta;
        }catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor : "+ex.getMessage());
            return null;
        }
    }

    public Map<String, Object> metaRespuestaServidor(Map<String, Object> data){
        Map<String, Object> dataJson = new HashMap<>();
        try {
            dataJson.put("data",data);
        }catch (Exception ex) {
            System.out.println("Error al crear la respuesta del servidor: " + ex.getMessage());
            return null;
        }
        return dataJson;
    }


    public static Map<String, Object> convertEntityToMap(Object obj , String nameData) {
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

    private static Map<String, Object> convertSingleEntityToMap(Object obj) {
        Map<String, Object> entityMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                String key = field.getName();

                if (!shouldExcludeField(key) && value != null) {
                    if ("idCard".equals(key)) {
                        entityMap.put(key, validationUtil.recortarTarjeta(value.toString())); // Ensure String conversion
                    } else {
                        entityMap.put(key, value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return entityMap;
    }

    private static boolean shouldExcludeField(String fieldName) {
        return "expirationDate".equals(fieldName) || "state".equals(fieldName)
                || "createdAt".equals(fieldName) || "updatedAt".equals(fieldName);
    }
}
