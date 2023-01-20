package observer;

/**
 * ObservableProperty class extends Observable
 * 
 * @param <T>
 */
public class ObservableProperty<T> extends Observable<T> {
	
	/**
	 * Notre valeur à mettre jour (T)
	 */
	private T value;
	
	/**
	 * On recupère notre valeur
	 * @return value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * On met à jour notre valeur
	 * @param value (T)
	 */
	public void setValue(T value) {
		if(shouldUpdate(value)) {
			this.value = value;
			notifyObservers(value);
		}
	}
	
	/**
	 * On regarde si c'est update
	 * @param value (T)
	 * @return si la valeur est mise à jour alors true sinon false
	 */
	private boolean shouldUpdate(T value) {
		if(this.value == null && value == null) return false;
		if(this.value == null) return true;
		return !this.value.equals(value);
	}

}
