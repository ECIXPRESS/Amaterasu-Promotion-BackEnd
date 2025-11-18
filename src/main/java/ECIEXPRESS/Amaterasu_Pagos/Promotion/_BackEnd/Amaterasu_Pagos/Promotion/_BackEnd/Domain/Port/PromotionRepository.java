package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port;

import edu.eci.amaterasu.promotion.domain.model.PromotionModel;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository {
    PromotionModel save(PromotionModel promotion);
    Optional<PromotionModel> findById(String id);
    Optional<PromotionModel> findByCode(String code);
    List<PromotionModel> findActivePromotions();
    List<PromotionModel> findByProduct(String product);
    List<PromotionModel> findByPaymentMethod(String paymentMethod);
    List<PromotionModel> findAll();
    boolean deleteById(String id);
    List<PromotionModel> findEligiblePromotions(String product, String paymentMethod, Double amount);
}
