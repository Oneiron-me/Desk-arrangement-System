package com.oneiron.ws.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageVo {

	private String id;	//유저 아이디
	
	private String name;	//유저 이름
	
	private String message;	// 메시지 내용
	
}
