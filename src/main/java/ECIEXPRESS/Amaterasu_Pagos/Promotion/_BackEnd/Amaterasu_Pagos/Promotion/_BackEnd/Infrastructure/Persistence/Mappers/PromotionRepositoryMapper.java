package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Mappers;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Promotion;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryRequests.PromotionDocument;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryResponses.PromotionRepositoryResponse;

public class PromotionRepositoryMapper {
    public static PromotionRepositoryResponse DocumentToResponse(PromotionDocument promotionDocument){
        return new PromotionRepositoryResponse(promotionDocument);
    }

    public static PromotionDocument ReceiptToDocument(Promotion promotion) {
        PromotionDocument promotionDocument = new PromotionDocument();
        promotionDocument.setPromotionId(promotion.getPromotionId());
        promotionDocument.setProductId(promotion.getProductId());
        promotionDocument.setActive(promotion.isActive());
        promotionDocument.setEndDate(promotion.getEndDate());
        promotionDocument.setStartDate(promotion.getStartDate());
        promotionDocument.setPromotionMultiplier(promotion.getPromotionMultiplier());
        return promotionDocument;
    }
}
