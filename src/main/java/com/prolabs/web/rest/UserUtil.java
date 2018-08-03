package com.prolabs.web.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class UserUtil {
	
	public static String getLogedInUser() {
		String user = "";
		try{
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			System.out.println(authentication.getCredentials()+" "+authentication.getName() );
			User pricipal = (User) authentication.getPrincipal();
			user = pricipal.getUsername();
		}catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}

}
