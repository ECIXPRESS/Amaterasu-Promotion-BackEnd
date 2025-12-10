package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Adapters;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Promotion;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port.PromotionRepositoryProvider;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryRequests.PromotionDocument;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryResponses.PromotionRepositoryResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Mappers.PromotionRepositoryMapper;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Repositories.MongoPromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PromotionRepositoryAdapter implements PromotionRepositoryProvider {
    private final MongoPromotionRepository mongoPromotionRepository;

    @Override
    public PromotionRepositoryResponse save(Promotion promotion) {
        mongoPromotionRepository.save(PromotionRepositoryMapper.ReceiptToDocument(promotion));
        return PromotionRepositoryMapper.DocumentToResponse(mongoPromotionRepository.findById(promotion.getPromotionId()).get());
    }

    @Override
    public PromotionRepositoryResponse getPromotionById(String promotionId) {
        PromotionDocument promotionDocument = mongoPromotionRepository.findById(promotionId).orElse(null);
        return PromotionRepositoryMapper.DocumentToResponse(promotionDocument);
    }

    @Override
    public List<PromotionRepositoryResponse> getPromotionsByProductId(String productId) {
        List<PromotionDocument> documents = mongoPromotionRepository.findAll();
        return documents.stream()
                .filter(document -> document.getProductId().equals(productId))
                .map(PromotionRepositoryMapper::DocumentToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionRepositoryResponse> getAll() {
        List<PromotionDocument> documents = mongoPromotionRepository.findAll();
        return documents.stream()
                .map(PromotionRepositoryMapper::DocumentToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Promotion promotion) {
        mongoPromotionRepository.delete(PromotionRepositoryMapper.ReceiptToDocument(promotion));
    }
}
