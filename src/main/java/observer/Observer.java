package observer;

/**
 * Interface Observer
 * @param <T>
 */
public interface Observer<T> {

	/**
	 * Méthode pour mettre à jour notre valeur
	 * @param value (T)
	 */
	void update(T value);

}
