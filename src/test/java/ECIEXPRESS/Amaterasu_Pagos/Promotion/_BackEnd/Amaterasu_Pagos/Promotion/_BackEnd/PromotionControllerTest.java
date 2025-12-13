package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Ports.PromotionUseCases;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.UpdatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.GetPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.UpdatePromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Exception.GlobalExceptionHandler;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.PromotionController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromotionController.class)
@Import(GlobalExceptionHandler.class)
class PromotionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PromotionUseCases promotionUseCases;

    @Test
    void updatePromotion_overridesPromotionIdFromPath() throws Exception {
        long now = System.currentTimeMillis();
        String body = """
                {
                  "promotionId": "promo-body",
                  "endDate": %d,
                  "startDate": %d,
                  "promotionMultiplier": 0.25
                }
                """.formatted(now + 86_400_000, now - 86_400_000);

        when(promotionUseCases.updatePromotion(any()))
                .thenReturn(new UpdatePromotionResponse("promo-path", "end", "start", 0.25));

        mockMvc.perform(put("/api/v1/promotions/promo-path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.promotionId").value("promo-path"))
                .andExpect(jsonPath("$.promotionMultiplier").value(0.25));

        ArgumentCaptor<UpdatePromotionRequest> captor = ArgumentCaptor.forClass(UpdatePromotionRequest.class);
        verify(promotionUseCases).updatePromotion(captor.capture());
        assertEquals("promo-path", captor.getValue().promotionId());
    }

    @Test
    void getAllPromotions_returnsOk() throws Exception {
        when(promotionUseCases.getAllPromotions()).thenReturn(List.of(
                new GetPromotionResponse("promo-1", "prod-1", true, "end", "start", 0.10)
        ));

        mockMvc.perform(get("/api/v1/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].promotionId").value("promo-1"))
                .andExpect(jsonPath("$[0].productId").value("prod-1"));
    }

    @Test
    void getPromotionById_returnsOk() throws Exception {
        when(promotionUseCases.getPromotionById("promo-1"))
                .thenReturn(new GetPromotionResponse("promo-1", "prod-1", true, "end", "start", 0.10));

        mockMvc.perform(get("/api/v1/promotions/promo-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.promotionId").value("promo-1"))
                .andExpect(jsonPath("$.productId").value("prod-1"));
    }

    @Test
    void createPromotion_returns201() throws Exception {
        long now = System.currentTimeMillis();
        String body = """
                {
                  "productId": "prod-1",
                  "isActive": true,
                  "endDate": %d,
                  "startDate": %d,
                  "promotionMultiplier": 0.10
                }
                """.formatted(now + 86_400_000, now - 86_400_000);

        mockMvc.perform(post("/api/v1/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        verify(promotionUseCases).createPromotion(any());
    }

    @Test
    void getAll_returns_200_and_list() throws Exception {
        when(promotionUseCases.getAllPromotions()).thenReturn(List.of(
                new GetPromotionResponse("p1","prod-1",true,"end","start",0.1)
        ));

        mockMvc.perform(get("/api/v1/promotions"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].promotionId").value("p1"));

        verify(promotionUseCases).getAllPromotions();
    }

    @Test
    void create_returns_201() throws Exception {
        String json = """
                {
                  "productId":"prod-1",
                  "isActive":false,
                  "endDate": 1735689600000,
                  "startDate": 1735603200000,
                  "promotionMultiplier":0.1
                }
                """;

        mockMvc.perform(post("/api/v1/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(promotionUseCases).createPromotion(any());
    }

    @Test
    void delete_returns_204() throws Exception {
        mockMvc.perform(delete("/api/v1/promotions/p1"))
                .andExpect(status().isNoContent());

        verify(promotionUseCases).deletePromotion("p1");
    }
}