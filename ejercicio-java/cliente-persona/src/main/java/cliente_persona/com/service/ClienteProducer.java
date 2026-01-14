package cliente_persona.com.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClienteProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${cliente.rabbitmq.exchange}")
    private String exchange;

    @Value("${cliente.rabbitmq.routing-key}")
    private String routingKey;

    public ClienteProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensajeCliente(String mensaje) {
        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje);
    }
}
