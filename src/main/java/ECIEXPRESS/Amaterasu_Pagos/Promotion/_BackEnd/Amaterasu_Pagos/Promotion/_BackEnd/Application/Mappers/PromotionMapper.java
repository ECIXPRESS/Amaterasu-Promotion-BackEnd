package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Mappers;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Promotion;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryResponses.PromotionRepositoryResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.GetPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.UpdatePromotionResponse;

public class PromotionMapper {
    public static GetPromotionResponse repositoryResponseToGetResponse(PromotionRepositoryResponse response){
        if (response == null || response.promotionDocument() == null) return null;
        return new GetPromotionResponse(
                response.promotionDocument().getPromotionId(),
                response.promotionDocument().getProductId(),
                response.promotionDocument().isActive(),
                response.promotionDocument().getEndDate(),
                response.promotionDocument().getStartDate(),
                response.promotionDocument().getPromotionMultiplier()
        );
    }

    public static Promotion repositoryResponseToPromotion(PromotionRepositoryResponse response){
        if (response == null || response.promotionDocument() == null) return null;
        return new Promotion(
                response.promotionDocument().getPromotionId(),
                response.promotionDocument().getProductId(),
                response.promotionDocument().isActive(),
                response.promotionDocument().getEndDate(),
                response.promotionDocument().getStartDate(),
                response.promotionDocument().getPromotionMultiplier()
        );
    }

    public static UpdatePromotionResponse repositoryResponseToUpdateResponse(PromotionRepositoryResponse response){
        if (response == null || response.promotionDocument() == null) return null;
        return new UpdatePromotionResponse(
                response.promotionDocument().getPromotionId(),
                response.promotionDocument().getEndDate(),
                response.promotionDocument().getStartDate(),
                response.promotionDocument().getPromotionMultiplier()
        );
    }
}
