package com.spectrum.login.config;

// import lombok.RequiredArgsConstructor;

// import java.util.Arrays;

// import org.apache.catalina.filters.CorsFilter;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import com.spectrum.login.model.Role;
// import com.spectrum.login.service.IUserService;

// @Configuration
// @EnableWebSecurity
// @RequiredArgsConstructor
// public class SecurityConfiguration {
//     private final JwtAuthenticationFilter jwtAuthenticationFilter;
//     private final IUserService userService;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.csrf(AbstractHttpConfigurer::disable)
//                 .authorizeHttpRequests(request -> request
//                         .requestMatchers("/api/v1/auth/**").permitAll()
//                         .requestMatchers("/api/v1/admin","/question/upload").hasAuthority(Role.ADMIN.name())
//                         .requestMatchers("/api/v1/user","/getEmployee","/leaveApplications/GetAllRequest/**").hasAuthority(Role.USER.name())
//                         .anyRequest().authenticated())
//                 .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .authenticationProvider(authenticationProvider())
//                 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//         return http.build();
//     }

//     @Bean
//     public AuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//         authenticationProvider.setUserDetailsService(userService.userDetailsService());
//         authenticationProvider.setPasswordEncoder(passwordEncoder());
//         return authenticationProvider;
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//         return config.getAuthenticationManager();
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();
//         configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//         configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//         configuration.setAllowCredentials(true);
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", configuration);
//         return source;
//     }
// }

import lombok.RequiredArgsConstructor;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.spectrum.login.model.Role;
import com.spectrum.login.service.IUserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final IUserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors().and()
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/**", "/employees/**", "/api/**", "/education/**",
                                "/BankDetails/**", "/api/employeement-history/**", "/api/master/**", "/api/master1/**",
                                "/api/masternew/**", "/recruitment/**", "/Asset/**", "/undertaking/**",
                                 "CreateNewAccount/**", "/CompanyRegistartion/**","/register","/documents/**")
                        .permitAll()
                        .requestMatchers("/api/v1/admin", "/question/upload", "/getEmployee",
                                "/leaveApplications/GetAllRequest/**",
                                "/api/workflow/**", "/leaveApplications/**", "/AccountMaster/**","/timesheetmain",
                                "/CompanyRegistartion/**", "/employees/**", "/api/expense/**", "/api/leaveApproval/**",
                                "/api/schedules/**", "/project/**", "/recruitment/**", "/Asset/**", "/undertaking/**")
                        .hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/user", "/getEmployee", "/leaveApplications/GetAllRequest/**",
                                "/api/workflow/**", "/leaveApplications/**", "/AccountMaster/**","/timesheetmain",
                                "/CompanyRegistartion/**", "/employees/**", "/api/schedules/**",
                                "/api/leaveApproval/**","/project/**")
                        .hasAuthority(Role.USER.name())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}