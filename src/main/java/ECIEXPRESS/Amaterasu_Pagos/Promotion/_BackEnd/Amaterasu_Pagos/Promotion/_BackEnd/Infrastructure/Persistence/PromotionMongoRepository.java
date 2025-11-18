package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence;

import edu.eci.amaterasu.promotion.domain.model.PromotionModel;
import edu.eci.amaterasu.promotion.domain.port.PromotionRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionMongoRepository extends MongoRepository<PromotionModel, String>, PromotionRepository {

    @Override
    default PromotionModel save(PromotionModel promotion) {
        return save(promotion);
    }

    @Override
    default Optional<PromotionModel> findById(String id) {
        return findById(id);
    }

    Optional<PromotionModel> findByCode(String code);

    @Query("{ 'isActive': true, 'startDate': { '$lte': ?0 }, 'endDate': { '$gte': ?0 } }")
    List<PromotionModel> findActivePromotions();

    default List<PromotionModel> findActivePromotions() {
        return findActivePromotions(LocalDateTime.now());
    }

    List<PromotionModel> findByApplicableProduct(String product);

    List<PromotionModel> findByPaymentMethodType(String paymentMethod);

    @Override
    default List<PromotionModel> findAll() {
        return findAll();
    }

    @Override
    default boolean deleteById(String id) {
        if (existsById(id)) {
            deleteById(id);
            return true;
        }
        return false;
    }

    @Query("{ 'isActive': true, " +
            "'startDate': { '$lte': ?2 }, " +
            "'endDate': { '$gte': ?2 }, " +
            "'$or': [ " +
            "  { 'applicableProduct': ?0 }, " +
            "  { 'applicableProduct': null } " +
            "], " +
            "'$or': [ " +
            "  { 'paymentMethodType': ?1 }, " +
            "  { 'paymentMethodType': null } " +
            "], " +
            "'$or': [ " +
            "  { 'minimumPurchaseAmount': { '$lte': ?3 } }, " +
            "  { 'minimumPurchaseAmount': null } " +
            "] }")
    List<PromotionModel> findEligiblePromotions(String product, String paymentMethod, LocalDateTime now, Double amount);

    @Override
    default List<PromotionModel> findEligiblePromotions(String product, String paymentMethod, Double amount) {
        return findEligiblePromotions(product, paymentMethod, LocalDateTime.now(), amount);
    }
}
