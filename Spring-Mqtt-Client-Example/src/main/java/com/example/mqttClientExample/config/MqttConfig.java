package com.example.mqttClientExample.config;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.*;

@Configuration
public class MqttConfig {

    private final static String MQTT_SERVER = "tcp://localhost:1883";
    private final static String ROOT_TOPIC = "#";

    /**
     * Mqtt Connect Option 설정
     */
    @Bean
    public MqttConnectOptions mqttConnectOptions(){
        MqttConnectOptions connectOptions = new MqttConnectOptions();

        // 커넥션 실패시 재연결 설정값
        connectOptions.setAutomaticReconnect(true);
        // 연결할 Mqtt Server URLs
        connectOptions.setServerURIs(new String[]{MQTT_SERVER});

        // mqtt Server 인증 접속 설정
        connectOptions.setUserName("");
        connectOptions.setPassword("".toCharArray());

        // 케넥션 세션 유지 여부 설정
        connectOptions.setCleanSession(true);

        // 커넥션 유지 설정
        connectOptions.setKeepAliveInterval(100);
        // 커넥션 타임 아웃 설정
        connectOptions.setConnectionTimeout(10);

        //TODO : 기타 옵션은 MqttConnectOptions 참고 하여 설정

        return connectOptions;
    }

    /**
     * Mqtt Client Factory 설정
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory(){
        DefaultMqttPahoClientFactory mqttClientFactory = new DefaultMqttPahoClientFactory();
        // Client Factory 커넥션 옵션 설정
        mqttClientFactory.setConnectionOptions(mqttConnectOptions());

        return mqttClientFactory;
    }

    /**
     * Client 설정
     */
    @Bean
    public IMqttClient mqttClient() throws MqttException {
        MqttPahoClientFactory factory = mqttClientFactory();
        return factory.getClientInstance(MQTT_SERVER, "Client-ID");
    }


    // 아래는 채널 및 Topic 의존성 설정
    /**
     * 메세지 채널 의존성 주입
     * DirectChannel
     * QueueChannel
     * PriorityChannel
     * PublishSubscribeChannel
     * RendezvousChannel
     * ExecutorChannel
     * FluxMessageChannel
     * ReactiveStreamsSubscribableChannel
     */
    @Bean
    public MessageChannel messageChannel() {
        return new DirectChannel();
    }


    /**
     * MessageProducer 의존성 주입
     * MqttPahoMessageDrivenChannelAdapter
     * MqttPahoMessageHandler3
     * MqttPahoOutboundChannelAdapter
     * MqttPahoMessageHandler + MqttPahoOutboundChannelAdapter
     */
    @Bean
    public MessageProducer messageProducer() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("Clint-ID", this.mqttClientFactory(), ROOT_TOPIC);

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel( messageChannel() );
        return adapter;
    }


    /**
     * Subscript MessageHandler 의존성
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler subscriptHandler() {
        return message -> {
            // TODO
            MessageHeaders headerTopic = message.getHeaders();
            System.out.println("subscriptHandler : " + headerTopic);
        };
    }



    /**
     * Publish MessageHandler 의존성 주입
     * -> MqttChannel InterFace
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler publishHandler() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler("Client-ID", this.mqttClientFactory());

        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(ROOT_TOPIC);

        return message -> {
            messageHandler.handleMessage(message);

        };
    }


    /**
     * Mqtt publisher Handler 설정
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel", outputChannel = "mqttOutboundChannel")
    public MessageHandler inOutHandler() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("Client-ID", this.mqttClientFactory());

        // set async
        messageHandler.setAsync(true);
        // set default qos
        messageHandler.setDefaultQos(0);
        // default setting topic is wild all #
        messageHandler.setDefaultTopic(ROOT_TOPIC);

        messageHandler.setDefaultRetained(true);
        return messageHandler;
    }


}
