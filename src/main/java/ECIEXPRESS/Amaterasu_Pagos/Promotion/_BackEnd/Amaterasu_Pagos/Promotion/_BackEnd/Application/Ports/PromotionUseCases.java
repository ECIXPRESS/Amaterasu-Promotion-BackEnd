package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Ports;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.ApplyPromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.CreatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.UpdatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.ApplyPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.GetPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.UpdatePromotionResponse;

import java.util.List;

public interface PromotionUseCases {
    public List<GetPromotionResponse> getAllPromotions();
    public GetPromotionResponse getPromotionById(String id);
    public void createPromotion(CreatePromotionRequest request);
    public UpdatePromotionResponse updatePromotion(UpdatePromotionRequest request);
    public ApplyPromotionResponse applyPromotion(ApplyPromotionRequest request);
    public void deletePromotion(String id);
}
