package gov.nic.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import gov.nic.filter.ActuatorKeyFilter;
import gov.nic.filter.ApiKeyFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final ApiKeyFilter apiKeyFilter;
	
    private final ActuatorKeyFilter actuatorKeyFilter;

    public SecurityConfig(ApiKeyFilter apiKeyFilter,ActuatorKeyFilter actuatorKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
        this.actuatorKeyFilter = actuatorKeyFilter;
    }
	
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/ChatBot/api/**")
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .addFilterBefore(apiKeyFilter, SecurityContextHolderFilter.class); 

        return http.build();
    }

    @Bean
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/ChatBot/actuator/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ChatBot/actuator/health", "/ChatBot/actuator/info","/ChatBot/actuator/logfile").permitAll()
                .anyRequest().hasRole("ADMIN")
            )
            .addFilterBefore(actuatorKeyFilter, SecurityContextHolderFilter.class)
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());

        return http.build();
    }   


}
