package crediBanco.util;

import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;

import java.lang.reflect.Field;
import java.util.*;

public class ValidationUtil {




    public static Double generateRandomDouble(double min, double max) {
        return min + (max - min) * new Random().nextDouble();
    }


    /*convertir Entity en Map*/
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
                        entityMap.put(key, recortarTarjeta(value.toString())); // Ensure String conversion
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
    /*convertir Entity en Map*/

    public static String recortarTarjeta(String cadena) {
        if (cadena.length() <= 4) {
            return cadena; // La cadena ya tiene la longitud deseada
        } else {
            // Mostrar solo los últimos 4 caracteres
            return cadena.substring(cadena.length() - 4);
        }
    }


    public static String validateToString(Object obj) {
        if (obj == null || obj.equals("undefined")) {
            return null;
        }

        if (obj instanceof String) {
            return ((String) obj).isEmpty() ? "vacio" : (String) obj;
        }

        if (obj instanceof Integer) {
            return Integer.toString((Integer) obj);
        }

        if (obj instanceof Collection<?> && ((Collection<?>) obj).isEmpty()) {
            return "vacio";
        }

        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null && !value.equals("undefined")) {
                    return value.toString();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
