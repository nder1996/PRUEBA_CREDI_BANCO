package crediBanco.model.response;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse<T> {


    private Map<String, Object> data;
    private Meta meta;
    private ErrorDetails error;

    // Clase Meta para los metadatos
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private String message;
        private int status;
    }

    // Clase ErrorDetails para los detalles de error
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorDetails {
        private String code;
        private String details;
    }





    /*
    private Map<String, Object> data;
    private Map<String, Object> meta;
    private ErrorDetails error;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public  static class ErrorDetails {
        private String code;
        private String details;
    }

*/

}
