package crediBanco.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorDetailApiResponse {

    private  Integer codeHttp;
    private  String codeName;
    private  String codeDescripcion;

    public static final List<ErrorDetailApiResponse> ERRORS = Arrays.asList(
            new ErrorDetailApiResponse(400, "INVALID_CARD_NUMBER", "El número de tarjeta que ingresaste no es válido"),
            new ErrorDetailApiResponse(400, "INSUFFICIENT_FUNDS", "No tienes suficiente dinero en tu cuenta para realizar la transacción"),
            new ErrorDetailApiResponse(400, "UPDATE_RECORD_ERROR", "Hubo un error al intentar actualizar la información solicitada"),
            new ErrorDetailApiResponse(400, "INVALID_FORMAT_INPUT", "la informacion ingresada no corresponde con el formato solicitado"),
            new ErrorDetailApiResponse(400, "INTERNAL_SERVER_ERROR", "Ocurrió un error en el servidor"),
            new ErrorDetailApiResponse(400, "UPDATE_RECORD_ERROR_CARD", "Hubo un error al momento de recargar la tarjeta"),
            new ErrorDetailApiResponse(400, "NO_DATA_AVAILABLE", "No se encontraron registros que coincidan con los datos proporcionados."),
            new ErrorDetailApiResponse(400, "ERROR_CREATE_TRANSACTION", "Error al crear una transaction")
            );

}


