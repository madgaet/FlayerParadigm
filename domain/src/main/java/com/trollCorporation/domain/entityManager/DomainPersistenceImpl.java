package com.trollCorporation.domain.entityManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DomainPersistenceImpl implements DomainPersistence {
	
	private static final EntityManager entityManager;
	
	static {
		EntityManagerFactory emf =
	            Persistence.createEntityManagerFactory("domain");
		entityManager = emf.createEntityManager();
	}
	
	public synchronized EntityManager getEntityManager() {
		return entityManager;
	}

}
