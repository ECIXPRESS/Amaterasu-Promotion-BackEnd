package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception;

public class ActivePromotionAlreadyExistsException extends RuntimeException {
    public ActivePromotionAlreadyExistsException(String productId) {
        super("There is already an active promotion for productId: " + productId);
    }
}
