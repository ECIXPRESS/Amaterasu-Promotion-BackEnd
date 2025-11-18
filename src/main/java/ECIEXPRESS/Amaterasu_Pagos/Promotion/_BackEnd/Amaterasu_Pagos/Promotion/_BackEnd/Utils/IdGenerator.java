package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Utils;

import java.util.UUID;

public class IdGenerator {

    public static String generatePromotionCode() {
        return "PROMO_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
