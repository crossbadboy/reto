package cuenta_movimientos.com.adapter.in.messaging;

import cuenta_movimientos.com.config.RabbitMQConfig;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Service
public class ClienteConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void recibirMensajeCliente(Object mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
    }
}
