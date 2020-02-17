package com.ev.custom.service.impl;

import com.ev.custom.dao.NoticeDao;
import com.ev.custom.domain.NoticeDO;
import com.ev.custom.service.NoticeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	private NoticeDao noticeDao;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Environment env;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public NoticeDO get(Long id){
		return noticeDao.get(id);
	}
	
	@Override
	public List<NoticeDO> list(Map<String, Object> map){
		return noticeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return noticeDao.count(map);
	}
	
	@Override
	public int save(NoticeDO notice){
		return noticeDao.save(notice);
	}
	
	@Override
	public int update(NoticeDO notice){
		return noticeDao.update(notice);
	}
	
	@Override
	public int remove(Long id){
		return noticeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return noticeDao.batchRemove(ids);
	}

	@Override
	public void saveAndSendSocket(String title, String content,String contentDetail, Long type, Long fromUser, Long toUser) {
		NoticeDO notice =new NoticeDO(type,title,content,contentDetail,fromUser,toUser);
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.setExchange("gyhl.data.notice.exchange");
		rabbitTemplate.setRoutingKey("gyhl.data.notice.routing.key");
		Message messageObj= null;
		try {
			messageObj = MessageBuilder.withBody(objectMapper.writeValueAsBytes(notice)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		messageObj.getMessageProperties().setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, MessageProperties.CONTENT_TYPE_JSON);
		rabbitTemplate.convertAndSend(messageObj);
	}


}
