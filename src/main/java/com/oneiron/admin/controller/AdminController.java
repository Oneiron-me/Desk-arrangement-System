package com.oneiron.admin.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oneiron.admin.doc.AdminVo;
import com.oneiron.admin.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Resource(name="adminServiceImpl")
	AdminService adminServiceImpl;

	/**
	 * 
	 * @return GO TO ADMIN PAGE
	 */
	@RequestMapping("/home")
	public String admin() {
		return "views/admin/admin";
	}
	
	/**
	 * @return ADMIN 관리 페이지에서 관리자 정보들 가져오는곳.
	 */
	@RequestMapping("/getAdminInfo")
	public @ResponseBody Map<String, Object> getAdminInfo() {
		return adminServiceImpl.getAdminInfoByN1ql();
	}
	
	/**
	 * 
	 * @return MYBATIS 연동했음... 근데 잘안쓸거같다
	 * @throws SQLException
	 */
	@RequestMapping("/getAdminInfoTest")
	public @ResponseBody AdminVo getAdminInfoTest() throws SQLException {
//		String connectionString = "jdbc:couchbase:User='oneiron';Password='1q2w3e4r';Server='http://hancheol.dlinkddns.com';N1QLPort=8093;";
//		 
//		Connection conn = DriverManager.getConnection(connectionString);
//		DatabaseMetaData table_meta = conn.getMetaData();
//		ResultSet rs = table_meta.getColumns(null, null, "oneiron", null);
//		while(rs.next()){
//		  System.out.println("바보"+ rs.getString("COLUMN_NAME"));
//		}
		return adminServiceImpl.getAdminInfoTest();
	}
	
	/**
	 * 
	 * @param doc
	 * @return 관리자 개인정보 저장함.
	 */
	@RequestMapping("/putAdminInfo")
	public @ResponseBody boolean putAdminInfo(@RequestBody Map<String, Object> map, HttpServletRequest reqest) {
		return adminServiceImpl.putAdminInfo(map);
	}
	
	/**
	 * 
	 * @param doc
	 * @return 관리자 플젝 정보 저장함
	 */
	@RequestMapping("/putAdminProjectInfo")
	public @ResponseBody boolean putAdminProjectInfo(@RequestBody Map<String, Object> map) {
		return adminServiceImpl.putAdminProjectInfo(map);
	}
	
}
