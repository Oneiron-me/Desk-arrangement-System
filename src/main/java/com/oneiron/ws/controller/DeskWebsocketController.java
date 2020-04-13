package com.oneiron.ws.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.oneiron.desk.service.DeskService;
import com.oneiron.ws.vo.ResponseContentVo;

@Controller
public class DeskWebsocketController {
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "deskServiceImpl")
	DeskService deskServiceImpl;
	
	
	@MessageMapping("/write/{deskId}")
	@SendTo("/topic/responseNote/{deskId}")
	public Map<String, Object> responseContent(@DestinationVariable("deskId") String deskId ,@Payload Map<String, Object> map, SimpMessageHeaderAccessor accessor) throws InterruptedException{
		Thread.sleep(300); // siomulated delay
		
		//만약 html에 넣을거였으면 escape 처리를 했을건데 지금은 textarea에 써서 안해도될듯하다!
//		map.replace("message", HtmlUtils.htmlEscape(map.get("message").toString()));
		
		Map<String, Object> result = deskServiceImpl.putNote(map, accessor);
		
		String flag = result.get("flag").toString();
		
		map.replace("message", HtmlUtils.htmlUnescape(map.get("message").toString()));
		
		if(flag.equals("success"))
			return result;
		else {
			map.put("content", "서버에 이상이 있대요 죄송해요!!! 얼른 고칠게요!!!");
			return map;
		}
	}
	
//	@MessageMapping("/connect/{deskId}")
//	@SendTo("/topic/connect/{deskId}")
//	public Greeting connect(@DestinationVariable("deskId") String deskId ,@Payload HelloMessage message) throws InterruptedException{
//		Thread.sleep(100); // siomulated delay
//		
//		Util util = new Util();
//		Map<String, Object> map = util.genUUIDAndCurrentTimes();
//		
//		SessionInfo info = new SessionInfo();
//		String s = null;
//		
//		try {
//			s = util.serializeObject(info.getUserList());
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return new Greeting(s);
//	}
}
