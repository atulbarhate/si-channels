package com.example.demo.flows;

import java.io.File;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.integration.transformer.ObjectToStringTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.example.demo.filters.EPESpelFilter;
import com.example.demo.filters.EPEXSDFilter;
import com.example.demo.transformers.FileToStringTransformer;
import com.solacesystems.jcsmp.TextMessage;

@EnableIntegration
@Configuration
public class SFTRFlow {
	@Autowired
	private ConnectionFactory connectionFactory;

	@Bean
	public MessageSource<?> fileReader() {
		// TODO Auto-generated method stub		
		FileReadingMessageSource reader = new FileReadingMessageSource();
		reader.setDirectory(new File("C:\\env\\ws-sts4\\si-channels\\input"));
		return reader;
	}
	@Bean
	public MessageSource<?> fileReader1() {
		// TODO Auto-generated method stub		
		FileReadingMessageSource reader = new FileReadingMessageSource();
		reader.setDirectory(new File("C:\\env\\ws-sts4\\si-channels\\input1"));
		return reader;
	}

	
   @Bean
    public DirectChannel inputChannel() {
        return new DirectChannel();
    }
   
   @Bean
   public MessageChannel queueChannel() {
       return MessageChannels.queue().get();
   }
   
   
	@Bean
	public IntegrationFlow sftrFlow(){
		return IntegrationFlows.from( Jms.inboundGateway(connectionFactory)
				.destination("process.queue")
				.configureListenerContainer(spec -> spec.sessionTransacted(true)))
				.filter(new EPEXSDFilter())
				.filter(new EPESpelFilter())
				.transform(new ObjectToStringTransformer())
//				.routeToRecipients(r -> r
//						.recipientFlow(f -> f 
//								.transform(new FileToStringTransformer())
//								.log(LoggingHandler.Level.INFO, "payload")
//								.handle(fileWriter())
//								)
//						
//						.recipientFlow(f -> f
//								.transform(new FileToStringTransformer())
//								.log(LoggingHandler.Level.INFO, "payload")
//								.handle(fileWriter())
//								)
//						.defaultOutputToParentFlow()
//						)
				.log(LoggingHandler.Level.INFO, "payload.getPayload().toString()")
//				.transform(new FileToStringTransformer())
				
				.get();

	}

	
	
	@Bean
	public IntegrationFlow sftrFlow1(){
		return IntegrationFlows.from( fileReader1(), c -> c.poller(Pollers.fixedDelay(100)))
				.filter(new EPEXSDFilter())
				.filter(new EPESpelFilter())
				.transform(File.class,  new FileToStringTransformer())
				.log(LoggingHandler.Level.INFO, "payload")
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


//	@Bean(name = PollerMetadata.DEFAULT_POLLER)
//	public PollerMetadata defaultPoller() {
//
//	    PollerMetadata pollerMetadata = new PollerMetadata();
//	    pollerMetadata.setTrigger(new PeriodicTrigger(10));
//	    return pollerMetadata;
//	}
//	
	
	
//  @ServiceActivator(inputChannel = "routingChannel")
//  @Bean
//  public RecipientListRouter router() {
//      RecipientListRouter router = new RecipientListRouter();
//      router.setSendTimeout(1_234L);
//      router.setIgnoreSendFailures(true);
//      router.setApplySequence(true);
//      router.addRecipient(inputChannel());
//      router.addRecipient(queueChannel());
////      router.addRecipient("channel3");
//      return router;
//  }				
//  
	
//	public MessageChannel solaceQueueReader() {
//		
//		return new PollableChannel() {
//			
//			@Override
//			public boolean send(Message<?> message, long timeout) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public Message<?> receive(long timeout) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public Message<?> receive() {
//				System.out.println("message received");
//				return null;
//			}
//		};
//	}

}
