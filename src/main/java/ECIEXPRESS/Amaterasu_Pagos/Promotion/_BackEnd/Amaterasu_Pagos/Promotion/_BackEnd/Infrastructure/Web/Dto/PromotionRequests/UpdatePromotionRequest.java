package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests;

import java.util.Date;

public record UpdatePromotionRequest(
        String promotionId,
        Date endDate,
        Date startDate,
        double promotionMultiplier
) {
}
