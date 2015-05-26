import org.junit.Test;
import junit.framework.TestCase;


public class ActiveMqPracticeTest extends TestCase {

	ActiveMqPractice.HelloWorldConsumer testConsumer = new ActiveMqPractice.HelloWorldConsumer();
	ActiveMqPractice.HelloWorldProducer testProducer = new ActiveMqPractice.HelloWorldProducer();
	ActiveMqPractice.HelloWorldProducer testProducer2 = new ActiveMqPractice.HelloWorldProducer();
	
	@Test
	public void test1() {
		testProducer.run();
		assertTrue(testProducer.lastMessageSent.contains("Hello"));
		
		
	}

	/**@Test
	public void test2() throws InterruptedException {
		ActiveMqPractice.HelloWorldProducer producer1 = new ActiveMqPractice.HelloWorldProducer();
		ActiveMqPractice.HelloWorldConsumer consumer1 = new ActiveMqPractice.HelloWorldConsumer();
        ActiveMqPractice.thread(producer1);
        Thread.sleep(1000);
        ActiveMqPractice.thread(consumer1);
        //Thread.sleep(1000);
		
		
		assertTrue("Message not received", testConsumer.lastMessageReceived.contains("Hello"));
		
	}
	*/
}

