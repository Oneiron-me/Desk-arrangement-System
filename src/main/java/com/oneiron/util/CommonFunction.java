package com.oneiron.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.oneiron.login.doc.UserDoc;

@Service
public class CommonFunction {

	public String userId() {
		UserDoc doc = (UserDoc)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return doc.getId();
	}
	
	public String userName() {
		UserDoc doc = (UserDoc)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return doc.getUsername();
	}
	
	public String today() {
		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(today);
	}
}
