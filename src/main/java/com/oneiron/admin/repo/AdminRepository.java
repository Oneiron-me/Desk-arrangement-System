package com.oneiron.admin.repo;

import java.util.ArrayList;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.oneiron.admin.doc.AdminDoc;

public interface AdminRepository extends CrudRepository<AdminDoc, String> {

	@Query("select a from oneiron a where meta().id = ?1")
	ArrayList<AdminDoc> findAllByIdAndStream(String id);
}
