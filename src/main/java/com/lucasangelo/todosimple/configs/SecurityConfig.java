package com.lucasangelo.todosimple.configs;

import com.lucasangelo.todosimple.security.JWTAuthenticationFilter;
import com.lucasangelo.todosimple.security.JWTAuthorizationFilter;
import com.lucasangelo.todosimple.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

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

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        this.authenticationManager = authenticationManagerBuilder.build();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()//permite POST sem autenticação em /user e /login.
                .antMatchers(PUBLIC_MATCHERS).permitAll()//Permite qualquer métoodo em /.
                .anyRequest().authenticated().and()
                .authenticationManager(authenticationManager);//Qualquer outra requisição precisa estar autenticada
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//impossivel salvar sessão

        http.addFilter(new JWTAuthenticationFilter(this.authenticationManager, jwtUtil));
        http.addFilter(new JWTAuthorizationFilter(this.authenticationManager, this.jwtUtil, this.userDetailsService));

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
