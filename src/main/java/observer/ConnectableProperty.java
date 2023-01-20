package observer;

/**
 * Notre classe ConnectableProperty
 * @param <T>
 */
public class ConnectableProperty<T> extends ObservableProperty<T> implements Observer<T> {

	/**
	 * Notre valeur (T)
	 */
	private T value;

	/**
	 * Constructeur vide
	 */
	public ConnectableProperty() {
		// Constructeur vide
	}

	/**
	 * On créé notre connectableProperty
	 * 
	 * @param value (T)
	 */
	public ConnectableProperty(T value) {
		setValue(value);
	}

	/**
	 * On met à jour notre valeur
	 * 
	 * @param value (T)
	 */
	@Override
	public void update(T value) {
		setValue(value);
	}

	/**
	 * On lie les propriétés
	 * 
	 * @param property (ConnectableProperty<T>)
	 */
	public void bind(ConnectableProperty<T> property) {
		this.setValue(property.getValue());
		property.attach(this);
	}

	/**
	 * On attache de façon bidirectionnel la property
	 * 
	 * @param property (ConnectableProperty<T>)
	 */
	public void bindBidrectional(ConnectableProperty<T> property) {
		property.setValue(this.getValue());
		property.attach(this);
		this.attach(property);
	}

	/**
	 * On détache notre propriété
	 * 
	 * @param property (ConnectableProperty<T>)
	 */
	public void unbind(ConnectableProperty<T> property) {
		property.detach(this);
	}

	/**
	 * On dettache de façon bidirectionnel les property
	 * 
	 * @param property (ConnectableProperty<T>)
	 */
	public void unbindBidrectional(ConnectableProperty<T> property) {
		property.detach(this);
		this.detach(property);
	}

}
