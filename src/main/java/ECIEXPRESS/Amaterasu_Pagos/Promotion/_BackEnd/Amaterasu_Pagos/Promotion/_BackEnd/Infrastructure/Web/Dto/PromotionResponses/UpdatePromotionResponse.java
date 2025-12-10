package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses;

public record UpdatePromotionResponse(
        String promotionId,
        String endDate,
        String startDate,
        double promotionMultiplier
) {
}
