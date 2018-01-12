package ws;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyWebSocketHandler implements WebSocketHandler {

	private static final Logger log = LoggerFactory
			.getLogger(MyWebSocketHandler.class);

	// 保存所有的用户session
	private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();

	// 连接 就绪时
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {

		System.out.println("connect to the websocket success......");
		session.sendMessage(new TextMessage("Server:connected OK!"));
		System.out.println("session:" + session);

		users.add(session);

	}

	// 处理信息
	@Override
	public void handleMessage(WebSocketSession session,
			WebSocketMessage<?> message) throws Exception {

		TextMessage returnMessage = new TextMessage(message.getPayload()
				+ " received at server");
		System.out.println(session.getHandshakeHeaders().getFirst("Cookie"));
		session.sendMessage(returnMessage);

		Gson gson = new Gson();

		// 将消息JSON格式通过Gson转换成Map
		// message.getPayload().toString() 获取消息具体内容
		Map<String, Object> msg = gson.fromJson(
				message.getPayload().toString(),
				new TypeToken<Map<String, Object>>() {
				}.getType());

		log.info("handleMessage......." + message.getPayload() + "..........."
				+ msg);

		// session.sendMessage(message);

		// 处理消息 msgContent消息内容
		TextMessage textMessage = new TextMessage(msg.get("msgContent")
				.toString(), true);
		// 调用方法（发送消息给所有人）
		sendMsgToAllUsers(textMessage);

	}

	// 处理传输时异常
	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		// TODO Auto-generated method stub

	}

	// 关闭 连接时
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus closeStatus) throws Exception {

		log.info("connect websocket closed.......");

		users.remove(session);

	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

	// 给所有用户发送 信息
	public void sendMsgToAllUsers(WebSocketMessage<?> message) throws Exception {

		for (WebSocketSession user : users) {
			user.sendMessage(message);
		}

	}

}