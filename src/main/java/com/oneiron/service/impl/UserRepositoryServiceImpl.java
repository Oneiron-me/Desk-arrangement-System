package com.oneiron.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneiron.login.doc.UserDoc;
import com.oneiron.service.UserRepositoryService;
import com.oneiron.util.Util;

@Service
public class UserRepositoryServiceImpl implements UserRepositoryService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier(BeanNames.COUCHBASE_TEMPLATE)
	private CouchbaseTemplate defaultTemplate;
	
	@Override
	public int save(UserDoc entity) {
		Bucket bucket = defaultTemplate.getCouchbaseBucket();
		int cnt = 0;
		
		Util util = new Util();
		String json = null;
		
		try {
			json = util.serializeObject(entity);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		JsonObject obj = JsonObject.fromJson(json);
		JsonDocument doc = JsonDocument.create(entity.getId(), obj);
		JsonDocument result = bucket.upsert(doc);
		
		if(result != null)
			cnt = 1;
		
		return cnt;
	}

	@Override
	public int delete(UserDoc doc) {
		Bucket bucket = defaultTemplate.getCouchbaseBucket();
		int cnt = 0;
		
		JsonDocument result = bucket.remove(doc.getId());
		
		if(result != null)
			cnt = 1;
		
		return cnt;
	}

	@Override
	public UserDoc findUserById(String loginId) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		Bucket bucket = defaultTemplate.getCouchbaseBucket();
		UserDoc doc = new UserDoc();
		doc.setId(loginId);
		
		Util util = new Util();
		String json = null;
		
		try {
			json = util.serializeObject(doc);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		JsonObject jObj = JsonObject.fromJson(json); 
		JsonDocument jDoc = JsonDocument.create(loginId, jObj); 
		JsonDocument resultDoc = bucket.get(jDoc);
		
		UserDoc result = null; 
		
		try {
			if(resultDoc != null){
				result = mapper.readValue(resultDoc.content().toString(), UserDoc.class);
			}
		} catch (JsonProcessingException e) { 
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public UserDoc findUserByIdN1ql(String id) {
		
		UserDoc result = null;
		
		Util util = new Util();
		
		Map<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("id", id);
		
		String paramStr = null;
		try {
			paramStr = util.serializeObject(paramMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		StringBuffer query = new StringBuffer();

		query.append("select META().id, a.name, a.`password`, a.`role` from `oneiron` a where META().id = $id limit 1");

		JsonObject placeholderValues = JsonObject.fromJson(paramStr);
		N1qlParams params = N1qlParams.build().pretty(false);

		ParameterizedN1qlQuery paramQuery = N1qlQuery.parameterized(query.toString(), placeholderValues, params);
		
		List<UserDoc> list = defaultTemplate.findByN1QLProjection(paramQuery, UserDoc.class);
		if(list.size() > 0) {
			result = list.get(0);
		}
		
		return result;
	}


}
