package com.epam.smartcityhousingsystem.configuration;

import com.epam.smartcityhousingsystem.configuration.constants.SwaggerConstants;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

/**
 *Configuration for swagger
 *
 * @author Jakhongir Rasulov on 10/31/21
 * @project smartcity-housing-system
 */


@Import({BeanValidatorPluginsConfiguration.class, SpringDataRestConfiguration.class})
@Configuration
public class SwaggerConfiguration {

    private ApiKey apiKey(){
        return new ApiKey(SwaggerConstants.SECURITY_REFERENCE, HttpHeaders.AUTHORIZATION, SecuritySchemeIn.HEADER.toString());
    }

    private SecurityContext securityContext(){
        return SecurityContext.builder().securityReferences(securityReference()).build();
    }

    private List<SecurityReference> securityReference(){
       AuthorizationScope[] authorizationScopes = { new AuthorizationScope(SwaggerConstants.AUTHORIZATION_SCOPE, SwaggerConstants.AUTHORIZATION_DESCRIPTION) };
       return Collections.singletonList(new SecurityReference(SwaggerConstants.SECURITY_REFERENCE, authorizationScopes));
    }

    @Bean
    public Docket docket(){

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .forCodeGeneration(true)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.regex(SwaggerConstants.SECURE_PATH))
                .build()
                .tags(new Tag(SwaggerConstants.API_TAG, "All API's relating to Housing system"))
                ;
    }

    private ApiInfo createApiInfo(){
        return new ApiInfo(
                SwaggerConstants.API_TITLE,
                SwaggerConstants.API_DESCRIPTION,
                SwaggerConstants.API_VERSION,
                SwaggerConstants.TERMS_OF_SERVICE,
                createContact(),
                SwaggerConstants.LICENCE,
                SwaggerConstants.LICENCE_URL,
                Collections.emptyList()
        );
    }

    private Contact createContact(){
        return new Contact(
                SwaggerConstants.CONTACT_NAME,
                SwaggerConstants.CONTACT_URL,
                SwaggerConstants.CONTACT_EMAIL);
    }

}
