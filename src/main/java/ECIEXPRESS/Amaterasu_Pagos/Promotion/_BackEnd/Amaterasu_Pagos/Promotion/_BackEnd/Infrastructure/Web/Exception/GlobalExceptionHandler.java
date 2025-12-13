package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Exception;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.*;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.Erros.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PromotionValidationException.class)
    public ResponseEntity<ApiError> handleValidation(PromotionValidationException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "PROMOTION_VALIDATION", ex.getMessage(), req);
    }

    @ExceptionHandler(PromotionNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(PromotionNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "PROMOTION_NOT_FOUND", ex.getMessage(), req);
    }

    @ExceptionHandler(ActivePromotionAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleConflict(ActivePromotionAlreadyExistsException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "PROMOTION_ALREADY_EXISTS", ex.getMessage(), req);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderNotFound(OrderNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", ex.getMessage(), req);
    }

    @ExceptionHandler(ExternalOrderServiceException.class)
    public ResponseEntity<ApiError> handleExternal(ExternalOrderServiceException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_GATEWAY, "ORDER_SERVICE_ERROR", ex.getMessage(), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected error", req);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String code, String message, HttpServletRequest req) {
        return ResponseEntity.status(status)
                .body(new ApiError(code, message, Instant.now(), req.getRequestURI()));
    }
}
