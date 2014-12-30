package com.trollCorporation.domain.users;

import java.util.Calendar;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Repository;

import com.trollCorporation.common.exceptions.RegistrationException;
import com.trollCorporation.domain.entityManager.DomainPersistenceImpl;
import com.trollCorporation.domain.exceptions.DbConnectionException;

@Repository("UsersDao")
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

	public final UserEntity findUserByUsername(final String username)
			throws DbConnectionException {
		return findUserByUsername(username, 0);
	}
	
	private final UserEntity findUserByUsername(final String username, final int retry)
			throws DbConnectionException {
		try {
			TypedQuery<UserEntity> query = getEntityManager()
				.createQuery("From UserEntity WHERE username=:username", UserEntity.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (JDBCConnectionException e) {
			if (retry < 5) {
				try { Thread.sleep((long)(Math.random()*retry*1000)+125L); }
				catch (InterruptedException e1) {/*do nothing*/}
				return findUserByUsername(username, retry+1);
			} else {
				throw new DbConnectionException("Error while trying to find user in DB",e);
			}
		}
	}
	
	@Transactional(value=TxType.REQUIRED)
	public synchronized void register(UserEntity user) throws RegistrationException {
		try {
			user.setCreationDate(Calendar.getInstance());
			user.setModificationDate(Calendar.getInstance());
			getEntityManager().persist(user);
		} catch (Exception e) {
			throw new RegistrationException(e.getMessage(), e);
		}
	}

}
