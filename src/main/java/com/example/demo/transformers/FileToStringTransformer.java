/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demo.transformers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.support.DefaultMessageBuilderFactory;
import org.springframework.integration.support.MessageBuilderFactory;
import org.springframework.integration.support.utils.IntegrationUtils;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

/**
 * A payload transformer that copies a File's contents to a String.
 *
 * @author Mark Fisher
 * @author Gary Russell
 */
public class FileToStringTransformer implements Transformer, BeanFactoryAware {

	private final Log logger = LogFactory.getLog(this.getClass());

	private volatile Charset charset = Charset.defaultCharset();

	private volatile BeanFactory beanFactory;

	private volatile boolean deleteFiles;

	private volatile MessageBuilderFactory messageBuilderFactory = new DefaultMessageBuilderFactory();

	private boolean messageBuilderFactorySet;
	/**
	 * Set the charset name to use when copying the File to a String.
	 *
	 * @param charset The charset.
	 */
	public void setCharset(String charset) {
		Assert.notNull(charset, "charset must not be null");
		Assert.isTrue(Charset.isSupported(charset), "Charset '" + charset + "' is not supported.");
		this.charset = Charset.forName(charset);
	}

	protected final String transformFile(File file) throws IOException {
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), this.charset));
		String fileString = FileCopyUtils.copyToString(reader); 
		
		logger.info(" contents : " + fileString);
		
		return fileString;
	}

	
	public Message<?> transform(Message<?> message) {
		try {
			Assert.notNull(message, "Message must not be null");
			Object payload = message.getPayload();
			Assert.notNull(payload, "Message payload must not be null");
			Assert.isInstanceOf(File.class, payload, "Message payload must be of type [java.io.File]");
			File file = (File) payload;
			String result = this.transformFile(file);
			Message<?> transformedMessage = getMessageBuilderFactory().withPayload(result)
					.copyHeaders(message.getHeaders())
					.setHeaderIfAbsent(FileHeaders.ORIGINAL_FILE, file)
					.setHeaderIfAbsent(FileHeaders.FILENAME, file.getName())
					.build();
			
			if (this.deleteFiles) {
				if (!file.delete() && this.logger.isWarnEnabled()) {
					this.logger.warn("failed to delete File '" + file + "'");
				}
			}
			return transformedMessage;
		}
		catch (Exception e) {
			throw new MessagingException(message, "failed to transform File Message", e);
		}
	}



	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	protected MessageBuilderFactory getMessageBuilderFactory() {
		if (!this.messageBuilderFactorySet) {
			if (this.beanFactory != null) {
				this.messageBuilderFactory = IntegrationUtils.getMessageBuilderFactory(this.beanFactory);
			}
			this.messageBuilderFactorySet = true;
		}
		return this.messageBuilderFactory;
	}
}
