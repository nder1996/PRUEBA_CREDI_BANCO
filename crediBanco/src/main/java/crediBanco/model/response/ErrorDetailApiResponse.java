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
            new ErrorDetailApiResponse(400, "INVALID_CARD_NUMBER", "El número de tarjeta proporcionado es inválido."),
            new ErrorDetailApiResponse(401, "CARD_EXPIRED", "La tarjeta de crédito o débito utilizada para la transacción ha expirado."),
            new ErrorDetailApiResponse(402, "INSUFFICIENT_FUNDS", "No hay suficientes fondos en la cuenta para completar la transacción."),
            new ErrorDetailApiResponse(403, "EXCEEDS_DAILY_LIMIT", "La transacción excede el límite diario de transacciones o el monto de la transacción."),
            new ErrorDetailApiResponse(404, "DUPLICATE_TRANSACTION", "La transacción ya ha sido procesada."),
            new ErrorDetailApiResponse(500, "INTERNAL_ERROR", "Ha ocurrido un error interno en el servidor durante el procesamiento de la transacción."),
            new ErrorDetailApiResponse(404, "ACCOUNT_NOT_FOUND", "La cuenta especificada no existe."),
            new ErrorDetailApiResponse(423, "ACCOUNT_LOCKED", "La cuenta ha sido bloqueada debido a demasiados intentos de inicio de sesión fallidos."),
            new ErrorDetailApiResponse(401, "INVALID_PIN", "El PIN proporcionado es incorrecto."),
            new ErrorDetailApiResponse(401, "INVALID_CREDENTIALS", "Las credenciales proporcionadas son inválidas."),
            new ErrorDetailApiResponse(498, "EXPIRED_TOKEN", "El token de autenticación proporcionado ha expirado."),
            new ErrorDetailApiResponse(422, "INVALID_ACCOUNT_TYPE", "El tipo de cuenta especificado no es válido o no está permitido para la operación solicitada."),
            new ErrorDetailApiResponse(403, "UNAUTHORIZED_ACCESS", "Se ha intentado acceder a un recurso sin la autorización adecuada."),
            new ErrorDetailApiResponse(405, "TRANSACTION_NOT_PERMITTED", "La transacción solicitada no está permitida para la cuenta o el usuario."),
            new ErrorDetailApiResponse(429, "RATE_LIMIT_EXCEEDED", "Se ha superado el límite de solicitudes permitidas en un período de tiempo determinado."),
            new ErrorDetailApiResponse(422, "INVALID_CARD_NUMBER_CREATION", "El número de tarjeta generado es inválido."),
            new ErrorDetailApiResponse(410, "CARD_EXPIRED_CREATION", "La fecha de expiración de la tarjeta generada está en el pasado."),
            new ErrorDetailApiResponse(422, "INVALID_CARD_TYPE", "El tipo de tarjeta generado no es válido o no está permitido para el servicio ofrecido."),
            new ErrorDetailApiResponse(409, "DUPLICATE_CARD", "Se intentó generar una tarjeta que ya existe en el sistema."),
            new ErrorDetailApiResponse(500, "INTERNAL_ERROR_CREATION", "Ha ocurrido un error interno en el servidor durante la generación de la tarjeta."),
            new ErrorDetailApiResponse(423, "ACCOUNT_LOCKED_STATE", "La cuenta asociada a la tarjeta generada está bloqueada."),
            new ErrorDetailApiResponse(402, "INSUFFICIENT_FUNDS_STATE", "La cuenta asociada a la tarjeta generada no tiene suficientes fondos para activar la tarjeta."),
            new ErrorDetailApiResponse(401, "INVALID_PIN_STATE", "Se intentó activar la tarjeta con un PIN incorrecto."),
            new ErrorDetailApiResponse(498, "EXPIRED_TOKEN_STATE", "El token de autenticación proporcionado para activar la tarjeta ha expirado."),
            new ErrorDetailApiResponse(422, "INVALID_ACCOUNT_TYPE_STATE", "El tipo de cuenta asociada a la tarjeta no es válido o no está permitido para la activación de tarjetas."),
            new ErrorDetailApiResponse(409, "UPDATE_RECORD_ERROR", "Error al activar la tarjeta. No se encontró ninguna tarjeta con el ID proporcionado.")

    );
}


