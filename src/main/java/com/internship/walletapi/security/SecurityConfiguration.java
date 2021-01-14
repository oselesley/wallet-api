package com.internship.walletapi.security;


import com.internship.walletapi.filters.JWTRequestFilter;
import com.internship.walletapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;


@Order(3)
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private JWTRequestFilter jwtRequestFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
               .csrf().disable()
               .authorizeRequests()
               .antMatchers("/auth/", "/h2/**").permitAll()

               .antMatchers(HttpMethod.POST, "/api/v1/wallets/**").hasAnyRole("USER", "ADMIN")
               .antMatchers(HttpMethod.GET,"/api/v1/wallets/**").hasAnyRole("USER", "ADMIN")
               .antMatchers(HttpMethod.GET, "/api/v1/transactions/approve/**").hasRole("ADMIN")
               .anyRequest()
               .authenticated();

       http
               .csrf()
               .disable();
       http
               .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
       http
               .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/h2/**")
                .antMatchers("/auth/**")
        .antMatchers("/swagger/**", "/v3/api-docs/**");
    }

    @Order(5)
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

}
