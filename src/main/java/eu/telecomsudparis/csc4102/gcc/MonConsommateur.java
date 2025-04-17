// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc;

import java.util.concurrent.Flow;

public class MonConsommateur implements Flow.Subscriber<String>{
	private Flow.Subscription subscription;
	
	
	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}
	
	@Override
	public void onNext(String item) {
		System.out.println("Notification reçue :" + item);
		subscription.request(1);
	}
	
	@Override public void onError(Throwable throwable) {
		System.err.println("Erreur lors de la notification");
	}
	@Override public void onComplete() {
		System.out.println("Notification teminée ");
	}
}
