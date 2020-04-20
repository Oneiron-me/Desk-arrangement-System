package com.oneiron.service;

import com.oneiron.login.doc.UserDoc;

public interface UserRepositoryService{
	
	/**
	 * 저장할때씀~!
	 * @param doc
	 * 
	 */
	public int save(UserDoc doc);
	
	/**
	 * 삭제~!
	 * @param doc
	 */
	public int delete(UserDoc doc);
	
	/**
	 * 아이디로 정보가져오기
	 * @param id
	 */
	public UserDoc findUserById(String id);
	
	/**
	 * 
	 * @param id
	 * @return 쿼리짜서가져옴...
	 */
	public UserDoc findUserByIdN1ql(String id);
	
	/**
	 * 
	 * @param id
	 * @return 구글로그인 정보는 따로 쿼리짜서가져옴...
	 */
	public UserDoc findGoogleUserByIdN1ql(String id);
	
}
