/**
 * 
 */
package com.example.demo.routes;

import java.util.Collection;

import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

/**
 * @author DELL
 *
 */
public class EPECSVRoute extends AbstractMessageRouter {

	@Override
	protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

}
