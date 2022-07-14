package com.company.config;

import com.company.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**"
    };


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Authorization
        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/auth", "/auth/*").permitAll()
          //      .antMatchers("/swagger-ui", "/swagger-ui/**").permitAll()
                .antMatchers("/factory/public", "/factory/public/*").permitAll()
                .antMatchers("/attach/public","/attach/open", "/attach/download").permitAll()
                .antMatchers("/attach/adm", "/attach/adm/*").hasAnyRole("ADMIN")
                .antMatchers("/factory/adm", "/factory/adm/*").hasAnyRole("ADMIN")
                .antMatchers("/product/adm", "/product/adm/*").hasAnyRole("ADMIN")
                .antMatchers("/sale/adm", "/sale/adm/*").hasAnyRole("ADMIN")
                .antMatchers( "/sale/adm/**").hasAnyRole("ADMIN")
                .antMatchers("/product/emp", "/product/emp/*").hasAnyRole("EMPLOYEE","ADMIN")
                .antMatchers("/sale/emp", "/sale/emp/*","/sale/emp/**").hasAnyRole("EMPLOYEE","ADMIN")
                .antMatchers("/attach/product", "/attach/upload/product").hasAnyRole("EMPLOYEE", "ADMIN")
                .antMatchers("/product/product", "/product/upload/product").hasAnyRole("EMPLOYEE", "ADMIN")
                .antMatchers("/factory/list").hasAnyRole("EMPLOYEE", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //.httpBasic();
        http.cors().disable().csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                String md5 = MD5Util.getMd5(rawPassword.toString());
                return md5.equals(encodedPassword);
            }
        };

    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
