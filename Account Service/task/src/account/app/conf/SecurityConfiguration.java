package account.app.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers(HttpMethod.POST,"/api/auth/singup").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyAuthority("ROLE_USER","ROLE_ACCOUNTANT", "ROLE_ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyAuthority("ROLE_USER","ROLE_ACCOUNTANT")
                .antMatchers(HttpMethod.POST, "/api/acct/payments").hasAuthority("ROLE_ACCOUNTANT")
                .antMatchers(HttpMethod.PUT, "/api/acct/payments").hasAuthority("ROLE_ACCOUNTANT")
                .antMatchers(HttpMethod.GET, "/api/admin/user").hasAuthority("ROLE_ADMINISTRATOR")
                .antMatchers(HttpMethod.DELETE, "/api/admin/user").hasAuthority("ROLE_ADMINISTRATOR")
                .antMatchers(HttpMethod.PUT, "/api/admin/user/role").hasAuthority("ROLE_ADMINISTRATOR")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

}
