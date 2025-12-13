package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order.Dto.OrderResponses;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Enums.OrderType;
import java.math.BigDecimal;

public record OrderItemResponse(
        String id,
        String orderId,
        String productId,
        String productName,
        OrderType productType,
        Integer quantity,
        BigDecimal unitPrice,
        String details,
        BigDecimal subtotal) {
}
