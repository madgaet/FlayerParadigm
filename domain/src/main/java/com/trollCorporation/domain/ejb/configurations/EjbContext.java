package com.trollCorporation.domain.ejb.configurations;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBContext;

import org.apache.log4j.Logger;

import com.trollCorporation.domain.users.UsersDao;
import com.trollCorporation.domain.users.UsersDaoImpl;

public final class EjbContext {
	
	private static Logger LOG = Logger.getLogger(EJBContext.class.getName());
	private static Map<Object, String> mappedImpl;
	
	static {
		mappedImpl = new HashMap<Object, String>();
		//creation of beans
		mappedImpl.put(UsersDao.class, UsersDaoImpl.class.getName());	
	}
	
	public final static <E> Object getMappedImpl(final String namedClass) {
		try {
			return Class.forName(mappedImpl.get(Class.forName(namedClass))).newInstance();
		} catch (InstantiationException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
