package com.oneiron.ws.vo;

import lombok.Getter;

@Getter
public class ResponseContentVo {
	String content;
	
	public ResponseContentVo() {};
	
	public ResponseContentVo(String content) {
		this.content = content;
	}
	
}
