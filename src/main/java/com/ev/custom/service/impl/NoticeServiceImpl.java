package com.ev.custom.service.impl;

import com.ev.custom.dao.NoticeDao;
import com.ev.custom.domain.NoticeAssocDO;
import com.ev.custom.domain.NoticeDO;
import com.ev.custom.service.NoticeAssocService;
import com.ev.custom.service.NoticeService;
import com.ev.custom.service.WeChatService;
import com.ev.custom.vo.NoticeEntity;
import com.ev.system.service.UserService;
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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Service
public class NoticeServiceImpl implements NoticeService{
	@Autowired
	private NoticeDao noticeDao;

	@Autowired
	private NoticeAssocService noticeAssocService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Environment env;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WeChatService weChatService;

	@Autowired
	private UserService userService;

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
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return noticeDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return noticeDao.countForMap(map);
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
	public void saveAndSendSocket(String title, String content,Long billId,String contentDetail,  Long type, Long fromUser, List<Long> toUsers) throws IOException, ParseException {
		NoticeEntity noticeEntity = saveNotice(title, content, billId,contentDetail, type, fromUser, toUsers);
		/**
		 * 往rabbit发送队列消息
		 */
		sendMessage(noticeEntity);
	}

	/**
	 * 1.保存消息主体信息
	 * 2.保存消息关联用户
	 * @param title 标题
	 * @param content 内容
	 * @param billId 单据主键
	 * @param contentDetail 内容明细
	 * @param type 类型
	 * @param fromUser 发送人
	 * @param toUsers 接收人/抄送人
	 */
	private NoticeEntity saveNotice(String title, String content, Long billId,String contentDetail, Long type, Long fromUser, List<Long> toUsers){
		if(toUsers.size()==0){
			return null;
		}
		//1.保存消息主体信息
		NoticeEntity noticeEntity = new NoticeEntity();
		NoticeDO notice =new NoticeDO();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setContentDetail(contentDetail);
		notice.setBillId(billId);
		notice.setType(type);
		notice.setFromUserId(fromUser);
		save(notice);
		//2.保存消息关联用户
		List<NoticeAssocDO> noticeAssocDOList = new ArrayList<>();
		for(Long toUserId : toUsers){
			NoticeAssocDO noticeAssocDO = new NoticeAssocDO(notice.getId(),toUserId,null,"0",0);
			noticeAssocDOList.add(noticeAssocDO);
		}
		noticeAssocService.batchInsert(noticeAssocDOList);
		noticeEntity.setNoticeDO(notice);
		noticeEntity.setToUserList(toUsers);
		return noticeEntity;
	}

	/**
	 *
	 * @param message
	 * @throws JsonProcessingException
	 */
	private void sendMessage(NoticeEntity message) throws JsonProcessingException {
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.setExchange("gyhl.data.notice.exchange");
		rabbitTemplate.setRoutingKey("gyhl.data.notice.routing.key");
		Message messageObj= MessageBuilder.withBody(objectMapper.writeValueAsBytes(message)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
		messageObj.getMessageProperties().setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, MessageProperties.CONTENT_TYPE_JSON);
		rabbitTemplate.convertAndSend(messageObj);
	}
	
}
