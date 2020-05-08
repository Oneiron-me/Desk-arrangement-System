package com.oneiron.admin.doc;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AdminVo {

	private String id;
	private List<Map<String, Object>> info;
	private Map<String, Object> projects;
}
