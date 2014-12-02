package com.trollCorporation.domain.entityManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@Transactional(value=TxType.SUPPORTS)
public class DomainPersistenceImpl implements DomainPersistence {
	
//	@Autowired
//	private EntityManagerFactory entityManagerFactory;
	@PersistenceContext
	private EntityManager entityManager;
	
	public synchronized EntityManager getEntityManager() {
//		if (entityManager == null) {
//			synchronized (this) {
//				if (entityManager == null) {
//					entityManager = entityManagerFactory.createEntityManager();
//				}
//			}
//		}
		return entityManager;
	}
	
//	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
//		this.entityManagerFactory = entityManagerFactory;
//	}
	
}
