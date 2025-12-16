package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) { super(message); }
    public ExternalServiceException(String message, Throwable cause) { super(message, cause); }
}