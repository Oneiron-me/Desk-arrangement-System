package com.oneiron.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.oneiron.doc.OneironDoc;
import com.oneiron.doc.TravelDoc;
import com.oneiron.repo.Repository;
import com.oneiron.service.RepositoryService;
import com.oneiron.util.SuperHashMap;
import com.oneiron.util.Util;

@Service
public class RepositoryServiceImpl implements RepositoryService {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier(BeanNames.COUCHBASE_TEMPLATE)
	private CouchbaseTemplate defaultTemplate;

	@Autowired
	Repository repository;

	/*
	 * @Override public List<TravelDoc> select() { // TODO Auto-generated method
	 * stub return null; }
	 */

	@Override
	public List<TravelDoc> findById(Long id) {
		return null;// repository.findById(id);
	}

	@Override
	public List<SuperHashMap> findByKey(TravelDoc doc) throws JsonProcessingException {
		// convert json string
		
		Util util = new Util();
		
		String paramStr = util.serializeObject(doc);

		// String alias = "a";
		/*
		 * query.append("select " + alias +
		 * ".* from `travel-sample` "+alias+" where 1=1 ");
		 * query.append(Util.buildParam(alias, doc));
		 */

		StringBuffer query = new StringBuffer();

		query.append("select a.* from `travel-sample` a where 1=1 ");
		if (doc.getId() != null && !doc.getId().equals(0)) {
			query.append(" and a.id = $id");
		}
		query.append(" limit 1");

		JsonObject placeholderValues = JsonObject.fromJson(paramStr);
		N1qlParams params = N1qlParams.build().pretty(false);

		ParameterizedN1qlQuery paramQuery = N1qlQuery.parameterized(query.toString(), placeholderValues, params);

		logger.info("param : {}", paramQuery);
		logger.info("test 머야 : " + SuperHashMap.class.getSuperclass().getSuperclass().getSuperclass().getTypeName());
		List<SuperHashMap> list = defaultTemplate.findByN1QLProjection(paramQuery, SuperHashMap.class);
		return list;
	}

	@Override
	public int createId(OneironDoc doc) {
		int cnt = 0;

		SecureRandom sr = null;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };

		int[] number = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		int aBound = alphabet.length - 1;
		int nBound = number.length - 1;

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < 9; i++) {
			if (i < 5) {
				sb.append(alphabet[sr.nextInt(aBound)]);
			} else {
				sb.append(number[sr.nextInt(nBound)]);
			}
		}

		logger.info("아이디 : {}", sb.toString());

		JsonObject jObj = JsonObject.create();
		jObj.put("id", sb.toString());
		jObj.put("name", doc.getName());

		JsonDocument jDoc = JsonDocument.create(sb.toString(), jObj);

		logger.info("아이디325235 : {}", jObj);

		logger.info("됏냐이거 : {}", "test");
		
		JsonDocument result = defaultTemplate.getCouchbaseBucket().insert(jDoc);
		logger.info("됏냐이거 : {}", result);
		logger.info("닥유먼트 : {}", result.content());
		
		return cnt;
	}

	@Override
	public List<SuperHashMap> findUserInfo(OneironDoc doc) throws Exception {
		// convert json string
		
		Util util = new Util();
		
		String paramStr = util.serializeObject(doc);

		// String alias = "a";
		/*
		 * query.append("select " + alias +
		 * ".* from `travel-sample` "+alias+" where 1=1 ");
		 * query.append(Util.buildParam(alias, doc));
		 */

		StringBuffer query = new StringBuffer();

		query.append("select META().id, a.* from `oneiron` a where 1=1 ");
		if (doc.getId() != null && !doc.getId().equals(0)) {
			query.append(" and META().id = $id");
		}
		query.append(" limit 1");

		JsonObject placeholderValues = JsonObject.fromJson(paramStr);
		N1qlParams params = N1qlParams.build().pretty(false);

		ParameterizedN1qlQuery paramQuery = N1qlQuery.parameterized(query.toString(), placeholderValues, params);

		logger.info("param : {}", paramQuery);
		logger.info("test 머야 : " + SuperHashMap.class.getSuperclass().getSuperclass().getSuperclass().getTypeName());
		List<SuperHashMap> list = defaultTemplate.findByN1QLProjection(paramQuery, SuperHashMap.class);
		return list;
	}

}
