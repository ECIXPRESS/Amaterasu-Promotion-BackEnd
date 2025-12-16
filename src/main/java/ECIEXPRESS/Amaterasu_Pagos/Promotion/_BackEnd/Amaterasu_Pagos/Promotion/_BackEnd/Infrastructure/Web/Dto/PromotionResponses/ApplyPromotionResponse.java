package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses;

import java.util.List;

public record ApplyPromotionResponse(
        double finalAmount,
        List<String> appliedPromotions){
}
