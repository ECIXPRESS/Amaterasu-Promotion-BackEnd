package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Services.PromotionService;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Promotion;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port.OrderProvider;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port.PromotionRepositoryProvider;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.ActivePromotionAlreadyExistsException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.PromotionNotFoundException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order.Dto.OrderResponses.OrderItemResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryRequests.PromotionDocument;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryResponses.PromotionRepositoryResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.ApplyPromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.CreatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.UpdatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.ApplyPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.GetPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.UpdatePromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Utils.DateUtils.TIMESTAMP_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    private PromotionRepositoryProvider promotionRepositoryProvider;
    private OrderProvider orderProvider;

    private PromotionService service;

    @BeforeEach
    void setUp() {
        promotionRepositoryProvider = mock(PromotionRepositoryProvider.class);
        orderProvider = mock(OrderProvider.class);
        service = new PromotionService(promotionRepositoryProvider, orderProvider);
    }


    @Test
    void getPromotionById_throws_whenNotFound() {
        when(promotionRepositoryProvider.getPromotionById("promo-x")).thenReturn(null);

        assertThrows(PromotionNotFoundException.class, () -> service.getPromotionById("promo-x"));
    }

    @Test
    void getPromotionById_returnsResponse_whenOk() {
        PromotionDocument doc = new PromotionDocument(
                "promo-1", "prod-1", true, "end", "start", 0.10
        );
        when(promotionRepositoryProvider.getPromotionById("promo-1"))
                .thenReturn(new PromotionRepositoryResponse(doc));

        GetPromotionResponse resp = service.getPromotionById("promo-1");

        assertEquals("promo-1", resp.promotionId());
        assertEquals("prod-1", resp.productId());
        assertTrue(resp.isActive());
        assertEquals(0.10, resp.promotionMultiplier(), 1e-9);
    }

    @Test
    void createPromotion_throws_whenActivePromotionAlreadyExists() {
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1",
                true,
                new Date(System.currentTimeMillis() + 60_000),
                new Date(System.currentTimeMillis() - 60_000),
                0.10
        );

        PromotionDocument existing = new PromotionDocument("promo-existing", "prod-1", true, "end", "start", 0.10);
        when(promotionRepositoryProvider.getActivePromotionByProductId("prod-1"))
                .thenReturn(new PromotionRepositoryResponse(existing));

        assertThrows(ActivePromotionAlreadyExistsException.class, () -> service.createPromotion(req));
        verify(promotionRepositoryProvider, never()).save(any());
    }

    @Test
    void createPromotion_saves_whenNoActivePromotion() {
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1",
                true,
                new Date(System.currentTimeMillis() + 60_000),
                new Date(System.currentTimeMillis() - 60_000),
                0.10
        );

        when(promotionRepositoryProvider.getActivePromotionByProductId("prod-1")).thenReturn(null);

        service.createPromotion(req);

        ArgumentCaptor<Promotion> captor = ArgumentCaptor.forClass(Promotion.class);
        verify(promotionRepositoryProvider).save(captor.capture());

        Promotion saved = captor.getValue();
        assertEquals("prod-1", saved.getProductId());
        assertTrue(saved.isActive());
        assertEquals(0.10, saved.getPromotionMultiplier(), 1e-9);
        assertNotNull(saved.getPromotionId());
    }

    @Test
    void updatePromotion_throws_whenNotFound() {
        UpdatePromotionRequest req = new UpdatePromotionRequest(
                "promo-x", null, null, 0.20
        );

        when(promotionRepositoryProvider.getPromotionById("promo-x")).thenReturn(null);

        assertThrows(PromotionNotFoundException.class, () -> service.updatePromotion(req));
    }

    @Test
    void updatePromotion_updatesAndReturnsResponse_whenOk() {
        PromotionDocument existing = new PromotionDocument("promo-1", "prod-1", true, "end-old", "start-old", 0.10);
        when(promotionRepositoryProvider.getPromotionById("promo-1"))
                .thenReturn(new PromotionRepositoryResponse(existing));

        PromotionDocument savedDoc = new PromotionDocument("promo-1", "prod-1", true, "end-new", "start-new", 0.20);
        when(promotionRepositoryProvider.save(any(Promotion.class)))
                .thenReturn(new PromotionRepositoryResponse(savedDoc));

        UpdatePromotionRequest req = new UpdatePromotionRequest(
                "promo-1",
                new Date(System.currentTimeMillis() + 999_999),
                new Date(System.currentTimeMillis() - 999_999),
                0.20
        );

        UpdatePromotionResponse resp = service.updatePromotion(req);

        assertEquals("promo-1", resp.promotionId());
        assertEquals(0.20, resp.promotionMultiplier(), 1e-9);
        assertEquals("end-new", resp.endDate());
        assertEquals("start-new", resp.startDate());
    }

    @Test
    void applyPromotion_appliesDiscountOnlyWhenPromotionExists() {
        List<OrderItemResponse> items = List.of(
                new OrderItemResponse("item-1", "order-1", "prod-1", "Product 1", null, 1,
                        BigDecimal.valueOf(1000), "details", BigDecimal.valueOf(1000)),
                new OrderItemResponse("item-2", "order-1", "prod-2", "Product 2", null, 1,
                        BigDecimal.valueOf(500), "details", BigDecimal.valueOf(500))
        );

        when(orderProvider.getOrderItemsById("order-1")).thenReturn(items);

        PromotionDocument promoDoc = new PromotionDocument("promo-1", "prod-1", true, "end", "start", 0.10);
        when(promotionRepositoryProvider.getActivePromotionByProductId("prod-1"))
                .thenReturn(new PromotionRepositoryResponse(promoDoc));
        when(promotionRepositoryProvider.getActivePromotionByProductId("prod-2"))
                .thenReturn(null);

        ApplyPromotionResponse resp = service.applyPromotion(new ApplyPromotionRequest("order-1"));

        // prod-1: 1000 - (1000*0.10) = 900
        // prod-2: 500 (sin promo)
        assertEquals(1400.0, resp.finalAmount(), 1e-9);
        assertEquals(List.of("promo-1"), resp.appliedPromotions());
    }

    @Test
    void deletePromotion_throws_whenNotFound() {
        when(promotionRepositoryProvider.getPromotionById("promo-x")).thenReturn(null);
        assertThrows(PromotionNotFoundException.class, () -> service.deletePromotion("promo-x"));
    }

    @Test
    void deletePromotion_deletes_whenOk() {
        PromotionDocument doc = new PromotionDocument("promo-1", "prod-1", true, "end", "start", 0.10);
        when(promotionRepositoryProvider.getPromotionById("promo-1"))
                .thenReturn(new PromotionRepositoryResponse(doc));

        service.deletePromotion("promo-1");

        verify(promotionRepositoryProvider).delete(any(Promotion.class));
    }

    @Test
    void checkActivePromotions_savesWhenPromotionExpired() {
        Date now = new Date();
        Date pastEnd = new Date(now.getTime() - 5_000);
        Date pastStart = new Date(now.getTime() - 60_000);

        PromotionDocument doc = new PromotionDocument(
                "promo-1",
                "prod-1",
                true,
                DateUtils.formatDate(pastEnd, TIMESTAMP_FORMAT),
                DateUtils.formatDate(pastStart, TIMESTAMP_FORMAT),
                0.10
        );

        when(promotionRepositoryProvider.getActivePromotions())
                .thenReturn(List.of(new PromotionRepositoryResponse(doc)));

        when(promotionRepositoryProvider.save(any(Promotion.class)))
                .thenReturn(new PromotionRepositoryResponse(doc));

        service.checkActivePromotions();

        ArgumentCaptor<Promotion> captor = ArgumentCaptor.forClass(Promotion.class);
        verify(promotionRepositoryProvider).save(captor.capture());
        assertFalse(captor.getValue().isActive());
    }
}