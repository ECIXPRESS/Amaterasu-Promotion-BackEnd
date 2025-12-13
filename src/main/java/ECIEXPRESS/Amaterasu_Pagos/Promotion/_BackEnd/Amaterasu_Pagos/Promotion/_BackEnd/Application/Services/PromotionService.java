package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Services;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Mappers.PromotionMapper;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Ports.PromotionUseCases;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.Promotion;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port.OrderProvider;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port.PromotionRepositoryProvider;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.ActivePromotionAlreadyExistsException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.PromotionNotFoundException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Clients.Order.Dto.OrderResponses.OrderItemResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryResponses.PromotionRepositoryResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.ApplyPromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.CreatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.UpdatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.ApplyPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.GetPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.UpdatePromotionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class PromotionService implements PromotionUseCases {
    private PromotionRepositoryProvider promotionRepositoryProvider;
    private OrderProvider orderProvider;

    @Override
    public List<GetPromotionResponse> getAllPromotions() {
        checkActivePromotions();
        return promotionRepositoryProvider.getAll().stream()
                .map(PromotionMapper::repositoryResponseToGetResponse)
                .toList();
    }

    @Override
    public GetPromotionResponse getPromotionById(String id) {
        checkActivePromotions();
        PromotionRepositoryResponse repo = promotionRepositoryProvider.getPromotionById(id);
        if (repo == null || repo.promotionDocument() == null) {
            throw new PromotionNotFoundException(id);
        }
        return PromotionMapper.repositoryResponseToGetResponse(repo);
    }

    @Override
    public void createPromotion(CreatePromotionRequest request) {
        checkActivePromotions();

        PromotionRepositoryResponse active = promotionRepositoryProvider.getActivePromotionByProductId(request.productId());
        if (active != null && active.promotionDocument() != null) {
            throw new ActivePromotionAlreadyExistsException(request.productId());
        }

        Promotion promotion = new Promotion().createPromotion(request);
        promotionRepositoryProvider.save(promotion);
    }

    @Override
    public UpdatePromotionResponse updatePromotion(UpdatePromotionRequest request) {
        checkActivePromotions();

        PromotionRepositoryResponse repo = promotionRepositoryProvider.getPromotionById(request.promotionId());
        if (repo == null || repo.promotionDocument() == null) {
            throw new PromotionNotFoundException(request.promotionId());
        }

        Promotion promotion = PromotionMapper.repositoryResponseToPromotion(repo);
        promotion.update(request);
        return PromotionMapper.repositoryResponseToUpdateResponse(promotionRepositoryProvider.save(promotion));
    }

    @Override
    public ApplyPromotionResponse applyPromotion(ApplyPromotionRequest request) {
        List<OrderItemResponse> orderItems = orderProvider.getOrderItemsById(request.orderId());

        double finalAmount = 0;
        List<String> appliedPromotions = new ArrayList<>();

        for (OrderItemResponse item : orderItems) {
            PromotionRepositoryResponse active = promotionRepositoryProvider.getActivePromotionByProductId(item.productId());
            Promotion promotion = PromotionMapper.repositoryResponseToPromotion(active);

            if (promotion != null) {
                double discountedAmount = promotion.applyPromotion(item.subtotal().doubleValue());
                finalAmount += discountedAmount;
                appliedPromotions.add(promotion.getPromotionId());
            } else {
                finalAmount += item.subtotal().doubleValue();
            }
        }
        return new ApplyPromotionResponse(finalAmount, appliedPromotions);
    }

    @Override
    public void deletePromotion(String id) {
        PromotionRepositoryResponse repo = promotionRepositoryProvider.getPromotionById(id);
        if (repo == null || repo.promotionDocument() == null) {
            throw new PromotionNotFoundException(id);
        }
        promotionRepositoryProvider.delete(PromotionMapper.repositoryResponseToPromotion(repo));
    }

    @Override
    public void checkActivePromotions(){
        promotionRepositoryProvider.getActivePromotions().forEach(promotionResponse -> {
            Promotion oldPromotion = PromotionMapper.repositoryResponseToPromotion(promotionResponse);
            if(oldPromotion.activeStatusChanged()){
                promotionRepositoryProvider.save(oldPromotion);
            }
        });
    }
}
