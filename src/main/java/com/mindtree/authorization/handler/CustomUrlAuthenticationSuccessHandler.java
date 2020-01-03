package com.mindtree.authorization.handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.mindtree.authorization.dao.impl.AuthorizationDaoImpl;
import com.mindtree.authorization.entity.PrincipalData;
import com.mindtree.authorization.entity.User;
import com.mindtree.authorization.service.UserServiceProxy;

import feign.RetryableException;

@PropertySource("classpath:application.properties")
public class CustomUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	protected Log LOGGER = LogFactory.getLog(this.getClass());

	@Autowired
	private AuthorizationServerEndpointsConfiguration configuration;

	PrincipalData principalData;

	@Autowired
	private AuthorizationDaoImpl authorizationDaoImpl;

	@Value("${client.name}")
	private String clientName;

	@Value("${client.authority}")
	private String clientAuthority;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		handle(request, response, authentication);
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		String targetUrl = determineTargetUrl(authentication, request);

		if (response.isCommitted()) {
			LOGGER.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	public String determineTargetUrl(Authentication authentication, HttpServletRequest request) {

		try {
			Map<String, String> requestParameters = new HashMap<String, String>();
			Map<String, Serializable> extensionProperties = new HashMap<String, Serializable>();
			List<String> scopes = new ArrayList<String>();
			Set<String> responseTypes = new HashSet<String>();
			responseTypes.add("code");
			scopes.add("read");

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

			authorities.add(new SimpleGrantedAuthority(clientAuthority));

			OAuth2Request oauth2Request = new OAuth2Request(requestParameters, clientName, authorities, true,
					new HashSet<String>(scopes), new HashSet<String>(Arrays.asList("OMF_AuthorizationService")), "/",
					responseTypes, extensionProperties);
			PrincipalData principalInfo = new PrincipalData();
			BeanUtils.copyProperties(authentication.getPrincipal(), principalInfo);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					principalInfo.getEmail(), "N/A", authorities);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			OAuth2Authentication authenticationRequest = new OAuth2Authentication(oauth2Request, authenticationToken);
			AuthorizationServerTokenServices tokenService = configuration.getEndpointsConfigurer().getTokenServices();
			OAuth2AccessToken token = tokenService.createAccessToken(authenticationRequest);

			principalInfo.setEmail(authenticationToken.getName());
			principalInfo.setToken(token.toString());
			authorizationDaoImpl.register(principalInfo);
			User user = new User();
			user.setUserName(authenticationToken.getName());
			authorizationDaoImpl.socialRegisterUser(user);

			authenticationRequest.setDetails(token);
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(authenticationRequest);
			HttpSession session = request.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", context);
			LOGGER.debug(
					"Previous session id is :" + session.getId() + "\n \n Generating token is :" + token.toString());
		} catch (Exception e) {
			LOGGER.error("Something went wrong in setting the security context.Refer logs for more information");
			e.printStackTrace();
		}

		return "/callback";
	}

}