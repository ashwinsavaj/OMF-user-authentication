package com.mindtree.authorization.social.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SocialLoginController {

	protected Log LOGGER = LogFactory.getLog(this.getClass());
	
	Authentication authentication ;

	@RequestMapping(value = "/callback",method=RequestMethod.GET)
	public ModelAndView  callback() {
	
		try {

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession();
			LOGGER.debug("Context is" + session.getAttribute("SPRING_SECURITY_CONTEXT").toString());
			SecurityContext context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
			SecurityContextHolder.setContext(context);
			authentication = context.getAuthentication();
		
			LOGGER.debug("User information from getting google oauth :" + authentication.getPrincipal()
					+ "\n \n User Session ID and Authorities Details :" + authentication.getAuthorities());
		//	model.addObject("oauth_token", authentication.getDetails());

		} catch (Exception e) {
			LOGGER.error("Something went wrong in setting the security context.Refer logs for more information");
			e.printStackTrace();
		}

		return new ModelAndView("redirect:" + "http://ubuntu-omf.southindia.cloudapp.azure.com:9095/home/"+authentication.getDetails());
	}
}
