package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.Erros;

import java.time.Instant;

public record ApiError(
        String code,
        String message,
        Instant timestamp,
        String path
) {}
