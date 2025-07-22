package com.lucasangelo.todosimple.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration//indica que essa é uma classe de configuração do Spring
@EnableWebSecurity//ativa a segurança da web com Spring Security.
@EnableGlobalMethodSecurity(prePostEnabled = true)//permite usar anotações como @PreAuthorize e @PostAuthorize nos métodos para controlar acesso por regras.
public class SecurityConfig {

    private static final String[] PUBLIC_MATCHERS = {
            "/"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/user",
            "/login"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {//requisicao http

        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()//permite POST sem autenticação em /user e /login.
                .antMatchers(PUBLIC_MATCHERS).permitAll()//Permite qualquer métoodo em /.
                .anyRequest().authenticated();//Qualquer outra requisição precisa estar autenticada
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//impossivel salvar sessão

        return http.build();
    };

    @Bean// métod cria um objeto que deve ser gerenciado pelo Spring (ou seja, uma dependência injetável).
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {//criptografa a senha no banco e retorna quando o usuario efetuar login para comparaçãp
        return new BCryptPasswordEncoder();
    }
}
