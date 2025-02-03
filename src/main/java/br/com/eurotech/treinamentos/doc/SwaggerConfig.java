package br.com.eurotech.treinamentos.doc;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Component
public class SwaggerConfig  implements OpenApiCustomizer{

    @Override
    public void customise(OpenAPI openApi) {
        var securitySchemeName = "bearerAuth";
        openApi.getComponents().addSecuritySchemes(securitySchemeName, new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
        openApi.addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }

    
}