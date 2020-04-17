package com.oneiron.intro.service;

import java.util.List;
import java.util.Map;

import com.oneiron.intro.doc.DeskDoc;

public interface IntroService {

	/**
	 * 
	 * @param doc
	 * @return desk insert 후 성공여부 받아옴. 0:실패 1:성공
	 */
	public int deskSave(DeskDoc doc);
	
	/**
	 * 
	 * @param map : PARAMETER
	 * @return DESK 생성후 반환값 BOOLEAN으로 뱉어냅니다
	 */
	public boolean createDesk(Map<String, Object> map);
	
	/**
	 * 
	 * @return desk list를 불러옵니다
	 */
	public List<Map<String, Object>> getDeskList();
}
