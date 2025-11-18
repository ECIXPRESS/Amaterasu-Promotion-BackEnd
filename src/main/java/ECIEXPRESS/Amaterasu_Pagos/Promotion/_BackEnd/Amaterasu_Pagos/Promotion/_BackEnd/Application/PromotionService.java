package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model.*;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Port.*;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Exception.*;


import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public PromotionModel createPromotion(PromotionModel promotion) {
        Optional<PromotionModel> existingPromotion = promotionRepository.findByCode(promotion.getCode());
        if (existingPromotion.isPresent()) {
            throw new PromotionException("Promotion code already exists: " + promotion.getCode());
        }
        if (promotion.getStartDate().isAfter(promotion.getEndDate())) {
            throw new PromotionException("Start date must be before end date");
        }
        return promotionRepository.save(promotion);
    }

    public Optional<PromotionModel> getPromotionById(String id) {
        return promotionRepository.findById(id);
    }

    public Optional<PromotionModel> getPromotionByCode(String code) {
        return promotionRepository.findByCode(code);
    }

    public List<PromotionModel> getActivePromotions() {
        return promotionRepository.findActivePromotions();
    }

    public List<PromotionModel> getPromotionsByProduct(String product) {
        return promotionRepository.findByProduct(product);
    }

    public List<PromotionModel> getPromotionsByPaymentMethod(String paymentMethod) {
        return promotionRepository.findByPaymentMethod(paymentMethod);
    }

    public List<PromotionModel> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public List<PromotionModel> getEligiblePromotions(String product, String paymentMethod, Double amount) {
        BigDecimal purchaseAmount = BigDecimal.valueOf(amount);
        return promotionRepository.findEligiblePromotions(product, paymentMethod, amount);
    }

    public PromotionModel applyPromotion(String promotionCode, String product, String paymentMethod, Double amount) {
        Optional<PromotionModel> promotionOpt = promotionRepository.findByCode(promotionCode);
        if (promotionOpt.isEmpty()) {
            throw new PromotionException("Promotion not found: " + promotionCode);
        }
        PromotionModel promotion = promotionOpt.get();
        BigDecimal purchaseAmount = BigDecimal.valueOf(amount);
        if (!promotion.canApply(purchaseAmount, product, paymentMethod)) {
            throw new PromotionException("Promotion cannot be applied to this purchase");
        }
        promotion.incrementUsage();
        return promotionRepository.save(promotion);
    }

    public BigDecimal calculateDiscount(String promotionCode, BigDecimal originalAmount) {
        Optional<PromotionModel> promotionOpt = promotionRepository.findByCode(promotionCode);
        if (promotionOpt.isEmpty()) {
            return BigDecimal.ZERO;
        }
        PromotionModel promotion = promotionOpt.get();
        return promotion.calculateDiscount(originalAmount);
    }

    public PromotionModel updatePromotion(String id, PromotionModel updatedPromotion) {
        Optional<PromotionModel> existingPromotion = promotionRepository.findById(id);
        if (existingPromotion.isEmpty()) {
            throw new PromotionException("Promotion not found: " + id);
        }

        PromotionModel promotion = existingPromotion.get();
        promotion.setName(updatedPromotion.getName());
        promotion.setDescription(updatedPromotion.getDescription());
        promotion.setType(updatedPromotion.getType());
        promotion.setDiscountValue(updatedPromotion.getDiscountValue());
        promotion.setMinimumPurchaseAmount(updatedPromotion.getMinimumPurchaseAmount());
        promotion.setUsageLimit(updatedPromotion.getUsageLimit());
        promotion.setStartDate(updatedPromotion.getStartDate());
        promotion.setEndDate(updatedPromotion.getEndDate());
        promotion.setIsActive(updatedPromotion.getIsActive());
        promotion.setApplicableProduct(updatedPromotion.getApplicableProduct());
        promotion.setPaymentMethodType(updatedPromotion.getPaymentMethodType());

        return promotionRepository.save(promotion);
    }

    public boolean deletePromotion(String id) {
        return promotionRepository.deleteById(id);
    }

    public void deactivateExpiredPromotions() {
        List<PromotionModel> activePromotions = promotionRepository.findActivePromotions();
        LocalDateTime now = LocalDateTime.now();

        for (PromotionModel promotion : activePromotions) {
            if (promotion.getEndDate().isBefore(now)) {
                promotion.setIsActive(false);
                promotionRepository.save(promotion);
            }
        }
    }
}