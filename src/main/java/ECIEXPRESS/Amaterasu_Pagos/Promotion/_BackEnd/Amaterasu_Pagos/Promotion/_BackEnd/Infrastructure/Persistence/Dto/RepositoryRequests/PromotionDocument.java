package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Persistence.Dto.RepositoryRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Promotions-db")
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDocument {
    @Id
    private String promotionId;
    @Field("productId")
    private String productId;
    @Field("isActive")
    private boolean isActive;
    @Field("endDate")
    private String endDate;
    @Field("startDate")
    private String startDate;
    @Field("promotionMultiplier")
    private double promotionMultiplier;
}
