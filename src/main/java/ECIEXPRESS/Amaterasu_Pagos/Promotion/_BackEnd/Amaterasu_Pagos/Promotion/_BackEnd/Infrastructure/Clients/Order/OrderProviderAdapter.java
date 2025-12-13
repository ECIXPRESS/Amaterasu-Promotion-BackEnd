package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port.OrderProvider;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.ExternalServiceException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.OrderNotFoundException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order.Dto.OrderResponses.OrderItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProviderAdapter implements OrderProvider {

    private final RestTemplate restTemplate;

    @Value("${microservices.order.url}")
    private String baseUrl;

    @Value("${microservices.order.base-path}")
    private String basePath;

    @Override
    public List<OrderItemResponse> getOrderItemsById(String orderId) {
        try {
            log.info("Getting order items for order: {}", orderId);

            HttpHeaders headers = createHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = buildUrl("/{orderId}/items");

            ResponseEntity<List<OrderItemResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<OrderItemResponse>>() {},
                    orderId
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                log.error("Failed to get order items for order: {}. Status: {}", orderId, response.getStatusCode());
                throw new ExternalServiceException("Failed to get order items for order: " + orderId);
            }

            return response.getBody();

        } catch (HttpClientErrorException.NotFound e) {
            throw new OrderNotFoundException(orderId);
        } catch (Exception e) {
            throw new ExternalServiceException("Error getting order items for order " + orderId + ": " + e.getMessage(), e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private String buildUrl(String pathTemplate) {
        String cleanBasePath = basePath.endsWith("/") ? basePath.substring(0, basePath.length() - 1) : basePath;
        String cleanPathTemplate = pathTemplate.startsWith("/") ? pathTemplate : "/" + pathTemplate;

        return String.format("%s%s%s", baseUrl, cleanBasePath, cleanPathTemplate);
    }
}

