package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Promotion;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.PromotionValidationException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.CreatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.UpdatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Utils.DateUtils.TIMESTAMP_FORMAT;
import static org.junit.jupiter.api.Assertions.*;

class PromotionTest {

    @Test
    void createPromotion_throwsPromotionValidationException_whenMultiplierIsZeroOrLess() {
        Date now = new Date();
        Date start = new Date(now.getTime() - 60_000);
        Date end = new Date(now.getTime() + 60_000);

        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1",
                true,
                end,
                start,
                0.0
        );

        PromotionValidationException ex = assertThrows(
                PromotionValidationException.class,
                () -> new Promotion().createPromotion(req)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("multiplier"));
    }

    @Test
    void createPromotion_throwsPromotionValidationException_whenInactiveButNowInWindow() {
        Date now = new Date();
        Date start = new Date(now.getTime() - 60_000);
        Date end = new Date(now.getTime() + 60_000);

        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1",
                false, // <- inválido si now está dentro de la ventana
                end,
                start,
                0.10
        );

        PromotionValidationException ex = assertThrows(
                PromotionValidationException.class,
                () -> new Promotion().createPromotion(req)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("must be active"));
    }

    @Test
    void createPromotion_setsFields_whenValid() {
        Date now = new Date();
        Date start = new Date(now.getTime() - 60_000);
        Date end = new Date(now.getTime() + 60_000);

        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1",
                true,
                end,
                start,
                0.10
        );

        Promotion promotion = new Promotion().createPromotion(req);

        assertNotNull(promotion.getPromotionId());
        assertEquals("prod-1", promotion.getProductId());
        assertTrue(promotion.isActive());
        assertNotNull(promotion.getStartDate());
        assertNotNull(promotion.getEndDate());
        assertEquals(0.10, promotion.getPromotionMultiplier(), 1e-9);
    }

    @Test
    void update_updatesOnlyProvidedFields() {
        Promotion promotion = new Promotion(
                "promo-1",
                "prod-1",
                true,
                DateUtils.formatDate(new Date(System.currentTimeMillis() + 120_000), TIMESTAMP_FORMAT),
                DateUtils.formatDate(new Date(System.currentTimeMillis() - 120_000), TIMESTAMP_FORMAT),
                0.10
        );

        Date newEnd = new Date(System.currentTimeMillis() + 3600_000);

        promotion.update(new UpdatePromotionRequest("promo-1", newEnd, null, 0.20));

        assertEquals(0.20, promotion.getPromotionMultiplier(), 1e-9);
        assertEquals(DateUtils.formatDate(newEnd, TIMESTAMP_FORMAT), promotion.getEndDate());
        assertNotNull(promotion.getStartDate()); // no se tocó
    }

    @Test
    void activeStatusChanged_deactivatesWhenEndDateIsPast() {
        Promotion promotion = new Promotion(
                "promo-1",
                "prod-1",
                true,
                DateUtils.formatDate(new Date(System.currentTimeMillis() - 120_000), TIMESTAMP_FORMAT), // pasado
                DateUtils.formatDate(new Date(System.currentTimeMillis() - 3600_000), TIMESTAMP_FORMAT),
                0.10
        );

        boolean changed = promotion.activeStatusChanged();

        assertTrue(changed);
        assertFalse(promotion.isActive());
    }
    @Test
    void createPromotion_throws_whenProductIdMissing() {
        Promotion promotion = new Promotion();
        CreatePromotionRequest req = new CreatePromotionRequest(
                "   ", true,
                new Date(System.currentTimeMillis() + 60_000),
                new Date(System.currentTimeMillis() - 60_000),
                0.10
        );

        PromotionValidationException ex = assertThrows(PromotionValidationException.class,
                () -> promotion.createPromotion(req));

        assertEquals("Product id is required", ex.getMessage());
    }

    @Test
    void createPromotion_throws_whenEndDateNull() {
        Promotion promotion = new Promotion();
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1", true,
                null,
                new Date(System.currentTimeMillis() - 60_000),
                0.10
        );

        PromotionValidationException ex = assertThrows(PromotionValidationException.class,
                () -> promotion.createPromotion(req));

        assertEquals("End date is required", ex.getMessage());
    }

    @Test
    void createPromotion_throws_whenStartDateNull() {
        Promotion promotion = new Promotion();
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1", true,
                new Date(System.currentTimeMillis() + 60_000),
                null,
                0.10
        );

        PromotionValidationException ex = assertThrows(PromotionValidationException.class,
                () -> promotion.createPromotion(req));

        assertEquals("Start date is required", ex.getMessage());
    }

    @Test
    void createPromotion_throws_whenMultiplierNotPositive() {
        Promotion promotion = new Promotion();
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1", true,
                new Date(System.currentTimeMillis() + 60_000),
                new Date(System.currentTimeMillis() - 60_000),
                0.0
        );

        PromotionValidationException ex = assertThrows(PromotionValidationException.class,
                () -> promotion.createPromotion(req));

        assertEquals("Promotion multiplier must be greater than 0", ex.getMessage());
    }

    @Test
    void createPromotion_throws_whenStartAfterEnd() {
        Promotion promotion = new Promotion();
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1", true,
                new Date(System.currentTimeMillis() + 60_000),
                new Date(System.currentTimeMillis() + 120_000),
                0.10
        );

        PromotionValidationException ex = assertThrows(PromotionValidationException.class,
                () -> promotion.createPromotion(req));

        assertEquals("Promotion start date must be before end date", ex.getMessage());
    }

    @Test
    void createPromotion_throws_whenEndDateInPast() {
        Promotion promotion = new Promotion();
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1", true,
                new Date(System.currentTimeMillis() - 60_000),
                new Date(System.currentTimeMillis() - 120_000),
                0.10
        );

        PromotionValidationException ex = assertThrows(PromotionValidationException.class,
                () -> promotion.createPromotion(req));

        assertEquals("Promotion end date must be in the future", ex.getMessage());
    }

    @Test
    void createPromotion_throws_whenNowInWindowButInactive() {
        Promotion promotion = new Promotion();
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1", false,
                new Date(System.currentTimeMillis() + 60_000),
                new Date(System.currentTimeMillis() - 60_000),
                0.10
        );

        PromotionValidationException ex = assertThrows(PromotionValidationException.class,
                () -> promotion.createPromotion(req));

        assertEquals("Promotion must be active when now is between startDate and endDate", ex.getMessage());
    }

    @Test
    void createPromotion_throws_whenNowOutsideWindowButActive() {
        Promotion promotion = new Promotion();
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1", true,
                new Date(System.currentTimeMillis() + 120_000),
                new Date(System.currentTimeMillis() + 60_000),
                0.10
        );

        PromotionValidationException ex = assertThrows(PromotionValidationException.class,
                () -> promotion.createPromotion(req));

        assertEquals("Promotion can't be active when now is outside startDate/endDate", ex.getMessage());
    }

    @Test
    void createPromotion_ok_populatesFields() {
        Promotion promotion = new Promotion();
        CreatePromotionRequest req = new CreatePromotionRequest(
                "prod-1", true,
                new Date(System.currentTimeMillis() + 120_000),
                new Date(System.currentTimeMillis() - 60_000),
                0.10
        );

        Promotion created = promotion.createPromotion(req);

        assertNotNull(created.getPromotionId());
        assertFalse(created.getPromotionId().isBlank());
        assertEquals("prod-1", created.getProductId());
        assertTrue(created.isActive());
        assertEquals(0.10, created.getPromotionMultiplier(), 1e-9);
        assertNotNull(created.getStartDate());
        assertNotNull(created.getEndDate());
    }

    @Test
    void update_updatesOnlyNonNullAndPositive() {
        Promotion promotion = new Promotion("promo-1", "prod-1", true, "end-old", "start-old", 0.10);

        UpdatePromotionRequest req = new UpdatePromotionRequest(
                "promo-1",
                new Date(System.currentTimeMillis() + 999_999),
                null,
                0.25
        );

        promotion.update(req);

        assertEquals("promo-1", promotion.getPromotionId());
        assertEquals("prod-1", promotion.getProductId());
        assertEquals(0.25, promotion.getPromotionMultiplier(), 1e-9);
        assertNotNull(promotion.getEndDate());
        assertEquals("start-old", promotion.getStartDate());
    }

    @Test
    void applyPromotion_calculatesDiscountedAmount() {
        Promotion promotion = new Promotion("promo-1", "prod-1", true, "end", "start", 0.10);

        double result = promotion.applyPromotion(1000.0);

        // discountedAmount = totalPrice * 0.10 => 100
        // return totalPrice - discountedAmount => 900
        assertEquals(900.0, result, 1e-9);
    }
}