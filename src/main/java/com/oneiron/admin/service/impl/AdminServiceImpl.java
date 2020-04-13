package com.oneiron.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.oneiron.admin.service.AdminService;
import com.oneiron.util.CommonFunction;
import com.oneiron.util.SuperHashMap;
import com.oneiron.util.Util;

@Service
public class AdminServiceImpl implements AdminService {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier(BeanNames.COUCHBASE_TEMPLATE)
	private CouchbaseTemplate defaultTemplate;
	
	@Resource(name="commonFunction")
	private CommonFunction commonFunction;
	
	@Override
	public Map<String, Object> getAdminInfoByN1ql() {
		
		String userId = "properties@kakao.com";
		
		logger.info("세션정보 {}" , userId);
		
		Util util = new Util();
		
		Map<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("id", userId);
		
		String paramStr = null;
		try {
			paramStr = util.serializeObject(paramMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		StringBuffer query = new StringBuffer();

		query.append("select a.info, a.projects from `oneiron` a where META().id = $id limit 1");

		JsonObject placeholderValues = JsonObject.fromJson(paramStr);
		N1qlParams params = N1qlParams.build().pretty(false);

		ParameterizedN1qlQuery paramQuery = N1qlQuery.parameterized(query.toString(), placeholderValues, params);
		List<SuperHashMap> list = defaultTemplate.findByN1QLProjection(paramQuery, SuperHashMap.class);
		
		Map<String, Object> result = new HashMap<>();
		if(list.size() > 0) {
			result = list.get(0);
		}
		
		return result;
	}

	@Override
	public boolean putAdminInfo(Map<String, Object> map) {
		String userId = commonFunction.userId();
		Bucket bucket = defaultTemplate.getCouchbaseBucket();
		
		map.put("lastChange", commonFunction.today());
		return bucket.mapAdd(userId, "info", map);
	}

	@Override
	public boolean putAdminProjectInfo(Map<String, Object> map) {
		String userId = commonFunction.userId();
		Bucket bucket = defaultTemplate.getCouchbaseBucket();
		map.put("lastChange", commonFunction.today());
		return bucket.mapAdd(userId, "projects", map);
		
	}

}
