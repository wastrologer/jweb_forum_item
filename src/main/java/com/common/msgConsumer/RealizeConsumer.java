package com.common.msgConsumer;


import com.common.entity.ReturnMessage;
import com.common.mq.MsgCommandId;
import com.common.mq.MsgConsumer;
import com.service.ICommentSvc;
import com.service.IEssaySvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jms.ObjectMessage;

public class RealizeConsumer extends MsgConsumer<ReturnMessage> {
	
	protected static final Logger logger = LoggerFactory.getLogger(RealizeConsumer.class);
	
	@Resource
	private IEssaySvc essaySvcImpl;

	@Resource
	private ICommentSvc commentSvcImpl;

	@Override
	protected void doMessageTask(long commandId, ReturnMessage msg, ObjectMessage objMessage) {
		//文章浏览量增加
		if (commandId == MsgCommandId.FORUM_ESSAY_CLICKNUM_ID) {
			try {
				int resultValue = essaySvcImpl.updateEssayClickNum(msg);
			} catch (Exception e) {
				logger.error("文章浏览量增加  Mq 操作 异常"+ e.getMessage());
			}
		}else if (commandId == MsgCommandId.FORUM_ESSAY_COMMENTNUM_ID) {
			try {
				int resultValue = essaySvcImpl.updateEssayCommentNum(msg);
			} catch (Exception e) {
				logger.error("文章评论量增加  Mq 操作 异常"+ e.getMessage());
			}
		}else if (commandId == MsgCommandId.FORUM_ESSAY_RECOMMENDNUM_ID) {
			try {
				int resultValue = essaySvcImpl.updateEssayRecommendNum(msg);
			} catch (Exception e) {
				logger.error("文章推荐量变动  Mq 操作 异常"+ e.getMessage());
			}
		}else if (commandId == MsgCommandId.FORUM_COMMENT_RECOMMENDNUM_ID) {
			try {
				int resultValue = commentSvcImpl.updateCommentRecommendNum(msg);
			} catch (Exception e) {
				logger.error("评论推荐量变动  Mq 操作 异常"+ e.getMessage());
			}
		}
	}
}
