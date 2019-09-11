package com.config;

import com.auth.handler.LoginSuccesHandler;
import com.models.service.impl.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;


@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccesHandler succesHandler;

//    @Autowired
//    private DataSource dataSource;

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {

        build.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

       /* build.jdbcAuthentication()
             .dataSource(dataSource)
             .passwordEncoder(passwordEncoder)
             .usersByUsernameQuery("select username, password, enabled from users where username=?")
             .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");
       */

        /*
        // Deprecated
        // UserBuilder users = User.withDefaultPasswordEncoder();

        PasswordEncoder encoder = passwordEncoder();
        User.UserBuilder users = User.builder().passwordEncoder(encoder::encode);

        builder.inMemoryAuthentication()
                .withUser(users.username("admin").password("12345").roles("ADMIN","USER"))
                .withUser(users.username("isaias").password("12345").roles("USER"));  */
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/","/css/**","/js/**","/img/**","/listar").permitAll()
           /* .antMatchers("/ver/**").hasAnyRole("USER")
            .antMatchers("/uploads/**").hasAnyRole("USER")
            .antMatchers("/form/**").hasAnyRole("ADMIN")
            .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
            .antMatchers("/factura/**").hasAnyRole("ADMIN") */

            .anyRequest().authenticated()
            .and()
                .formLogin()
                .successHandler(succesHandler)
                .loginPage("/login")
                .permitAll()
            .and()
            .logout()
                .permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/error_403")
        ;
    }


}
