package com.oneiron.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionInfo {

	private static Map<String, String> userInfoMap;
	
	public static SessionInfo getInstance() {
		userInfoMap = new ConcurrentHashMap<String, String>();
		return LazyHolder.INSTANCE;
	}

	private static class LazyHolder {
		private static final SessionInfo INSTANCE = new SessionInfo();
	}
	
	
	public void put(String session, String userId) {
		userInfoMap.put(session, userId);
	}
	
	public String get(String session) {
		return userInfoMap.get(session);
	}
	
	public void remove(String session) {
		userInfoMap.remove(session);
	}
	
	public List<String> getUserList(){
		List<String> list = new ArrayList<>();
		userInfoMap.keySet().forEach(session -> list.add(get(session)));
		
		return list;
	}
	
	public Map<String,String> getUserMap(){
		return userInfoMap;
	}
	
	public int getSessionCount() {
		return userInfoMap.size();
	}
	
}
