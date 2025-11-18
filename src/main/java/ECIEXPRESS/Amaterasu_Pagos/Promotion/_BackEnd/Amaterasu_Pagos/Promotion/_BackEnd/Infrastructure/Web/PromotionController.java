package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web;

import edu.eci.amaterasu.promotion.domain.model.PromotionModel;
import edu.eci.amaterasu.promotion.domain.service.PromotionService;
import edu.eci.amaterasu.promotion.dto.PromotionDto;
import edu.eci.amaterasu.promotion.exception.PromotionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    public ResponseEntity<PromotionModel> createPromotion(@RequestBody PromotionDto promotionDto) {
        try {
            PromotionModel promotion = convertToEntity(promotionDto);
            PromotionModel createdPromotion = promotionService.createPromotion(promotion);
            return ResponseEntity.ok(createdPromotion);
        } catch (PromotionException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionModel> getPromotionById(@PathVariable String id) {
        Optional<PromotionModel> promotion = promotionService.getPromotionById(id);
        return promotion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<PromotionModel> getPromotionByCode(@PathVariable String code) {
        Optional<PromotionModel> promotion = promotionService.getPromotionByCode(code);
        return promotion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<PromotionModel>> getActivePromotions() {
        List<PromotionModel> promotions = promotionService.getActivePromotions();
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/product/{product}")
    public ResponseEntity<List<PromotionModel>> getPromotionsByProduct(@PathVariable String product) {
        List<PromotionModel> promotions = promotionService.getPromotionsByProduct(product);
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/payment-method/{paymentMethod}")
    public ResponseEntity<List<PromotionModel>> getPromotionsByPaymentMethod(@PathVariable String paymentMethod) {
        List<PromotionModel> promotions = promotionService.getPromotionsByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<PromotionModel>> getEligiblePromotions(
            @RequestParam String product,
            @RequestParam String paymentMethod,
            @RequestParam Double amount) {
        List<PromotionModel> promotions = promotionService.getEligiblePromotions(product, paymentMethod, amount);
        return ResponseEntity.ok(promotions);
    }

    @PostMapping("/{code}/apply")
    public ResponseEntity<PromotionModel> applyPromotion(
            @PathVariable String code,
            @RequestParam String product,
            @RequestParam String paymentMethod,
            @RequestParam Double amount) {
        try {
            PromotionModel promotion = promotionService.applyPromotion(code, product, paymentMethod, amount);
            return ResponseEntity.ok(promotion);
        } catch (PromotionException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{code}/calculate-discount")
    public ResponseEntity<BigDecimal> calculateDiscount(
            @PathVariable String code,
            @RequestParam Double originalAmount) {
        BigDecimal discount = promotionService.calculateDiscount(code, BigDecimal.valueOf(originalAmount));
        return ResponseEntity.ok(discount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionModel> updatePromotion(@PathVariable String id, @RequestBody PromotionDto promotionDto) {
        try {
            PromotionModel promotion = convertToEntity(promotionDto);
            PromotionModel updatedPromotion = promotionService.updatePromotion(id, promotion);
            return ResponseEntity.ok(updatedPromotion);
        } catch (PromotionException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable String id) {
        boolean deleted = promotionService.deletePromotion(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/deactivate-expired")
    public ResponseEntity<Void> deactivateExpiredPromotions() {
        promotionService.deactivateExpiredPromotions();
        return ResponseEntity.ok().build();
    }

    private PromotionModel convertToEntity(PromotionDto dto) {
        PromotionModel promotion = new PromotionModel();
        promotion.setCode(dto.getCode());
        promotion.setName(dto.getName());
        promotion.setDescription(dto.getDescription());
        promotion.setType(dto.getType());
        promotion.setDiscountValue(dto.getDiscountValue());
        promotion.setMinimumPurchaseAmount(dto.getMinimumPurchaseAmount());
        promotion.setUsageLimit(dto.getUsageLimit());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotion.setIsActive(dto.getIsActive());
        promotion.setApplicableProduct(dto.getApplicableProduct());
        promotion.setPaymentMethodType(dto.getPaymentMethodType());
        return promotion;
    }
}
