package com.trollCorporation.domain.users;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Stateless
public class UsersFinderImpl implements UsersFinder {
	
	private EntityManager entityManager;
	
	public UsersFinderImpl(){
		EntityManagerFactory emf =
	            Persistence.createEntityManagerFactory("domain");
		this.entityManager = emf.createEntityManager();		
   }
	
	@SuppressWarnings("unchecked")
	public final List<UserDao> getUsers() {
		Query query = entityManager.createNamedQuery("From Users", UserDao.class);
		return query.getResultList();
	}

	public final UserDao findUserById(final int id) {
		try {
			TypedQuery<UserDao> query = entityManager
				.createQuery("From UserDao WHERE id=:id", UserDao.class);
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public final UserDao findUserByUsername(final String username) {
		try {
			TypedQuery<UserDao> query = entityManager
				.createQuery("From UserDao WHERE username=:username", UserDao.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
