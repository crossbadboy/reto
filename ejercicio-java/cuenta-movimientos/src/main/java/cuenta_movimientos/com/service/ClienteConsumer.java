package cuenta_movimientos.com.service;

import cuenta_movimientos.com.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ClienteConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void recibirMensajeCliente(Object mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
    }
}
