package br.com.heiderlopes.iotmqttapi;

import br.com.heiderlopes.iotmqttapi.model.Coisa;
import br.com.heiderlopes.iotmqttapi.service.CoisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@SpringBootApplication
@IntegrationComponentScan
public class IotmqttapiApplication {

	private final String URL_BROKER = "tcp://iot.eclipse.org:1883";
	private final String TOPICO = "heider";
	private final String TOPICO_LED = "ledapicon";
	private final String TOPICO_TEMPERATURA = "temperaturaapicon";
	private final String CHAVE_TOPICO = "mqtt_receivedTopic";
	private final String CLIENT_ID = "clientID";

	private static final String MQTT_OUTBOUND_CHANNEL = "mqttOutboundChannel";
	private static final String MQTT_INPUT_CHANNEL = "mqttInputChannel";

	@Autowired
	CoisaService coisaService;

	public static void main(String[] args) {
		//SpringApplication.run(IotmqttapiApplication.class, args);
		ConfigurableApplicationContext context =
				new SpringApplicationBuilder(IotmqttapiApplication.class)
						.run(args);
		/*MyGateway gateway = context.getBean(MyGateway.class);
		gateway.sendToMqtt("0");*/
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(URL_BROKER);
		return factory;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = MQTT_OUTBOUND_CHANNEL)
	public MqttPahoMessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler =
				new MqttPahoMessageHandler(CLIENT_ID, mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(TOPICO_LED);
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = MQTT_OUTBOUND_CHANNEL)
	public interface MyGateway {
		void sendToMqtt(String valor);
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter =
				new MqttPahoMessageDrivenChannelAdapter(URL_BROKER, CLIENT_ID,
						TOPICO_LED, TOPICO, TOPICO_TEMPERATURA);
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(0);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = MQTT_INPUT_CHANNEL)
	public MessageHandler handler() {
		return message -> {
			Coisa coisa = new Coisa();
			coisa.setDeviceID(message.getHeaders().get(CHAVE_TOPICO).toString());
			coisa.setValor(message.getPayload().toString());
			Coisa aux = coisaService.findBy(coisa.getDeviceID());
			if (aux != null) {
				coisa.setId(coisa.getId());
			}
			coisaService.salvar(coisa);
		};
	}
}
