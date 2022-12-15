package com.atguigu.srb.sms.receiver;

import com.atguigu.srb.base.dto.SmsDTO;
import com.atguigu.srb.rabbitutil.constant.MQConst;
import com.atguigu.srb.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mch
 * @since 2022/12/15
 */
@Component
@Slf4j
public class SmsReceiver {
    @Resource
    private SmsService smsService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MQConst.QUEUE_SMS_ITEM, durable = "true"),
            exchange = @Exchange(value = MQConst.EXCHANGE_TOPIC_SMS),
            key = {MQConst.ROUTING_SMS_ITEM}))
    public void send(SmsDTO smsDTO) throws Exception {
        log.info("消息监听");
        smsService.send(smsDTO.getMobile(),smsDTO.getMessage());
    }
}
