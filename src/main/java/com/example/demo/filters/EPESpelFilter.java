/**
 * 
 */
package com.example.demo.filters;

import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

/**
 * @author DELL
 *
 */
public class EPESpelFilter implements MessageSelector {

	@Override
	public boolean accept(Message<?> message) {
		// TODO Auto-generated method stub
		
		System.out.println("Spel Filter called........");
		
		return true;
	}

}
