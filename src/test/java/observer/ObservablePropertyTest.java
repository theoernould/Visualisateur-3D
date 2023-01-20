package observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Classe de Test pour la classe ObservableProperty
 */
class ObservablePropertyTest {
	
	/**
	 * On regarse si on observe bien notre observer
	 */
	@Test
	public void devraitEtreObservable() {
		@SuppressWarnings("unused")
		Observable<Integer> observable = new ObservableProperty<>();
	}
	
	/**
	 * On change les propritées observalbes sans changer les observers
	 */
	@Test
	public void onChangeLesProporieteObservableSansObserveur() {
		ObservableProperty<Integer> property = new ObservableProperty<>();
		property.setValue(5);
		assertEquals(5, property.getValue());
	}
	
	/**
	 * On reagrde si on notifie les observers et on change les observableProperty
	 */
	@Test
	public void onChangeObservablePropertyQuiNotifiesLesObservers() {
		ObservableProperty<Integer> property = new ObservableProperty<>();
		SampleSpectator observer = new SampleSpectator();
		property.attach(observer);
		property.setValue(5);
		assertTrue(observer.wasNotified());
	}
	
	/**
	 * On regarde si on ne notifie pas les observers détaché
	 */
	@Test
	public void onChangeObservablePropertyQuiNotifiespasLesObserversDetache() {
		ObservableProperty<Integer> property = new ObservableProperty<>();
		SampleSpectator observer = new SampleSpectator();
		
		property.attach(observer);
		property.detach(observer);
		property.setValue(5);
		assertFalse(observer.wasNotified());
	}
}
