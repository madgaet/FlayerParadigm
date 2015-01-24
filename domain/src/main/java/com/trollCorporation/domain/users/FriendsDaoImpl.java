package com.trollCorporation.domain.users;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.trollCorporation.domain.entityManager.DomainPersistenceImpl;
import com.trollCorporation.domain.exceptions.DbConnectionException;

public class FriendsDaoImpl extends DomainPersistenceImpl implements FriendsDao {

	public List<FriendEntity> getFriends(final String username) throws DbConnectionException {
		try {
			String stringQuery = "From FriendEntity as friend " 
					+ "WHERE friend.user.username=:username "
					+ "OR friend.userFriend.username=:username "
					+ "AND friend.valid = true";
			TypedQuery<FriendEntity> query = getEntityManager()
				.createQuery(stringQuery, FriendEntity.class);
			query.setParameter("username", username);
			return query.getResultList();
		} catch (PersistenceException e) {
			throw new DbConnectionException("Error while trying to find user in DB",e);
		}
	}
	
	public List<FriendEntity> getFriendsRequests(final String username) throws DbConnectionException {
		try {
			String stringQuery = "From FriendEntity as friend " 
					+ "WHERE friend.user.username=:username "
					+ "OR friend.userFriend.username=:username "
					+ "AND friend.valid = false";
			TypedQuery<FriendEntity> query = getEntityManager()
				.createQuery(stringQuery, FriendEntity.class);
			query.setParameter("username", username);
			return query.getResultList();
		} catch (PersistenceException e) {
			throw new DbConnectionException("Error while trying to find user in DB",e);
		}
	}
	
	public FriendEntity findByNames(final String name, final String name2) 
			throws DbConnectionException {
		try {
			String stringQuery = "From FriendEntity as friend " 
				+ "WHERE (friend.user.username=:name AND friend.userFriend.username=:name2) "
				+ "OR (friend.user.username=:name2 AND friend.userFriend.username=:name) ";
			TypedQuery<FriendEntity> query = getEntityManager()
					.createQuery(stringQuery, FriendEntity.class);
			query.setParameter("name", name);
			query.setParameter("name2", name2);
			return query.getSingleResult();
		} catch (NoResultException n) {
			return null;
		} catch (Exception e) {
			throw new DbConnectionException("error while finding friends", e);
		}
	}
	
	@Transactional(value=TxType.REQUIRED)
	public void register(final FriendEntity friend) {
		getEntityManager().persist(friend);
	}
	
	@Transactional(value=TxType.REQUIRED)
	public void update(final FriendEntity friend) {
		getEntityManager().merge(friend);
	}
	
	@Transactional(value=TxType.REQUIRED)
	public void delete(final FriendEntity friend) {
		getEntityManager().remove(friend);
	}

}
