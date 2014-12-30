package com.trollCorporation.domain.entityManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@Transactional(value=TxType.SUPPORTS)
public class DomainPersistenceImpl implements DomainPersistence {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public synchronized EntityManager getEntityManager() {
		return entityManager;
	}
	
}
