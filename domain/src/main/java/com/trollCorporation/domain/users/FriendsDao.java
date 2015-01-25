package com.trollCorporation.domain.users;

import java.util.List;

import com.trollCorporation.domain.exceptions.DbConnectionException;

public interface FriendsDao {
	
	FriendEntity findByNames(String name, String name2) throws DbConnectionException;
	List<FriendEntity> getFriends(String username) throws DbConnectionException;
	List<FriendEntity> getFriendsRequests(String username) throws DbConnectionException;
	
	void register(FriendEntity friend);
	FriendEntity update(FriendEntity friend);
	void delete(FriendEntity friend);
}
