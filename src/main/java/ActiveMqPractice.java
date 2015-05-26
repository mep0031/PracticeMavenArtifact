import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

/**
 * Simple activeMQ practice
 */
public class ActiveMqPractice {
 
	//Sets up the threads to demonstrate message passing
    public static void main(String[] args) throws Exception {
    	HelloWorldProducer producer1 = new HelloWorldProducer();
    	producer1.p = 1;
    	HelloWorldProducer producer2 = new HelloWorldProducer();
    	producer2.p = 9;
        //thread(new HelloWorldConsumer(), false);
        thread(producer1);
        //Thread.sleep(1000);
        thread(producer2);
        Thread.sleep(1000);
        thread(new HelloWorldConsumer());
        //Thread.sleep(1000);
        
    }
 
    //defining thread
    public static void thread(Runnable runnable) {
        Thread aThread = new Thread(runnable);
        aThread.start();
    }
    //"HelloWorld" topic producer
    public static class HelloWorldProducer implements Runnable {
    	int p = 1; //priority level 
    	String lastMessageSent = null;
    	public void run() {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://HelloFactory");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination Topic
                Destination destination = session.createTopic("Hello.FOO");
 
                // Create a MessageProducer from the Session to the Topic
                MessageProducer producer = session.createProducer(destination);
                producer.setTimeToLive(100000);
                
                //set priority
                producer.setPriority(p);
                
                //producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 
                // Create a messages
                String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);
 
                // Tell the producer to send the message
                System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);
                lastMessageSent = message.getText();
 
                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
                System.out.println("Caught: " + e);
                //e.printStackTrace();
            }
        }
    }
    //"GoodbyeWorld" topic producer
    public static class GoodbyeWorldProducer implements Runnable {
        public void run() {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://GoodbyeFactory");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createTopic("Goodbye.FOO");
 
                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 
                // Create a messages
                String text = "Goodbye world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);
 
                // Tell the producer to send the message
                System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);
 
                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
                System.out.println("Caught: " + e);
                //e.printStackTrace();
            }
        }
    }
    
    //subscriber to the HelloWorld topic
    public static class HelloWorldConsumer implements Runnable, ExceptionListener {
    	String lastMessageReceived = null;
        public void run() {
            try {
 
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://HelloFactory");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                connection.setExceptionListener(this);
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination Topic
                Destination destination = session.createTopic("Hello.FOO");
 
                // Create a MessageConsumer from the Session to the Topic
                MessageConsumer consumer = session.createConsumer(destination);
 
                
                Message message;
                
                do{
                	// Wait for a message
                	message = consumer.receive(1000);
 
                		if(message != null){
                			TextMessage textMessage = (TextMessage) message;
                			String text = textMessage.getText();
                			System.out.println("Hello Consumer Received: " + text);
                			lastMessageReceived = text;
                		}
                }while(message != null);
                
 
                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                //e.printStackTrace();
            }
        }
        
        
        
    
    
 
        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
    }
    //subscriber to the GoodbyeWorld topic
    public static class GoodbyeWorldConsumer implements Runnable, ExceptionListener {
        public void run() {
            try {
 
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://GoodbyeFactory");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                connection.setExceptionListener(this);
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination Topic
                Destination destination = session.createTopic("Goodbye.FOO");
 
                // Create a MessageConsumer from the Session to the Topic
                MessageConsumer consumer = session.createConsumer(destination);
 
                // Wait for a message
                Message message = consumer.receive(1000);
 
                
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Goodbye Consumer Received: " + text);
                
 
                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                //e.printStackTrace();
            }
        }
        
        
        
    
    
 
        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
    }
}