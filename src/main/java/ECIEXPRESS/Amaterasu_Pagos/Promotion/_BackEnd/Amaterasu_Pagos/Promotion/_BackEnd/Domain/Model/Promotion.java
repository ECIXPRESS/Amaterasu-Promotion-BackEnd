package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.PromotionValidationException;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.CreatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.UpdatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

import static ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Utils.DateUtils.TIMESTAMP_FORMAT;

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
        if(request.productId() == null || request.productId().isBlank()){
            log.error("Product id is required");
            throw new PromotionValidationException("Product id is required");
        }
        if(request.endDate() == null){
            log.error("End date is required");
            throw new PromotionValidationException("End date is required");
        }
        if(request.startDate() == null){
            log.error("Start date is required");
            throw new PromotionValidationException("Start date is required");
        }
        if(request.promotionMultiplier() <= 0){
            log.error("Promotion multiplier must be greater than 0");
            throw new PromotionValidationException("Promotion multiplier must be greater than 0");
        }
        if(request.startDate().after(request.endDate())){
            log.error("Promotion start date must be before end date");
            throw new PromotionValidationException("Promotion start date must be before end date");
        }
        Date now = new Date();
        if (request.endDate().before(now)) {
            log.error("Promotion end date must be in the future");
            throw new PromotionValidationException("Promotion end date must be in the future");
        }

        boolean nowInWindow =
                !request.startDate().after(now) &&
                        !request.endDate().before(now);

        if (!request.isActive() && nowInWindow) {
            log.error("Promotion must be active when now is between startDate and endDate");
            throw new PromotionValidationException("Promotion must be active when now is between startDate and endDate");
        }

        if (request.isActive() && !nowInWindow) {
            log.error("Promotion can't be active when now is outside startDate/endDate");
            throw new PromotionValidationException("Promotion can't be active when now is outside startDate/endDate");
        }
        this.promotionId = UUID.randomUUID().toString();
        this.productId = request.productId();
        this.isActive = request.isActive();
        this.endDate = DateUtils.formatDate(request.endDate(), TIMESTAMP_FORMAT);
        this.startDate = DateUtils.formatDate(request.startDate(), TIMESTAMP_FORMAT);
        this.promotionMultiplier = request.promotionMultiplier();
        return this;
    }

    public void update(UpdatePromotionRequest request) {
        if(request.endDate() != null){
            this.endDate = DateUtils.formatDate(request.endDate(), TIMESTAMP_FORMAT);
        }
        if(request.startDate() != null){
            this.startDate = DateUtils.formatDate(request.startDate(), TIMESTAMP_FORMAT);
        }
        if(request.promotionMultiplier() > 0){
            this.promotionMultiplier = request.promotionMultiplier();
        }
    }

    public double applyPromotion(double totalPrice){
        double discountedAmount = totalPrice * promotionMultiplier;
        return totalPrice - discountedAmount;
    }

    public boolean activeStatusChanged(){
        if(DateUtils.parseDate(this.endDate, TIMESTAMP_FORMAT).before(new Date())){
            this.isActive = false;
            return true;
        }
        return false;
    }
}
