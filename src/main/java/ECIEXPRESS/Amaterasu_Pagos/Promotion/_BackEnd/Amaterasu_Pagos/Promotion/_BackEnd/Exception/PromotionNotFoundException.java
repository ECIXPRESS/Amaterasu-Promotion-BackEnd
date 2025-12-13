package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception;

public class PromotionNotFoundException extends RuntimeException {
    public PromotionNotFoundException(String promotionId) {
        super("Promotion not found: " + promotionId);
    }
}