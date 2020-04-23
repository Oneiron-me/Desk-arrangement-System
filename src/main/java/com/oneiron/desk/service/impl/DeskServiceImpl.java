package com.oneiron.desk.service.impl;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.oneiron.common.service.CommonService;
import com.oneiron.desk.service.DeskService;
import com.oneiron.login.doc.UserDoc;
import com.oneiron.util.Util;

@Service
public class DeskServiceImpl implements DeskService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier(BeanNames.COUCHBASE_TEMPLATE)
	private CouchbaseTemplate defaultTemplate;
	
	@Autowired
	HttpServletRequest httpServletRequest;
	
	@Autowired
	CommonService commonServiceImpl;

	@Override
	public Map<String, Object> putNote(Map<String, Object> map, SimpMessageHeaderAccessor accessor) {
		//헤더에 넣어둔 deskId를 가져왔음. 근데 저거 헤더가져온거 타입이 LinkedHashMap이라 저거로 가져옴...
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Object> deskMap = accessor.getMessageHeaders().get("DestinationVariableMethodArgumentResolver.templateVariables", new LinkedHashMap<String, Object>().getClass());
		
		Util util = new Util();
		Map<String, Object> utilMap = util.genUUIDAndCurrentTimes();
		
//		logger.info("맵 넘어왔냐 : {} ", map);
		
		Principal parkSipal = accessor.getUser();
		
		
		//프린시팔 별로 username, id 가져오는게 달라져서 분기함
		String userId = null;
		String userName = null;
		
		Object user = ((Authentication) parkSipal).getPrincipal();
		
		if(user instanceof DefaultOidcUser) {
			DefaultOidcUser oidcUser = (DefaultOidcUser)user;
			userId = oidcUser.getEmail();
			userName = oidcUser.getFullName();
			
		} else if(user instanceof UserDoc) {
			UserDoc doc = (UserDoc)((Authentication) parkSipal).getPrincipal();
			userId = doc.getId();
			userName = doc.getUsername();
		}
		
		//파라미터 만듬
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("deskId", deskMap.get("deskId"));
		map.put("noteId", utilMap.get("uuid"));
		map.put("createTime", utilMap.get("time"));
		
//		logger.info("이건또뭐냐 {}", deskMap.get("deskId"));
		
		String paramStr = null;
		try {
			paramStr = util.serializeObject(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		StringBuffer query = new StringBuffer();

		query.append("UPDATE `oneiron` USE KEYS $deskId SET msgList = ");
		query.append("ARRAY_PREPEND( {\"message\" : $message, \"createTime\" : $createTime, \"noteId\" : $noteId, \"userId\" : $userId, \"userName\" : $userName, \"deleteAt\" : \"N\"}, msgList)");

		//넣은 결과임
		boolean successFlag = commonServiceImpl.queryN1QL(query.toString(), paramStr);
		
		if(successFlag)
			map.put("flag", "success");
		else
			map.put("flag", "fail");
		
		
		return map;
	}

	@Override
	public Map<String, Object> getNoteList(Map<String, Object> map) {
		Util util = new Util();
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("deskId", map.get("deskId").toString());
		
		String paramStr = null;
		try {
			paramStr = util.serializeObject(paramMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		StringBuffer query = new StringBuffer();

		query.append("SELECT a.msgList FROM `oneiron` a ");
		query.append("where a.deskId = $deskId and (ANY v IN a.msgList SATISFIES v.deleteAt = 'N' END) limit 1 ");
		
		List<Map<String, Object>> list = commonServiceImpl.findByN1qlProjection(query.toString(), paramStr);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(list.size() > 0)
			result.putAll(list.get(0));
		
		return result;
	}

	@Override
	public List<Map<String, Object>> getInviteUserList(Map<String, Object> map) {
		Util util = new Util();
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("deskId", map.get("deskId").toString());
		
		String paramStr = null;
		try {
			paramStr = util.serializeObject(paramMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		StringBuffer query = new StringBuffer();

		query.append("select b.id as userId, b.name as userName from( ");
		query.append("	select a.id, a.name, ");
		query.append("		case when ( ANY v IN a.deskList SATISFIES v = $deskId END) then true else false end as exist ");
		query.append("	from `oneiron` a ");
		query.append("	where a.docType = 'user' ");
		query.append(") b ");
		query.append("where b.exist = false ");
		
		return commonServiceImpl.findByN1qlProjection(query.toString(), paramStr);
	}

	@Override
	public boolean putInviteUsers(Map<String, Object> map) {
		
		boolean result = false;
		
		Util util = new Util();
		
		String paramStr = null;
		try {
			paramStr = util.serializeObject(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		StringBuffer query = new StringBuffer();
		
		query.append("UPDATE `oneiron` USE KEYS $userId SET deskList = ARRAY_PUT(IFNULL(deskList, []), $deskId)");
		
		//유저를 초대해서 집어넣은게 성공했을때만!!
		if(commonServiceImpl.queryN1QL(query.toString(), paramStr)) {
			// sb 다시 재활용해서 쓴담
			query = new StringBuffer();
			query.append("UPDATE `oneiron` USE KEYS $deskId SET userList = ");
			query.append("ARRAY_PUT(IFNULL(userList, []), {\"userId\" : $userId, \"userName\" : $userName })");
			result = commonServiceImpl.queryN1QL(query.toString(), paramStr);
		}
		
		return result;
	}
}
