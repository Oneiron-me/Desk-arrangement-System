package com.oneiron.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.oneiron.admin.dao.AdminDao;
import com.oneiron.admin.doc.AdminDoc;
import com.oneiron.admin.doc.AdminVo;
import com.oneiron.admin.service.AdminService;
import com.oneiron.common.service.CommonService;
import com.oneiron.repo.Repository;
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
	
	@Autowired
	CommonService commonServiceImpl;
	
	@Autowired
	AdminDao adminDao;
	
	@Autowired
	Repository repository;
	
	@Override
	public Map<String, Object> getAdminInfoByN1ql() {
		
		String userId = "properties@kakao.com";
		
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
		
		List<Map<String, Object>> list = commonServiceImpl.findByN1qlProjection(query.toString(), paramStr);
		
		Map<String, Object> result = new HashMap<>();
		
		if(list.size() > 0) {
			result.putAll(list.get(0));
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

	@Override
	public AdminVo getAdminInfoTest() {
		String userId = "properties@kakao.com";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", userId);
		
		AdminDoc d = new AdminDoc();
		d.setId(userId);
		
		Stream<AdminDoc> dd = repository.findAllByCustomQueryAndStream(userId);
		
		dd.forEach(test -> logger.info("제발 {}", test));
		
		return null;// adminDao.getAdminInfo(paramMap);
	}

}
