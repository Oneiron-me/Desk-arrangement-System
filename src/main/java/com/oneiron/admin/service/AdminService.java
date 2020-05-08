package com.oneiron.admin.service;

import java.util.Map;

import com.oneiron.admin.doc.AdminVo;

public interface AdminService {

	/**
	 * @return ADMIN INFO 가져옵니다.
	 */
	public Map<String, Object> getAdminInfoByN1ql();
	
	public AdminVo getAdminInfoTest();
	
	/**
	 * 
	 * @param doc
	 * @return 관리자 개인정보 저장함
	 */
	public boolean putAdminInfo(Map<String, Object> map);
	
	/**
	 * 
	 * @param doc
	 * @return 관리자 플젝정보 저장함
	 */
	public boolean putAdminProjectInfo(Map<String, Object> map);
}
