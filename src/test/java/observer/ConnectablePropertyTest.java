package observer;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la Classe ConnectableProperty
 */
class ConnectablePropertyTest {

	 /**
	  * On regarde si notre ObservableProperty est initialisé
	  */
	 @Test
	 public void devrait_etre_observe() {
		 @SuppressWarnings("unused")
		ObservableProperty<String> observable = new ConnectableProperty<>();
	 }
	 
	 /**
	  * On regarde si notre Observer est initialisé
	  */
	 @Test
	 public void devrait_etre_un_observer() {
		 @SuppressWarnings("unused")
		Observer<String> observer = new ConnectableProperty<>();
	}
	 
	 /**
	  * On regarde si on initialise bien la valeur dans le constructeur
	  */
	 @Test
	 public void on_devrait_initialiser_la_valeur_dans_le_constructeur() {
		 ConnectableProperty<Integer> property1 = new ConnectableProperty<>(42);
		 assertEquals(42, property1.getValue());
	 }
	 
	 /**
	  * On regarde si on chnage bien la valeur des observers connectés
	  */
	 @Test
	 public void devraitChangerLaValeurDesObserveurConnecte() {
		 ConnectableProperty<Integer> property1 = new ConnectableProperty<>(42);
		 ConnectableProperty<Integer> property2 = new ConnectableProperty<>();
		 property2.bind(property1);
		 assertEquals(42, property2.getValue());
	 }
	 
	 /**
	  * On regarde si on transmet la valeur aux observeurs connecté 
	  */
	 @Test
	 public void onDevraitTransmettreLaValeurAuxObserverConnectes() {
		 ConnectableProperty<Integer> property1 = new ConnectableProperty<>();
		 ConnectableProperty<Integer> property2 = new ConnectableProperty<>();
		 
		 property1.bind(property2);
		 property2.setValue(5);
		 
		 assertEquals(5, property1.getValue());
		 assertEquals(5, property2.getValue());
	 }
	 
	 /**
	  * Si un observer n'est pas connecté à un
	  *  autre alors il ne modifie pas la valeur
	  */
	 @Test
	 public void connectNeTransmetPasLaValeurDunObserverConnecte() {
		 ConnectableProperty<Integer> property1 = new ConnectableProperty<>();
		 ConnectableProperty<Integer> property2 = new ConnectableProperty<>(42);
		 
		 property1.bind(property2);
		 assertEquals(42, property2.getValue());
		 
		 property1.setValue(5);
		 assertEquals(42, property2.getValue());
		 
	 }
	 
	 /**
	  * On regarde si notre première propiété transmet bien ça valeur à la deuxième
	  */
	 @Test
	 public void bindDirectionnelInitValueDepuisLePremier() {
		 ConnectableProperty<Integer> property1 = new ConnectableProperty<>(42);
		 ConnectableProperty<Integer> property2 = new ConnectableProperty<>(14);
		 
		 property1.bindBidrectional(property2);
		 
		 assertEquals(42, property1.getValue());
		 assertEquals(42, property2.getValue());
	 }
	 
	 /**
	  * On regarde si on a une connexion bidirectionnelle 
	  * et on regarde si on transmet la valeur dans les 2 cas
	  */
	 @Test
	 public void bindDirectionnelTransmetLaValeurDansLes2Cas() {
		 ConnectableProperty<Integer> property1 = new ConnectableProperty<>();
		 ConnectableProperty<Integer> property2 = new ConnectableProperty<>();
		 
		 property1.bindBidrectional(property2);
		 property1.setValue(42);
		 
		 assertEquals(42, property1.getValue());
		 assertEquals(42, property2.getValue());
		 
		 property2.setValue(5);
		 assertEquals(5, property1.getValue());
		 assertEquals(5, property2.getValue());
		 
		 property1.setValue(12);
		 assertEquals(12, property1.getValue());
		 assertEquals(12, property2.getValue());
		 
	 }
	 
	 /**
	  * On regarde si on a pas de boucle infinie 
	  * avec plusieur ConnectableProperty
	  */
	 @Test
	 public void circulaireConnectMaisSansBoucle() {
		 ConnectableProperty<Integer> property1 = new ConnectableProperty<>();
		 ConnectableProperty<Integer> property2 = new ConnectableProperty<>();
		 ConnectableProperty<Integer> property3 = new ConnectableProperty<>();
		 
		 property1.bindBidrectional(property2);
		 property2.bindBidrectional(property3);
		 property3.bindBidrectional(property1);
		 
		 
		 property2.setValue(5);
		 
		 assertEquals(5, property1.getValue());
		 assertEquals(5, property2.getValue());
		 assertEquals(5, property3.getValue());
		 
		 property1.setValue(42);
		 
		 assertEquals(42, property1.getValue());
		 assertEquals(42, property2.getValue());
		 assertEquals(42, property3.getValue());
		 
		 property3.setValue(17);
		 
		 assertEquals(17, property1.getValue());
		 assertEquals(17, property2.getValue());
		 assertEquals(17, property3.getValue());
	 }
	 
	 /**
	  * On regarde si on déconnecte bien les propriétés 
	  * et qu'on ne transmet pas les valeurs si c'est le cas
	  */
	 @Test
	 public void deconnecterDesPropertyNeTransmetPasLesValeurs() {
		 ConnectableProperty<Integer> property1 = new ConnectableProperty<>();
		 ConnectableProperty<Integer> property2 = new ConnectableProperty<>(5);
		 
		 property1.bind(property2);
		 assertEquals(5, property1.getValue());
		 
		 property1.unbind(property2);
		 
		 property2.setValue(42);
		 assertEquals(5, property1.getValue());
	 }
	 
	 /**
	  * On regarde si on déconnecte bien les propriétés 
	  * et qu'on ne transmet pas les valeurs si c'est le cas
	  * Avec une connexion bidirectionnelle 
	  */
	 @Test
	 public void deconnecterPropertyNeTransmetPasLesValeurs() {
		 ConnectableProperty<Integer> property1 = new ConnectableProperty<>(5);
		 ConnectableProperty<Integer> property2 = new ConnectableProperty<>();
		 
		 property1.bindBidrectional(property2);
		 
		 property1.unbindBidrectional(property2);
		 
		 property2.setValue(42);
		 assertEquals(5, property1.getValue());
		 assertEquals(42, property2.getValue());
		 
		 property1.setValue(7);
		 assertEquals(7, property1.getValue());
		 assertEquals(42, property2.getValue());
	 }

}
