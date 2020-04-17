package com.oneiron.common.service;

import java.util.List;
import java.util.Map;

import com.couchbase.client.java.query.N1qlQueryResult;

public interface CommonService {

	/**
	 * 
	 * @param query 쿼리
	 * @param param 파라미터
	 * @return List
	 */
	List<Map<String, Object>> findByN1qlProjection(String query, String param);
	
	/**
	 * 
	 * @param query 쿼리
	 * @param param 파라미터
	 * @return N1qlQueryResult
	 */
	N1qlQueryResult queryN1QL(String query, String param);
}
