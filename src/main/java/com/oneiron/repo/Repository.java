package com.oneiron.repo;

import java.util.stream.Stream;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.query.Param;

import com.oneiron.admin.doc.AdminDoc;

public interface Repository extends org.springframework.data.repository.Repository<AdminDoc, String>{
	
	@Query("select a.*, META().id AS _ID, META().cas AS _CAS from oneiron a where id = :userId")
	Stream<AdminDoc> findAllByCustomQueryAndStream(@Param("userId")String userId);
	
}
