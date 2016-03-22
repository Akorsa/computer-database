package com.excilys.mviegas.speccdb;

import java.io.Serializable;

/**
 * Interface permattant de rendre Identifiable un objet
 *
 * Created by Mickael on 21/03/2016.
 */
public interface Identifiable<PK extends Serializable> extends Serializable {

	/**
	 * @return Id de l'objet
	 */
	PK getID();
}
