package NestNet.NestNetWebSite.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){

        Info info = new Info()
                .title("네스트넷 동아리 웹사이트 API Doc")
                .version("1.0.0")
                .description("CBNU 학술동아리 네스트넷 웹사이트 프로젝트의 API 명세서");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
