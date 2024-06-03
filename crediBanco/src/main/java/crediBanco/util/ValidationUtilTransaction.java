package crediBanco.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class ValidationUtilTransaction extends  ValidationUtil{





    public boolean tiene24Horas(Date fechaTransaction) {
        Calendar calendarioActual = Calendar.getInstance(TimeZone.getTimeZone("America/Bogota"));
        Date fechaActual = calendarioActual.getTime();
        Calendar calendarioTransaction = Calendar.getInstance(TimeZone.getTimeZone("America/Bogota"));
        calendarioTransaction.setTime(fechaTransaction);
        long diferenciaMillis = fechaActual.getTime() - calendarioTransaction.getTimeInMillis();
        long horasDiferencia = -1 * (diferenciaMillis / (60 * 60 * 1000));
        boolean result = horasDiferencia <= 24;
        /*System.out.println("¿Tiene más de 24 horas? " + result);*/
        return result;
    }



}
