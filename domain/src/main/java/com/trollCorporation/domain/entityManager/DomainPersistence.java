package com.trollCorporation.domain.entityManager;

import javax.persistence.EntityManager;

public interface DomainPersistence {
	
	EntityManager getEntityManager();
}
