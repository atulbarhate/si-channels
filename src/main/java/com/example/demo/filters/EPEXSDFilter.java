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
public class EPEXSDFilter implements MessageSelector {

	@Override
	public boolean accept(Message<?> message) {
		// TODO Auto-generated method stub
		
		System.out.println("XSD Filter called........");
		
		return true;
	}

}
