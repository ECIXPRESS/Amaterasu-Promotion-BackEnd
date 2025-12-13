package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) { super("Order not found: " + orderId); }
}