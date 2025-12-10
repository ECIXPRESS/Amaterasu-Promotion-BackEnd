package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order.Dto.OrderResponses.OrderItemResponse;

public interface OrderProvider {
    public OrderItemResponse getOrderById(String orderId);
}
