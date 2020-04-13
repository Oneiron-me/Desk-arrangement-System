package com.oneiron.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
	
	public String serializeObject(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsString(obj);
	}
	
	public Map<String, Object> genUUIDAndCurrentTimes(){
		
		Map<String, Object> map = new HashMap<>();
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		map.put("uuid", uuid);
		map.put("time", format.format(System.currentTimeMillis()));
		
		return map;
	}
	
	public Object deepCopy(Object o) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		
		byte[] buff = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(buff);
		ObjectInputStream os = new ObjectInputStream(bais);
		Object copy = os.readObject();
		return copy;
	}
	
	public String buildParam(String alias, Object doc) throws JsonProcessingException {
		
		StringBuffer sb = new StringBuffer();
		
		if(doc != null && doc instanceof Serializable) {
			try {
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject)parser.parse(serializeObject(doc));
				
				for(Object key : obj.keySet()) {
					
					if(alias != null && !alias.equals("")) {
						sb.append(" and " + alias + "." + key + " = $" + key);
					} else {
						sb.append(" and "+ key + " = $" + key);
					}
					
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
	
	public SCryptPasswordEncoder sCryptPasswordEncoder(){
		return new SCryptPasswordEncoder();
	}
	
	/**
	 * 
	 * @param cpuCost : default 2
	 * @param memoryCost : default 8
	 * @param parallelization : default 1
	 * @param keyLength : default 32
	 * @param saltLength : default 64
	 * @return
	 */
	public SCryptPasswordEncoder sCryptPasswordEncoder(int cpuCost, int memoryCost, int parallelization, int keyLength, int saltLength){
		return new SCryptPasswordEncoder(cpuCost, memoryCost, parallelization, keyLength, saltLength);
	}
}