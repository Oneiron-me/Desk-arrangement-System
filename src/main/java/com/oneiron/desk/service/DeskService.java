package com.oneiron.desk.service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface DeskService {

	/**
	 * 
	 * @param map
	 * @param accessor
	 * @return 생성된 desk 안에 note 쑤셔넣음 ㅋㅋ
	 */
	public Map<String, Object> putNote(Map<String, Object> map, SimpMessageHeaderAccessor accessor);
	
	/**
	 * 
	 * @param map
	 * @return 노트 리스트를 가져옵니다~!
	 */
	public Map<String, Object> getNoteList(Map<String, Object> map);
	
	/**
	 * 
	 * @param map
	 * @return 초대할 유저 목록을 가져옵니다. HashMap을 extends 한 모든 객체를 뱉어줍니다.
	 */
	public List<Map<String, Object>> getInviteUserList(Map<String, Object> map);
}
