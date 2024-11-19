package med.voll.vollmed_web.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracoesSeguranca {

    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(req -> {
            req.requestMatchers("/css/**", "/js/**", "/assets/**").permitAll();
            req.requestMatchers("/pacientes/**").hasRole("ATENDENTE");
            req.requestMatchers(HttpMethod.GET, "/medicos").hasAnyRole("ATENDENTE", "PACIENTE");
            req.requestMatchers("/medicos/**").hasRole("ATENDENTE");
            req.anyRequest().authenticated();
        }).formLogin(form -> form.loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe.key("lembraDeMim")
                        .alwaysRemember(true)
                ).csrf(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder codificadorSenha() {
        return new BCryptPasswordEncoder();
    }
}
