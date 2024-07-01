package crediBanco.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

import crediBanco.model.response.ApiResponse;
import crediBanco.model.response.ErrorDetailApiResponse;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import java.util.*;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class ResponseApiBuilderService implements IResponseApiBuilderService{



    private final ModelMapper modelMapper = new ModelMapper();

    private final Gson gson = new GsonBuilder().create();


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
    public ApiResponse<String> successRespuesta(Map<String, Object> data) {
        ApiResponse<String>  response = new ApiResponse<>();
        try{
            response.setError(null);
            response.setMeta(new ApiResponse.Meta("Operación Exitosa",200));
            response.setData(data);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return response;
    }

    @Override
    public ApiResponse<String> errorRespuesta(String code) {
        ApiResponse<String>  response = new ApiResponse<>();
        try{
            List<ErrorDetailApiResponse> errorDetailsList = getErrorDetails();
            ErrorDetailApiResponse errorDetail = this.byErrorDetailsListXCode(code,errorDetailsList);
            response.setData(null);
            response.setMeta(new ApiResponse.Meta("Solicitud Incorrecta",errorDetail.getCodeHttp()));
            response.setError(new ApiResponse.ErrorDetails(errorDetail.getCodeName(),errorDetail.getCodeDescripcion()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return response;
    }

    @Override
    public Map<String, Object> converterAllMap(Object obj, String nameData) {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        JsonElement jsonElement = JsonParser.parseString(gson.toJson(obj));

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement stateElement = jsonObject.get("state");

            if (stateElement != null && stateElement.isJsonPrimitive()) {
                String estado = stateElement.getAsString();
                String estadoConvertido = convertirEstado(estado);
                jsonObject.addProperty("state", estadoConvertido);
            }

            map.put(nameData, gson.fromJson(jsonObject, Object.class));
        } else {
            // Manejar otros tipos de JsonElement (JsonArray, JsonPrimitive, etc.)
        }

        return map;
    }



    @Override
    public Map<String, Object> convertEntityToMapSinCambios(Object obj, String nameData) {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        String json = gson.toJson(obj);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        for (var entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if ("state".equals(key)) {
                String estado = value.getAsString();
                String estadoConvertido = convertirEstado(estado);
                jsonObject.addProperty(key, estadoConvertido);
            }
        }






        /* JsonElement jsonElement = gson.fromJson(json, JsonElement.class);*/


        return Map.of();
    }


    /*   public Map<String, Object> convertEntityToMapSinCambios(Object obj, String nameData) {
           Gson gson = new Gson();
           String json = gson.toJson(obj);
           Map<String, Object> map = new HashMap<>();

           // Convertir el objeto a un JsonElement
           JsonElement jsonElement = gson.fromJson(json, JsonElement.class);

           if (jsonElement.isJsonArray()) {
               JsonArray jsonArray = jsonElement.getAsJsonArray();
               map.put(nameData, gson.fromJson(jsonArray, Object.class));
           } else if (jsonElement.isJsonObject()) {
               JsonObject jsonObject = jsonElement.getAsJsonObject();
               for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                   String key = entry.getKey();
                   JsonElement value = entry.getValue();
                   // Aplicar reglas de exclusión y conversión
                   if (!shouldExcludeField(key) && !shouldExcludeFieldFecha(key)) {
                       if ("state".equals(key)) {
                           map.put(key, convertirEstado(value.getAsString()));
                       } else {
                           map.put(key, gson.fromJson(value, Object.class));
                       }
                   }
               }
           }

           return map;
       }
   */
    public boolean shouldExcludeField(String fieldName) {
        return "expirationDate".equals(fieldName) || "state".equals(fieldName)
                || "createdAt".equals(fieldName) || "updatedAt".equals(fieldName);
    }

    public boolean shouldExcludeFieldFecha(String fieldName) {
        return "createdAt".equals(fieldName) || "updatedAt".equals(fieldName);
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
                return estado;
        }
    }


    /*
    public Map<String, Object> convertEntityToMapSinCambios(Object obj, String nameData) {
        // Convertir el objeto en un mapa usando Gson
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> map = gson.fromJson(gson.toJson(obj), type);

        // Aplicar reglas adicionales
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true); // Permitir acceso a campos privados
            try {
                String fieldName = field.getName();
                if (shouldExcludeField(fieldName)) {
                    map.remove(fieldName);
                } else {
                    Object value = field.get(obj);
                    if (value != null && shouldExcludeFieldFecha(fieldName)) {
                        // Excluir campos de fecha si están presentes
                        map.remove(fieldName);
                    } else if ("state".equals(fieldName)) {
                        // Convertir estado si es el campo "state"
                        map.put(fieldName, convertirEstado((String) value));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Agregar el nombre al mapa
        map.put("name", nameData);

        return map;
    }

*/
/*
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
                return estado;
        }
    }

*/


}

