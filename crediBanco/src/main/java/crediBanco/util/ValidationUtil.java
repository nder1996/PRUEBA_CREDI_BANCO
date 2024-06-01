package crediBanco.util;

import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ValidationUtil {




    public static Double generateRandomDouble(double min, double max) {
        return min + (max - min) * new Random().nextDouble();
    }



    public  String recortarTarjeta(String cadena) {
        if (cadena.length() <= 4) {
            return "************"+cadena; // La cadena ya tiene la longitud deseada
        } else {
            // Mostrar solo los últimos 4 caracteres
            return "**********"+cadena.substring(cadena.length() - 4);
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


    public  String generateCardNumber(String productId) {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder(productId);
        for (int i = 0; i < 10; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }


    public  String getFutureDateWithFormat() {
        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();
        // Añadir 3 años a la fecha actual
        LocalDate futureDate = currentDate.plusYears(3);
        // Crear un formateador para el formato "MM/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        // Formatear la fecha futura
        return futureDate.format(formatter);
    }

}
