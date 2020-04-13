package com.oneiron.admin.doc;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(content=Include.NON_NULL)
public class AdminDoc implements Serializable{

	private static final long serialVersionUID = -7286408960591084708L;
	
	@Id
	String id; // meta id
	
	AdminDoc info; // 뭉탱이들
	
	String birth, //생일
	realName, //이름
	weapon, //사용가능한기술
	location, //사는곳
	email; // 내 이메일
	
	String projectSeqno; // 프로젝트 키
	
	List<AdminDoc> projects; //여태까지한거 프로젝트리스트
	
	String title, // 플젝 제목
	stDate, // 플젝 시작일 대충
	endDate, // 플젝 마감일 대충
	content; // 플젝 내용

}
