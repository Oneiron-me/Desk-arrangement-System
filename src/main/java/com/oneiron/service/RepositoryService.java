package com.oneiron.service;

import java.util.List;

import com.oneiron.doc.OneironDoc;
import com.oneiron.doc.TravelDoc;
import com.oneiron.util.SuperHashMap;

public interface RepositoryService {
	
	public int createId(OneironDoc doc);
	
//	public List<TravelDoc> select();
	public List<TravelDoc> findById(Long id);
	
	public List<SuperHashMap> findByKey(TravelDoc doc) throws Exception;
	
	public List<SuperHashMap> findUserInfo(OneironDoc doc) throws Exception;
	
}
