package crediBanco.util;

import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ValidationUtil {



    public String validateDataInput(Object obj) {
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

    public String convertObjectToString(Object obj) {
        String validatedData = validateDataInput(obj);
        if (validatedData == null) {
            return null;
        }
        if(validatedData.equals("vacio") || validatedData.equals("undefined")){
            return validatedData;
        }
        return obj.toString();
    }

    public String convertObjectToInteger(Object obj) {
        String validatedData = validateDataInput(obj);
        if (validatedData == null) {
            return null;
        }
        if(validatedData.equals("vacio") || validatedData.equals("undefined")){
            return "";
        }
        return obj.toString();
    }

    public String convertObjectToDouble(Object obj) {
        String validatedData = validateDataInput(obj);
        if (validatedData == null) {
            return null;
        }
        if(validatedData.equals("vacio") || validatedData.equals("undefined")){
            return validatedData;
        }
        return obj.toString();
    }

    public boolean validateMapJson(Map<String, Object> data, String key1, String key2) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        if  (data.size() == 2) {
            return data.get(key1) != null && data.get(key2) != null;
        }if (data.size() == 1) {
            return data.containsKey(key1) && data.get(key1) != null && key2 == null;
        }if (data.size() == 0) {
            return false;
        }

        if (!data.containsKey(key1) && !data.containsKey(key2)) {
            return false;
        }
        return validateValue(data.get(key1)) && validateValue(data.get(key2));
    }

    public boolean validateValue(Object value) {
        if (value == null || value.equals("undefined")) {
            return false;
        }
        if (value instanceof String && ((String) value).isEmpty()) {
            return false;
        }
        return true;
    }

    public Date parseDateFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String formattedDate = dateFormat.format(date);
            return dateFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
