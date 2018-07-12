package com.common.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

public class MsgProducer {

	public static Logger logger = LoggerFactory.getLogger(MsgProducer.class);

	public static final String COMMAND_ID = "COMMAND_ID";

	private JmsTemplate jmsTemplate;
	private String destination;

	public MsgProducer(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void send(final long commandId, final Serializable msg) throws JMSException {
		send(commandId, msg, destination);
	}

	public void send(final long commandId, final Serializable msg, final String destination) throws JMSException {
		if (destination != null) {
			MessageCreator messageCreator = new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					ObjectMessage objMessage = session.createObjectMessage();
					objMessage.setObject(msg);
					objMessage.setLongProperty(MsgProducer.COMMAND_ID, commandId);
					return objMessage;
				}
			};
			jmsTemplate.send(destination, messageCreator);
		} else {
			logger.error("msg send failed. destination is null!");
			throw new JMSException("msg send failed. destination is null!");
		}
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}
