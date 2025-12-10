package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Services;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Mappers.PromotionMapper;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Ports.PromotionUseCases;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Promotion;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port.PromotionRepositoryProvider;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.ApplyPromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.CreatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.UpdatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.ApplyPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.GetPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.UpdatePromotionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class PromotionService implements PromotionUseCases {
    private PromotionRepositoryProvider promotionRepositoryProvider;

    @Override
    public List<GetPromotionResponse> getAllPromotions() {
        try{
            return promotionRepositoryProvider.getAll().stream()
                    .map(PromotionMapper::repositoryResponseToGetResponse)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GetPromotionResponse getPromotionById(String id) {
        try{
            return PromotionMapper.repositoryResponseToGetResponse(promotionRepositoryProvider.getPromotionById(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createPromotion(CreatePromotionRequest request) {
        try{
            Promotion promotion = new Promotion().createPromotion(request);
            promotionRepositoryProvider.save(promotion);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UpdatePromotionResponse updatePromotion(UpdatePromotionRequest request) {
        try{
            Promotion promotion = PromotionMapper.repositoryResponseToPromotion(promotionRepositoryProvider.getPromotionById(request.promotionId()));
            promotion.update(request);
            return PromotionMapper.repositoryResponseToUpdateResponse(promotionRepositoryProvider.save(promotion));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApplyPromotionResponse applyPromotion(ApplyPromotionRequest request) {
        return null;
    }

    @Override
    public void deletePromotion(String id) {
    }
}
