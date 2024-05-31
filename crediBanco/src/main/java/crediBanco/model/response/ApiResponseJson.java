package crediBanco.model.response;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponseJson<T> {

    private JsonObject data;
    private String meta;
    private ErrorDetails error;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public  static class ErrorDetails {
        private String code;
        private String details;
    }
}
