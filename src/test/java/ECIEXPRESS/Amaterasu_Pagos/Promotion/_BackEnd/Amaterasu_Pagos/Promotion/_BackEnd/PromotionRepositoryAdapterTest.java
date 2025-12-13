package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd;


import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Promotion;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Adapters.PromotionRepositoryAdapter;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryRequests.PromotionDocument;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryResponses.PromotionRepositoryResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Repositories.MongoPromotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PromotionRepositoryAdapterTest {

    private MongoPromotionRepository mongoPromotionRepository;
    private PromotionRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        mongoPromotionRepository = mock(MongoPromotionRepository.class);
        adapter = new PromotionRepositoryAdapter(mongoPromotionRepository);
    }

    @Test
    void save_returnsNonNullResponse_whenMongoReturnsSavedDoc() {
        Promotion promotion = new Promotion("promo-1", "prod-1", true, "end", "start", 0.10);

        PromotionDocument saved = new PromotionDocument("promo-1", "prod-1", true, "end", "start", 0.10);

        when(mongoPromotionRepository.save(any(PromotionDocument.class))).thenReturn(saved);
        // Algunas versiones de tu adapter vuelven a consultar por id después de guardar:
        when(mongoPromotionRepository.findById("promo-1")).thenReturn(Optional.of(saved));

        var response = adapter.save(promotion);

        assertNotNull(response);
        assertNotNull(response.promotionDocument());
        assertEquals("promo-1", response.promotionDocument().getPromotionId());
    }

    @Test
    void getPromotionById_returnsResponse_whenFound() {
        PromotionDocument found = new PromotionDocument("promo-1", "prod-1", true, "end", "start", 0.10);
        when(mongoPromotionRepository.findById("promo-1")).thenReturn(Optional.of(found));

        var response = adapter.getPromotionById("promo-1");

        assertNotNull(response);
        assertNotNull(response.promotionDocument());
        assertEquals("prod-1", response.promotionDocument().getProductId());
    }

    @Test
    void getAll_returnsList() {
        when(mongoPromotionRepository.findAll()).thenReturn(List.of(
                new PromotionDocument("p1", "prod-1", true, "end", "start", 0.10),
                new PromotionDocument("p2", "prod-2", false, "end", "start", 0.20)
        ));

        var all = adapter.getAll();

        assertEquals(2, all.size());
        assertEquals("p1", all.get(0).promotionDocument().getPromotionId());
    }

    @Test
    void save_mapsPromotionToDocument_andReturnsResponse() {
        MongoPromotionRepository mongo = mock(MongoPromotionRepository.class);
        PromotionRepositoryAdapter adapter = new PromotionRepositoryAdapter(mongo);

        Promotion promotion = new Promotion("promo-1", "prod-1", true, "end", "start", 0.10);

        PromotionDocument savedDoc = new PromotionDocument("promo-1", "prod-1", true, "end", "start", 0.10);
        when(mongo.save(any(PromotionDocument.class))).thenReturn(savedDoc);

        PromotionRepositoryResponse resp = adapter.save(promotion);

        ArgumentCaptor<PromotionDocument> captor = ArgumentCaptor.forClass(PromotionDocument.class);
        verify(mongo).save(captor.capture());

        PromotionDocument sent = captor.getValue();
        assertEquals("promo-1", sent.getPromotionId());
        assertEquals("prod-1", sent.getProductId());
        assertTrue(sent.isActive());
        assertEquals(0.10, sent.getPromotionMultiplier(), 1e-9);

        assertNotNull(resp);
        assertNotNull(resp.promotionDocument());
        assertEquals("promo-1", resp.promotionDocument().getPromotionId());
    }

    @Test
    void getPromotionById_returnsNull_whenNotFound() {
        MongoPromotionRepository mongo = mock(MongoPromotionRepository.class);
        PromotionRepositoryAdapter adapter = new PromotionRepositoryAdapter(mongo);

        when(mongo.findById("promo-x")).thenReturn(Optional.empty());

        PromotionRepositoryResponse resp = adapter.getPromotionById("promo-x");
        assertNull(resp);
    }

    @Test
    void getPromotionsByProductId_filtersCorrectly() {
        MongoPromotionRepository mongo = mock(MongoPromotionRepository.class);
        PromotionRepositoryAdapter adapter = new PromotionRepositoryAdapter(mongo);

        when(mongo.findAll()).thenReturn(List.of(
                new PromotionDocument("p1", "prod-1", true, "end", "start", 0.10),
                new PromotionDocument("p2", "prod-2", true, "end", "start", 0.20)
        ));

        List<PromotionRepositoryResponse> resp = adapter.getPromotionsByProductId("prod-1");

        assertEquals(1, resp.size());
        assertEquals("prod-1", resp.get(0).promotionDocument().getProductId());
    }

    @Test
    void getActivePromotionByProductId_delegatesToMongoRepo() {
        MongoPromotionRepository mongo = mock(MongoPromotionRepository.class);
        PromotionRepositoryAdapter adapter = new PromotionRepositoryAdapter(mongo);

        PromotionDocument doc = new PromotionDocument("p1", "prod-1", true, "end", "start", 0.10);
        when(mongo.findByProductIdAndIsActive("prod-1")).thenReturn(doc);

        PromotionRepositoryResponse resp = adapter.getActivePromotionByProductId("prod-1");

        verify(mongo).findByProductIdAndIsActive("prod-1");
        assertNotNull(resp);
        assertEquals("p1", resp.promotionDocument().getPromotionId());
    }

    @Test
    void getActivePromotions_mapsList() {
        MongoPromotionRepository mongo = mock(MongoPromotionRepository.class);
        PromotionRepositoryAdapter adapter = new PromotionRepositoryAdapter(mongo);

        when(mongo.getActivePromotions()).thenReturn(List.of(
                new PromotionDocument("p1", "prod-1", true, "end", "start", 0.10),
                new PromotionDocument("p2", "prod-2", true, "end", "start", 0.20)
        ));

        List<PromotionRepositoryResponse> resp = adapter.getActivePromotions();

        assertEquals(2, resp.size());
    }

    @Test
    void getAll_mapsAllDocuments() {
        MongoPromotionRepository mongo = mock(MongoPromotionRepository.class);
        PromotionRepositoryAdapter adapter = new PromotionRepositoryAdapter(mongo);

        when(mongo.findAll()).thenReturn(List.of(
                new PromotionDocument("p1", "prod-1", true, "end", "start", 0.10),
                new PromotionDocument("p2", "prod-2", true, "end", "start", 0.20)
        ));

        List<PromotionRepositoryResponse> resp = adapter.getAll();

        assertEquals(2, resp.size());
    }

    @Test
    void delete_mapsPromotionToDocument_andCallsMongoDelete() {
        MongoPromotionRepository mongo = mock(MongoPromotionRepository.class);
        PromotionRepositoryAdapter adapter = new PromotionRepositoryAdapter(mongo);

        Promotion promotion = new Promotion("promo-1", "prod-1", true, "end", "start", 0.10);

        adapter.delete(promotion);

        ArgumentCaptor<PromotionDocument> captor = ArgumentCaptor.forClass(PromotionDocument.class);
        verify(mongo).delete(captor.capture());

        assertEquals("promo-1", captor.getValue().getPromotionId());
        assertEquals("prod-1", captor.getValue().getProductId());
    }
}