package com.lucasangelo.todosimple.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer { //implementaçao web do spring

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");// tudo depois da barra sera aceito como parametro


        //config + completa para produção

        //registry.addMapping("/**")
                //.allowedOrigins("http://localhost:3000")  // somente essa origem pode acessar
                //.allowedMethods("GET", "POST", "PUT", "DELETE")  // métodos permitidos
                //.allowedHeaders("*")  // cabeçalhos permitidos
               //.allowCredentials(true);  // permite envio de cookies/autenticação
    }
}
