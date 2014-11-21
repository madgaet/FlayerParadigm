package com.trollCorporation.domain.users;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.domain.entityManager.DomainPersistenceImpl;

@Local(UsersDao.class)
@Stateless(name="UsersPersistence")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsersDaoImpl extends DomainPersistenceImpl implements UsersDao {
	
	public UsersDaoImpl(){
   }
	
	@SuppressWarnings("unchecked")
	public final List<UserEntity> getUsers() {
		Query query = getEntityManager().createNamedQuery("From Users", UserEntity.class);
		return query.getResultList();
	}

	public final UserEntity findUserById(final int id) {
		try {
			TypedQuery<UserEntity> query = getEntityManager()
				.createQuery("From UserDao WHERE id=:id", UserEntity.class);
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public final UserEntity findUserByUsername(final String username) {
		try {
			TypedQuery<UserEntity> query = getEntityManager()
				.createQuery("From UserEntity WHERE username=:username", UserEntity.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public synchronized void register(UserEntity user) throws RegistrationException {
		try {
			getEntityManager().persist(user);
		} catch (Exception e) {
			throw new RegistrationException(e.getMessage(), e);
		}
	}

}
