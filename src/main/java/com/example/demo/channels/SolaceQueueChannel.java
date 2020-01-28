/**
 * 
 */
package com.example.demo.channels;

import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

/**
 * @author DELL
 *
 */
public class SolaceQueueChannel implements PollableChannel {

	@Override
	public boolean send(Message<?> message, long timeout) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Message<?> receive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message<?> receive(long timeout) {
		// TODO Auto-generated method stub
		return null;
	}

}
