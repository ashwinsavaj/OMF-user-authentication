package com.mindtree.authorization.oauth2.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.mindtree.authorization.handler.CustomUrlAuthenticationSuccessHandler;
import com.mindtree.authorization.service.CustomUserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomUserServiceImpl customUserDetail;

	@Autowired
	private DataSource dataSource;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	private void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetail).passwordEncoder(encoder());
	}

	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()/* .anonymous().disable(). */.authorizeRequests()
				.antMatchers("/oauth/token", "/oauth/check_token", "/register", "/login**", "/", "/callback","/v2/**", "/swagger-resources/**", "/csrf*", "/user/getById/**",
						"/user/newUser", "/user/newSocial", "/user/getUserById/**", "/userservice/user/newSocial",
						"/user/address/**", "/swagger**")
				.permitAll().anyRequest().authenticated().and().oauth2Login()
				.successHandler(myAuthenticationSuccessHandler());
	}

	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new CustomUrlAuthenticationSuccessHandler();
	}
}