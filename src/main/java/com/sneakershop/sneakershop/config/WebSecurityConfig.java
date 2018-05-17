package com.sneakershop.sneakershop.config;

import com.sneakershop.sneakershop.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//Класс для работы с юзерами(авторизация и управления ролями)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsServiceImp userDetailsServiceImp;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() //csrf выключаем
                .authorizeRequests()
                .antMatchers("/**","/static/**").permitAll().anyRequest().permitAll() //любой тип запроса может получить путь к ("/**", "/static/**")
                .and()
                .formLogin().loginPage("/login") //страница входа
                .permitAll() //доступна всем
                .successForwardUrl("/") //после успешного входа переход на "/"
                .loginProcessingUrl("/login").usernameParameter("name").passwordParameter("password") //какие поля ключевые у формы входа
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/"); //выход по ссылке /logout

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(userDetailsServiceImp).passwordEncoder(passwordEncoder); //передаем объект который знает как создавать юзера + устанавливаем что пароль будет зашифрован
    }

}
