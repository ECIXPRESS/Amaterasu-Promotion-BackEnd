package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web;

import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Application.Ports.PromotionUseCases;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.ApplyPromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.CreatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionRequests.UpdatePromotionRequest;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.ApplyPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.GetPromotionResponse;
import ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Infrastructure.Web.Dto.PromotionResponses.UpdatePromotionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
@Tag(name = "Gestión de Promociones", description = "API para gestión de promociones en ECIEXPRESS")
public class PromotionController {

    private final PromotionUseCases promotionUseCases;

    @GetMapping
    @Operation(summary = "Obtiene todas las promociones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promociones obtenidas exitosamente")
    })
    public ResponseEntity<List<GetPromotionResponse>> getAllPromotions() {
        return ResponseEntity.ok(promotionUseCases.getAllPromotions());
    }

    @GetMapping("/{promotionId}")
    @Operation(summary = "Obtiene una promoción por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoción encontrada"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    public ResponseEntity<GetPromotionResponse> getPromotionById(
            @Parameter(description = "ID de la promoción") @PathVariable String promotionId) {
        return ResponseEntity.ok(promotionUseCases.getPromotionById(promotionId));
    }

    @PostMapping
    @Operation(summary = "Crea una nueva promoción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promoción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos / validación fallida"),
            @ApiResponse(responseCode = "409", description = "Ya existe una promoción activa para el producto")
    })
    public ResponseEntity<Void> createPromotion(@Valid @RequestBody CreatePromotionRequest request) {
        promotionUseCases.createPromotion(request);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{promotionId}")
    @Operation(summary = "Actualiza una promoción existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoción actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos / validación fallida"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    public ResponseEntity<UpdatePromotionResponse> updatePromotion(
            @Parameter(description = "ID de la promoción") @PathVariable String promotionId,
            @Valid @RequestBody UpdatePromotionRequest request) {

        UpdatePromotionRequest fixedRequest = new UpdatePromotionRequest(
                promotionId,
                request.endDate(),
                request.startDate(),
                request.promotionMultiplier()
        );

        return ResponseEntity.ok(promotionUseCases.updatePromotion(fixedRequest));
    }

    @PostMapping("/apply")
    @Operation(summary = "Aplica promociones a un pedido y retorna el total con descuentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promociones aplicadas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos / pedido inválido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<ApplyPromotionResponse> applyPromotion(@Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionUseCases.applyPromotion(request));
    }

    @DeleteMapping("/{promotionId}")
    @Operation(summary = "Elimina una promoción por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Promoción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    public ResponseEntity<Void> deletePromotion(
            @Parameter(description = "ID de la promoción") @PathVariable String promotionId) {
        promotionUseCases.deletePromotion(promotionId);
        return ResponseEntity.noContent().build();
    }
}
