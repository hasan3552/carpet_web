package com.company.config;


//@Configuration
//@EnableWebSecurity
//public class SpringConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // Authentication
//        auth.inMemoryAuthentication()
//                .withUser("Ali").password("{bcrypt}$2a$10$V93CWoH3NxAPC7VzPd9ouuU8PWvZWYdoo94H3HOZ8kFSkBAvYssEe").roles("ADMIN")
//                .and()
//                .withUser("Vali").password("{noop}valish123").roles("USER");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // Authorization
//        http.authorizeRequests()
//                .antMatchers("/home", "/home/*").permitAll()
//                .antMatchers("/article/public/*").permitAll()
//                .antMatchers("/article/adm/*").hasAnyRole("PROFILE", "ADMIN")
//                .antMatchers("/profile", "/profile/*").hasAnyRole("PROFILE", "ADMIN")
//                .antMatchers("/admin", "/admin/*").hasRole("ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .and()
//                .httpBasic();
//    }
//}
