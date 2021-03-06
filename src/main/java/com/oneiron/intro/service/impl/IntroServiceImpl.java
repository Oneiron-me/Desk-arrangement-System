package com.oneiron.intro.service.impl;

import java.util.ArrayList;
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
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.oneiron.common.service.CommonService;
import com.oneiron.intro.doc.DeskDoc;
import com.oneiron.intro.service.IntroService;
import com.oneiron.util.CommonFunction;
import com.oneiron.util.Util;

@Service
public class IntroServiceImpl implements IntroService {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier(BeanNames.COUCHBASE_TEMPLATE)
	private CouchbaseTemplate defaultTemplate;
	
	@Resource(name="commonFunction")
	private CommonFunction commonFunction;
	
	@Autowired
	CommonService commonServiceImpl;
	
	@Override
	public int deskSave(DeskDoc doc) {
		Bucket bucket = defaultTemplate.getCouchbaseBucket();
		
		int cnt = 0;
		
		Util util = new Util();
		String json = null;
		
		try {
			json = util.serializeObject(doc);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		JsonObject obj = JsonObject.fromJson(json);
		JsonDocument jDoc = JsonDocument.create(null, obj);
		JsonDocument result = bucket.upsert(jDoc);
		
		if(result != null)
			cnt = 1;
		
		return cnt;
	}

	@Override
	public boolean createDesk(Map<String, Object> map) {
		
		boolean resultBool = false;
		
		String userId = commonFunction.userId();
		String userName = commonFunction.userName();
		
		Bucket bucket = defaultTemplate.getCouchbaseBucket();
		
		Util util = new Util();
		
		Map<String, Object> keyMap = util.genUUIDAndCurrentTimes();
		String uuid = keyMap.get("uuid").toString();
		String time = keyMap.get("time").toString();
		
		// userid로 deskID를 찾는데 deskid가 존재안하면 userId Document에 호다닥 집어넣어줌
		
		JsonDocument deskInfo = bucket.get(userId);
		if(deskInfo.content().get("deskList") == null) {
			bucket.mapAdd(userId, "deskList", new ArrayList<String>());
		}
		
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userId", userId);
		userMap.put("deskId", uuid);
		
		String paramStr = null;
		try {
			paramStr = util.serializeObject(userMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		StringBuffer query = new StringBuffer();

		query.append("UPDATE `oneiron` USE KEYS $userId SET deskList = ARRAY_PUT(IFNULL(deskList, []), $deskId)");

		resultBool = commonServiceImpl.queryN1QL(query.toString(), paramStr);
		
		//user document에 deskList가 만들어지면 desk document를 생성함.
		if(resultBool) {
			
			List<Map<String,Object>> userList = new ArrayList<>();
			Map<String,Object> userListInnerMap = new HashMap<String, Object>();
			userListInnerMap.put("userId", userId);
			userListInnerMap.put("userName", userName);
			userList.add(userListInnerMap);
			
			Map<String, Object> deskMap = new HashMap<String, Object>();
			deskMap.putAll(map);
			deskMap.put("createTime", time);
			deskMap.put("deskId", uuid);
			deskMap.put("userList", userList);
			deskMap.put("useAt", "Y");
			deskMap.put("docType", "desk");
			deskMap.put("msgList", new ArrayList<String>());
			
			String json = null;
			try {
				json = util.serializeObject(deskMap);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			JsonObject obj = JsonObject.fromJson(json);
			JsonDocument jDoc = JsonDocument.create(uuid, obj);
			bucket.insert(jDoc);
		}
		
		return resultBool;
	}

	@Override
	public List<Map<String, Object>> getDeskList() {
		String userId = commonFunction.userId();
		
		Util util = new Util();
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		
		String paramStr = null;
		try {
			paramStr = util.serializeObject(paramMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		StringBuffer query = new StringBuffer();

		query.append("SELECT a.comments, a.createTime, a.deskId, a.deskName, a.userList FROM `oneiron` a ");
		query.append("where (ANY v IN a.userList SATISFIES v.userId = $userId END) and a.useAt = 'Y' and a.docType = 'desk' ");
		
		return commonServiceImpl.findByN1qlProjection(query.toString(), paramStr);
	}

}
