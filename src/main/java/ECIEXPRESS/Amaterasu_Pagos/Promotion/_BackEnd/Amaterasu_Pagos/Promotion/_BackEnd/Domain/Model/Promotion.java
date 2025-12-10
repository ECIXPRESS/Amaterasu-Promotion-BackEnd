package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.CreatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.UpdatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
    private String promotionId;
    private String productId;
    private boolean isActive;
    private String endDate;
    private String startDate;
    private double promotionMultiplier;

    public Promotion createPromotion(CreatePromotionRequest request){
        return null;
    }

    public void update(UpdatePromotionRequest request) {
        if(request.endDate() != null){
            this.endDate = DateUtils.formatDate(request.endDate(), DateUtils.TIMESTAMP_FORMAT);
        }
        if(request.startDate() != null){
            this.startDate = DateUtils.formatDate(request.startDate(), DateUtils.TIMESTAMP_FORMAT);
        }
        if(request.promotionMultiplier() <= 0){
            this.promotionMultiplier = request.promotionMultiplier();
        }
    }
}
