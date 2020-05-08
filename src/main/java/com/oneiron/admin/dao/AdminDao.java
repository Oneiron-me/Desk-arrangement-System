package com.oneiron.admin.dao;

import java.util.Map;

import com.oneiron.admin.doc.AdminVo;

public interface AdminDao {

	public AdminVo getAdminInfo(Map<String, Object> map);
}
