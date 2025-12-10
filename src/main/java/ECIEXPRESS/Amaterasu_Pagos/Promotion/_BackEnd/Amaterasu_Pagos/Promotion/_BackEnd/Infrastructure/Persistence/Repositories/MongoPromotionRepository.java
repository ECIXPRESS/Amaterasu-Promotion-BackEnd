package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Repositories;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryRequests.PromotionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MongoPromotionRepository extends MongoRepository<PromotionDocument, String> {
    @Query("{ 'promotionId': ?0 }")
    PromotionDocument getPromotionById(String orderId);
}
