package project.bayaraja.application.security;

import io.swagger.v3.oas.annotations.tags.Tags;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SwaggerConfiguration {

    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("APIS")
                .pathsToMatch("/**")
                .build();
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.COOKIE)
                .name("JSESSIONID");
    }

    private Server createServer(){
        Server server = new Server();

        server.setUrl("http://localhost:8080");
        server.setDescription("Local or Development server");

        return server;
    }

    private List<Tag> createTags() {
        List<Tag> tags = new ArrayList<>();

        tags.add(0, new Tag().name("User").description("User Management"));
        tags.add(1, new Tag().name("Student").description("Student Management"));
        tags.add(2, new Tag().name("Payments").description("Payments Management"));
        tags.add(3, new Tag().name("Invoices").description("Invoices Management"));
        tags.add(4, new Tag().name("SPP").description("SPP Management"));
        tags.add(5, new Tag().name("Auth").description("Authentication Management"));

        return tags;
    }

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Bayar Aja API")
                                .version("1.0.0")
                                .description("Bayar Aja is application for administration")
                                .contact(
                                        new Contact()
                                                .name("Ziaurrahman Athaya")
                                                .email("jiaathaya@gmail.com")
                                )
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("cookieAuth")
                )
                .components(
                        new Components()
                                .addSecuritySchemes("session", createSecurityScheme())
                )
                .servers(List.of(createServer()))
                .tags(createTags());
    }

}
