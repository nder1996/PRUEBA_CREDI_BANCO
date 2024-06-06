package crediBanco.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ApiResponse<T> {

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


    @Override
    public String toString() {
        return "ApiResponse{" +
                "data=" + data +
                ", error=" + error +
                '}';
    }
}
