package com.mindtree.authorization.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
@PropertySource("classpath:application.properties")
public class AuthorizationServConfiguration extends AuthorizationServerConfigurerAdapter {
	
	@Value("${client.name}")
	private String clientName;

	@Value("${client.secret}")
	private String clientSecret;

	@Value("${client.accesstokenvalidity}")
	private int clientAccessToken;

	@Value("${client.refreshtokenvalidity}")
	private int clientRefreshToken;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void configure(ClientDetailsServiceConfigurer client) throws Exception {
		client.inMemory().withClient(clientName).authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
				.authorizedGrantTypes("password", "refresh_token", "authorization_code", "implicit")
				.scopes("read", "write", "trust").secret(passwordEncoder.encode(clientSecret))
				.accessTokenValiditySeconds(clientAccessToken).refreshTokenValiditySeconds(clientRefreshToken);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.checkTokenAccess("isAuthenticated()");
	}

}
