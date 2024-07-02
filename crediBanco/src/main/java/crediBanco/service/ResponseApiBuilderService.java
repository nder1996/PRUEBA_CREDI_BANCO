package crediBanco.service;

import com.google.gson.*;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Field;
import java.util.Map;

import crediBanco.model.response.ApiResponse;
import crediBanco.model.response.ErrorDetailApiResponse;
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
    public  <T> Map<String, Object> convertEntityToMapSinCambios(T objeto, String nameData) {
        Map<String, Object> mapa = new HashMap<>();

        if (objeto instanceof List<?>) {
            List<?> lista = (List<?>) objeto;
            mapa.put(nameData, lista);

        } else {
            // Si es un solo elemento, ponerlo directamente en el mapa
            mapa.put(nameData, objeto);
        }

        return mapa;
    }



    public static boolean shouldExcludeField(String fieldName) {
        return "expirationDate".equals(fieldName) || "state".equals(fieldName)
                || "createdAt".equals(fieldName) || "updatedAt".equals(fieldName);
    }

    public boolean shouldExcludeFieldFecha(String fieldName) {
        return "createdAt".equals(fieldName) || "updatedAt".equals(fieldName);
    }

    public static String convertirEstado(String estado) {
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



    @Override
    // Método para convertir un objeto en un Map<String, Object>
    public  <T> Map<String, Object> convertirObjetoAMap(T objeto,String nameData) {
        Map<String, Object> mapa = new HashMap<>();
        // Obtener todos los campos declarados de la clase del objeto
        Field[] campos = objeto.getClass().getDeclaredFields();
        for (Field campo : campos) {
            campo.setAccessible(true); // Hacer accesible el campo, incluso si es privado
            // Obtener el nombre y el valor del campo
            String nombreCampo = campo.getName();
            Object valorCampo;
            try {
                valorCampo = campo.get(objeto);
            } catch (IllegalAccessException e) {
                valorCampo = null; // Manejar la excepción según sea necesario
            }

            // Aplicar lógica de exclusión y conversión si es necesario
            if (!shouldExcludeField(nombreCampo)) {
                // Aplicar lógica de conversión de estado si el campo es "state"
                if ("state".equals(nombreCampo) && valorCampo instanceof String) {
                    valorCampo = convertirEstado((String) valorCampo);
                }
                // Poner el nombre del campo y su valor en el mapa
                mapa.put(nameData, valorCampo);
            }
        }

        return mapa;
    }



}

