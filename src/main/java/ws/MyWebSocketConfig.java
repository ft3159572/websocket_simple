package ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 现在是以注解的方式加载的配置, 如果想用xml的方式配置, 那么就不用写这个类了 
 * 并且打开applicationContext.xml里面的配置, 详情看applicationContext.xml注释
 * @author sm-air
 *
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class MyWebSocketConfig extends WebMvcConfigurerAdapter implements
		WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		// 前台 可以使用websocket环境
		registry.addHandler(myWebSocketHandler(), "/websocket")
				.addInterceptors(new HandshakeInterceptor());

		// 前台 不可以使用websocket环境，则使用sockjs进行模拟连接
		registry.addHandler(myWebSocketHandler(), "/sockjs/websocket")
				.addInterceptors(new HandshakeInterceptor()).withSockJS();
	}

	// websocket 处理类
	@Bean
	public WebSocketHandler myWebSocketHandler() {
		return new MyWebSocketHandler();
	}

}