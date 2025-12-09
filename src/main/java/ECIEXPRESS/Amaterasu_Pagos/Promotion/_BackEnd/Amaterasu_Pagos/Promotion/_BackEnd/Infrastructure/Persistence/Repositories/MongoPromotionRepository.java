package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Repositories;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryRequests.PromotionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoPromotionRepository extends MongoRepository<PromotionDocument, String> {
}
