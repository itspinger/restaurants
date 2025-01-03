package raf.rs.notification;

import org.apache.activemq.broker.BrokerService;

public class MessageBroker {

    public static void main(String[] args) throws Exception {
        final BrokerService broker = new BrokerService();

        // configure the broker
        broker.addConnector("tcp://localhost:61616");
        broker.start();

        while (true) {
            Thread.sleep(Integer.MAX_VALUE);
        }
    }

}
