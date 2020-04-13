package com.oneiron.repo;

import org.springframework.data.repository.CrudRepository;

import com.oneiron.admin.doc.AdminDoc;

public interface Repository extends CrudRepository<AdminDoc, Integer>{
}
