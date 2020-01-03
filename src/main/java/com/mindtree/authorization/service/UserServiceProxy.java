package com.mindtree.authorization.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mindtree.authorization.entity.User;
import com.mindtree.authorization.social.response.entity.Response;

@FeignClient(name = "OMF-userService")
@RibbonClient(name ="OMF-userService")
@RequestMapping("/user")
@Service
public interface UserServiceProxy {

	@GetMapping("/getById/{userName}")
	public Optional<User> getUserByIdAdmin(@PathVariable(value = "userName") String userName);
	
	@PostMapping("/newSocial")
	public Response newSocail(@Valid @RequestBody User user);

}
