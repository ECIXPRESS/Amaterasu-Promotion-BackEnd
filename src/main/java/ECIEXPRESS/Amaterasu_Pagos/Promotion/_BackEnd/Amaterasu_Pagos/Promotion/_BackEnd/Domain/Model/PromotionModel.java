package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Domain.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "promotions")
public class PromotionModel {
    @Id
    private String id;
    private String code;
    private String name;
    private String description;
    private PromotionType type;
    private BigDecimal discountValue;
    private BigDecimal minimumPurchaseAmount;
    private Integer usageLimit;
    private Integer currentUsage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
    private String applicableProduct;
    private String paymentMethodType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum PromotionType {
        PERCENTAGE,
        FIXED_AMOUNT,
        BUY_ONE_GET_ONE,
        FREE_SHIPPING
    }

    public PromotionModel() {
        this.currentUsage = 0;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PromotionModel(String code, String name, String description, PromotionType type,
                          BigDecimal discountValue, LocalDateTime startDate, LocalDateTime endDate) {
        this();
        this.code = code;
        this.name = name;
        this.description = description;
        this.type = type;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return isActive &&
                now.isAfter(startDate) &&
                now.isBefore(endDate) &&
                (usageLimit == null || currentUsage < usageLimit);
    }

    public boolean canApply(BigDecimal purchaseAmount, String product, String paymentMethod) {
        if (!isValid()) return false;
        if (applicableProduct != null && !applicableProduct.equals(product)) {
            return false;
        }
        if (paymentMethodType != null && !paymentMethodType.equals(paymentMethod)) {
            return false;
        }
        if (minimumPurchaseAmount != null && purchaseAmount.compareTo(minimumPurchaseAmount) < 0) {
            return false;
        }
        return true;
    }

    public BigDecimal calculateDiscount(BigDecimal originalAmount) {
        if (!isValid()) return BigDecimal.ZERO;
        switch (type) {
            case PERCENTAGE:
                return originalAmount.multiply(discountValue.divide(BigDecimal.valueOf(100)));
            case FIXED_AMOUNT:
                return discountValue.min(originalAmount);
            case BUY_ONE_GET_ONE:
                return originalAmount.divide(BigDecimal.valueOf(2));
            case FREE_SHIPPING:
                return BigDecimal.ZERO;
            default:
                return BigDecimal.ZERO;
        }
    }

    public void incrementUsage() {
        if (currentUsage < usageLimit) {
            currentUsage++;
            updatedAt = LocalDateTime.now();
        }
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public PromotionType getType() { return type; }
    public void setType(PromotionType type) { this.type = type; }

    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    public BigDecimal getMinimumPurchaseAmount() { return minimumPurchaseAmount; }
    public void setMinimumPurchaseAmount(BigDecimal minimumPurchaseAmount) { this.minimumPurchaseAmount = minimumPurchaseAmount; }

    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }

    public Integer getCurrentUsage() { return currentUsage; }
    public void setCurrentUsage(Integer currentUsage) { this.currentUsage = currentUsage; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
        this.updatedAt = LocalDateTime.now();
    }

    public String getApplicableProduct() { return applicableProduct; }
    public void setApplicableProduct(String applicableProduct) { this.applicableProduct = applicableProduct; }

    public String getPaymentMethodType() { return paymentMethodType; }
    public void setPaymentMethodType(String paymentMethodType) { this.paymentMethodType = paymentMethodType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
