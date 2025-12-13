package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses;

public record GetPromotionResponse(
        String promotionId,
        String productId,
        boolean isActive,
        String endDate,
        String startDate,
        double promotionMultiplier
) {
}
