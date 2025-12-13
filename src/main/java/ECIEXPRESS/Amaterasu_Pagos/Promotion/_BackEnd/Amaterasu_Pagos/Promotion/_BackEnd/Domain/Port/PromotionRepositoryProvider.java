package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Promotion;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryResponses.PromotionRepositoryResponse;

import java.util.List;

public interface PromotionRepositoryProvider {
    PromotionRepositoryResponse save(Promotion promotion);
    PromotionRepositoryResponse getPromotionById(String promotionId);
    List<PromotionRepositoryResponse> getPromotionsByProductId(String productId);
    PromotionRepositoryResponse getActivePromotionByProductId(String productId);
    List<PromotionRepositoryResponse> getActivePromotions();
    List<PromotionRepositoryResponse> getAll();
    void delete(Promotion promotion);
}
