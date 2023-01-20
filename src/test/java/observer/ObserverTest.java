package observer;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Observer
 */
class ObserverTest {

	/**
	 * On regarde si un sujet est observé ou pas
	 */
	@Test
	public void sujetSenseEtreObservable() {
		@SuppressWarnings("unused")
		Observable<Integer> subject = new SampleSubject();
	}
	
	/**
	 * On regarde si un spectateur est observé
	 */
	@Test
	public void spectatorDoitEtreObserve() {
		Observer<Integer> observer = new SampleSpectator();
		observer.update(5);
	}
	
	/**
	 * Si un appel à la méthode update est fait alors on notifie le spectateur 
	 */
	@Test
	public void spectateurDoitEtreNotifieParUnAppelUpdate() {
		SampleSpectator observer = new SampleSpectator();
		observer.update(5);
		assertTrue(observer.wasNotified());
	}
	
	/**
	 * On regarde si on ne notifie pas un observer sans appeler la méhode update
	 */
	@Test
	public void spectateurNeDoitPasEtreNotifieSansAppelUpdate() {
		SampleSpectator observeur = new SampleSpectator();
		assertFalse(observeur.wasNotified());
	}
	
	/**
	 * On regarde si on arrive bien à attaché et à détaché un observer 
	 */
	@Test
	public void oberserverDevraitEtreAttacheDetache() {
		Observable<Integer> observable = new SampleSubject();
		Observer<Integer> observer = new SampleSpectator();
		observable.attach(observer);
		observable.detach(observer);		
	}
	
	/**
	 * On regarde si on ne notifie pas les observer detachés
	 */
	@Test
	public void onNeDevraitPasNotifierLesObserverDetache() {
		Observable<Integer> subject = new SampleSubject();
		subject.notifyObservers(5);
		assertFalse(new SampleSpectator().wasNotified());
	}
	
	/**
	 * On regarde si on notifie bien un observer attaché
	 */
	@Test
	public void onDevraitNotifiesLesObserversAttache() {
		Observable<Integer> subject = new SampleSubject();
		SampleSpectator spectator = new SampleSpectator();
		subject.attach(spectator);
		subject.notifyObservers(7);
		assertTrue(spectator.wasNotified());
	}
	
	/**
	 * On regarde si on notifie bien tous les observers attachés
	 */
	@Test
	public void onDevraitNotifierToutLesObserversAttache() {
		Observable<Integer> subject = new SampleSubject();
		SampleSpectator spectator1 = new SampleSpectator();
		SampleSpectator spectator2 = new SampleSpectator();
		SampleSpectator spectator3 = new SampleSpectator();
		
		subject.attach(spectator1);
		subject.attach(spectator2);
		subject.attach(spectator3);
		subject.notifyObservers(5);
		
		assertTrue(spectator1.wasNotified());
		assertTrue(spectator2.wasNotified());
		assertTrue(spectator3.wasNotified());
	}
	
	/**
	 * On regarde si on ne notifie pas un observer
	 *  qui s'attache et qui se détache directement
	 */
	@Test
	public void nePasNotifeUnObserverAttachePuisDetache() {
		Observable<Integer> subject = new SampleSubject();
		SampleSpectator spectator = new SampleSpectator();
		
		subject.attach(spectator);
		subject.detach(spectator);
		subject.notifyObservers(5);
		
		assertFalse(spectator.wasNotified());
	}
	
	/**
	 * On regarde bien si on ne notfie pas un spectateur détaché
	 */
	@Test
	public void onNeDoitPasNotifierUnSpectateurDetache() {
		Observable<Integer> subject = new SampleSubject();
		SampleSpectator spectator = new SampleSpectator();
		
		subject.detach(spectator);
		subject.notifyObservers(5);
		
		assertFalse(spectator.wasNotified());
	}
	
	/**
	 * On regarde si on transmet bien la valeur aux notifiés
	 */
	@Test
	public void transmettreDesValeursAuxNotifies() {
		Observable<Integer> subject = new SampleSubject();
		SampleSpectator spectator = new SampleSpectator();
		
		subject.attach(spectator);
		assertEquals(0, spectator.getValue());
		
		subject.notifyObservers(5);
		assertTrue(spectator.wasNotified());
		assertEquals(5, spectator.getValue());
	}

}

