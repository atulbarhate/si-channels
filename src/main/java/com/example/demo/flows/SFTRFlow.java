package com.example.demo.flows;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

import com.example.demo.transformers.FileToStringTransformer;

@EnableIntegration
@Configuration
public class SFTRFlow {

	@Bean
	public MessageSource<?> fileReader() {
		// TODO Auto-generated method stub
		FileReadingMessageSource reader = new FileReadingMessageSource();
		reader.setDirectory(new File("C:\\env\\ws-sts4\\si-channels\\input"));
		
		
		return reader;
	}
	
	@Bean
	public IntegrationFlow sftrFlow(){
		return IntegrationFlows.from(fileReader(), c -> c.poller(Pollers.fixedDelay(100)))
				//.channel("solaceQueueReader")
				.log(LoggingHandler.Level.INFO, "payload")
				.transform(new FileToStringTransformer())
				//.handle(fileWriter())
				.log(LoggingHandler.Level.INFO, "payload")
				.log(LoggingHandler.Level.INFO, "There is end of process")
				.get();
		
	}

//	public SolMessageConsumer solaceQueueReader() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Bean
	public MessageHandler fileWriter() {
		// TODO Auto-generated method stub
		FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("output"));
		handler.setExpectReply(false);
		return handler;
	}


	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata defaultPoller() {

	    PollerMetadata pollerMetadata = new PollerMetadata();
	    pollerMetadata.setTrigger(new PeriodicTrigger(10));
	    return pollerMetadata;
	}
	
	
	public MessageChannel solaceQueueReader() {
		
		return new PollableChannel() {
			
			@Override
			public boolean send(Message<?> message, long timeout) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Message<?> receive(long timeout) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Message<?> receive() {
				System.out.println("message received");
				return null;
			}
		};
	}

}
