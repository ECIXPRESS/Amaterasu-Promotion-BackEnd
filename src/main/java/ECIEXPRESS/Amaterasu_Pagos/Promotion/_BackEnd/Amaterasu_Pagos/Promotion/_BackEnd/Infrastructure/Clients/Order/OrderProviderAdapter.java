package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port.OrderProvider;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order.Dto.OrderResponses.OrderItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProviderAdapter implements OrderProvider {

    @Override
    public OrderItemResponse getOrderById(String orderId) {
        return null;
    }

//    @GetMapping("/{orderId}/items")
//    @Operation(summary = "Obtenen la informacion del pedido")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Items obtenidos exitosamente"),
//            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
//    })
//    public ResponseEntity<List<OrderItemResponse>> obtenerItemsDePedido(
//            @Parameter(description = "ID del pedido") @PathVariable String orderId) {
//        try {
//            List<OrderItem> items = getOrderItemsUseCase.obtenerItemsDeOrden(orderId);
//            List<OrderItemResponse> responses = items.stream()
//                    .map(this::toOrderItemResponse)
//                    .collect(Collectors.toList());
//            return ResponseEntity.ok(responses);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
