package com.excilys.mviegas.speccdb.persistence.jdbc;

import com.excilys.mviegas.speccdb.persistence.ICrudService;

import javax.transaction.Transactional;

/**
 * Classe générique de Bean de DAO
 *
 * Created by excilys on 14/04/16.
 */
@Transactional
public abstract class AbstractGenericCrudServiceBean<T> implements ICrudService<T> {
}
