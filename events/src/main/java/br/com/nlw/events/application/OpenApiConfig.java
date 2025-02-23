package br.com.nlw.events.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API de Eventos")
            .description("Cadastro de eventos, inscrição de usuários e indicação para outros usuários")
            .contact(new Contact().name("LinkedIn").url("https://www.linkedin.com/in/dev-dmiguelsm"))
            .version("1.0.0"));
  }
}
