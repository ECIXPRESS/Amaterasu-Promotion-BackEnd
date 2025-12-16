package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order.Dto.OrderResponses.OrderItemResponse;

import java.util.List;

public interface OrderProvider {
    public List<OrderItemResponse> getOrderItemsById(String orderId);
}
