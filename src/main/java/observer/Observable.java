package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable est une classe abstraite
 * @param <T>
 */
public abstract class Observable<T> {

	/**
	 * Notre liste d'observers
	 */
	protected List<Observer<T>> observers = new ArrayList<>();

	/**
	 * On ajoute un observer à la liste
	 * @param observer (Observer<T>)
	 */
	public void attach(Observer<T> observer) {
		observers.add(observer);
	}

	/**
	 * On supprime un observer de la liste
	 * @param observer (Observer<T>)
	 */
	public void detach(Observer<T> observer) {
		observers.remove(observer);

	}

	/**
	 * On notifie les observers de la liste avec la valeur
	 * @param value (T)
	 */
	public void notifyObservers(T value) {
		for (Observer<T> observer : observers) {
			observer.update((T) value);
		}
	}

}
