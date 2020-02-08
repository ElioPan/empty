package com.ev.apis.controller.custom;


import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.custom.service.LeaveApplyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/", tags = "测试RabbitMQ")
public class RabbitApiController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LeaveApplyService leaveApplyService;

    @EvApiByToken(value = "/apis/rabbit/rabbitTest", method = RequestMethod.GET, apiTitle = "测试rabbitMQ")
    @ApiOperation("测试rabbit")
    public R testRabbit() throws JsonProcessingException {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setExchange(env.getProperty("data.device.exchange.name"));
        rabbitTemplate.setRoutingKey(env.getProperty("data.device.routing.key.name"));

        Message message= MessageBuilder.withBody(objectMapper.writeValueAsBytes(leaveApplyService.get(Long.parseLong("25")))).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        message.getMessageProperties().setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, MessageProperties.CONTENT_TYPE_JSON);
        rabbitTemplate.convertAndSend(message);

        return R.ok();
    }

}
