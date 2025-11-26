package ECIEXPRESS.Amaterasu_Pagos.Promotion._BackEnd.Amaterasu_Pagos.Promotion._BackEnd.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para el microservicio de Promociones.
 * Proporciona documentación interactiva de la API REST.
 *
 * @author Equipo Amaterasu
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configuración personalizada de OpenAPI para el servicio de promociones.
     *
     * @return Configuración OpenAPI con información del servicio
     */
    @Bean
    public OpenAPI promotionsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Promotions Service API - ECIEXPRESS")
                        .description("API REST para la gestión de promociones y descuentos en el sistema ECIEXPRESS. "
                                + "Este microservicio permite crear, actualizar, consultar y desactivar promociones "
                                + "asociadas a productos, pedidos o usuarios."))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desarrollo Local")
                ));
    }
}
