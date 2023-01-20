package observer;

/**
 * SampleSpectator implements Observer
 */
public class SampleSpectator implements Observer<Integer> {

	/**
	 * Notre valeur
	 */
	private Integer value = 0;
	/**
	 * On regardde si on est notifié (faux par défaut)
	 */
	private boolean notified = false;
	
	/**
	 * On met à jour notre valeur et notre notification
	 */
	@Override
	public void update(Integer value) {
		this.value = value;
		notified = true;
	}

	/**
	 * On regarde si on est notifié
	 * @return notified (boolean)
	 */
	public boolean wasNotified() {
		return this.notified;
	}
	
	/**
	 * On recupère notre valeur
	 * @return value (Integer)
	 */
	public Integer getValue() {
		return value;
	}

}
