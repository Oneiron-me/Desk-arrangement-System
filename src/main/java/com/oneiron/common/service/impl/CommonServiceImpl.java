package com.oneiron.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.oneiron.common.service.CommonService;
import com.oneiron.util.SuperHashMap;

@Service
public class CommonServiceImpl implements CommonService {
	
final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier(BeanNames.COUCHBASE_TEMPLATE)
	private CouchbaseTemplate defaultTemplate;

	@Override
	public List<Map<String, Object>> findByN1qlProjection(String query, String param) {
		
//		JsonObject placeholderValues = JsonObject.fromJson(param);
//		N1qlParams params = N1qlParams.build().pretty(false);
//		ParameterizedN1qlQuery paramQuery = N1qlQuery.parameterized(query.toString(), placeholderValues, params);
//		List<Map<String, Object>> result = new ArrayList<>(defaultTemplate.findByN1QLProjection(paramQuery, SuperHashMap.class));
//		logger.info("param : {}", paramQuery);
		List<Map<String, Object>> result = new ArrayList<>(defaultTemplate.findByN1QLProjection(N1qlQuery.parameterized(query.toString(), JsonObject.fromJson(param), N1qlParams.build().pretty(false)), SuperHashMap.class));
		logger.info("쿼리 결과 : {}" , result);
		return result;
	}

	@Override
	public boolean queryN1QL(String query, String param) {
		return defaultTemplate.queryN1QL(N1qlQuery.parameterized(query.toString(), JsonObject.fromJson(param), N1qlParams.build().pretty(false))).finalSuccess();
	}

}
