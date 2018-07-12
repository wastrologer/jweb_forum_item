package com.common.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

public abstract class MsgConsumer<T extends Serializable> implements MessageListener {

	public static Logger logger = LoggerFactory.getLogger(MsgConsumer.class);

	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			try {
				ObjectMessage objMessage = (ObjectMessage) message;
				T msg = (T) objMessage.getObject();
				long commandId = objMessage.getLongProperty(MsgProducer.COMMAND_ID);
				doMessageTask(commandId, msg, objMessage);
			} catch (JMSException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			logger.error("unsupport message type!");
		}
	}

	protected abstract void doMessageTask(long commandId, T msg, ObjectMessage objMessage);
}
