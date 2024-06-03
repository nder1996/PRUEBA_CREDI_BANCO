package crediBanco.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

public class ValidationUtilCard extends ValidationUtil{


    public  String privateNumberCard(String cadena) {
        if (cadena.length() <= 4) {
            return "************"+cadena; // La cadena ya tiene la longitud deseada
        } else {
            // Mostrar solo los últimos 4 caracteres
            return "**********"+cadena.substring(cadena.length() - 4);
        }
    }

    public  String generateCardNumber(String productId) {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder(productId);
        for (int i = 0; i < 10; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }

    public  String createDateExpirationCard() {
        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();
        // Añadir 3 años a la fecha actual
        LocalDate futureDate = currentDate.plusYears(3);
        // Crear un formateador para el formato "MM/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        // Formatear la fecha futura
        return futureDate.format(formatter);
    }

    public boolean validateFormatIdCard(String input) {
        if (input.length() != 16) {
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public  boolean validateFormateDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
