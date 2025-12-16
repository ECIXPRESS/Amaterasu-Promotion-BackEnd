package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.ExternalServiceException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.OrderNotFoundException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order.Dto.OrderResponses.OrderItemResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order.OrderProviderAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProviderAdapterTest {

    private RestTemplate restTemplate;
    private OrderProviderAdapter adapter;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        adapter = new OrderProviderAdapter(restTemplate);

        // @Value fields
        ReflectionTestUtils.setField(adapter, "baseUrl", "http://orders-service");
        ReflectionTestUtils.setField(adapter, "basePath", "/api/v1/orders");
    }

    @Test
    void getOrderItemsById_returnsItems_whenOk() {
        List<OrderItemResponse> items = List.of(
                new OrderItemResponse(
                        "item-1", "order-1", "prod-1", "Product",
                        null, 1, BigDecimal.valueOf(1000), "details", BigDecimal.valueOf(1000)
                )
        );

        ResponseEntity<List<OrderItemResponse>> response = ResponseEntity.ok(items);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class),
                any(Object.class)
        )).thenReturn(response);

        List<OrderItemResponse> result = adapter.getOrderItemsById("order-1");

        assertEquals(1, result.size());
        assertEquals("prod-1", result.get(0).productId());

        // Verifica URL y headers enviados
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        verify(restTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.GET),
                entityCaptor.capture(),
                any(ParameterizedTypeReference.class),
                eq("order-1")
        );

        assertEquals("http://orders-service/api/v1/orders/{orderId}/items", urlCaptor.getValue());

        HttpHeaders headers = entityCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertTrue(headers.getAccept().contains(MediaType.APPLICATION_JSON));
    }

    @Test
    void getOrderItemsById_throwsOrderNotFoundException_when404() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class),
                any(Object.class)
        )).thenThrow(HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "Not Found",
                HttpHeaders.EMPTY,
                new byte[0],
                StandardCharsets.UTF_8
        ));

        OrderNotFoundException ex = assertThrows(OrderNotFoundException.class,
                () -> adapter.getOrderItemsById("order-1"));

        assertEquals("Order not found: order-1", ex.getMessage());
    }

    @Test
    void getOrderItemsById_throwsExternalServiceException_whenResponseBodyNull() {
        ResponseEntity<List<OrderItemResponse>> response = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class),
                any(Object.class)
        )).thenReturn(response);

        ExternalServiceException ex = assertThrows(ExternalServiceException.class,
                () -> adapter.getOrderItemsById("order-1"));

        // El adapter lanza "Failed..." y luego lo envuelve en "Error getting..."
        assertTrue(ex.getMessage().contains("Error getting order items for order order-1"));
        assertNotNull(ex.getCause());
        assertTrue(ex.getCause() instanceof ExternalServiceException);
        assertTrue(ex.getCause().getMessage().contains("Failed to get order items for order: order-1"));
    }
}