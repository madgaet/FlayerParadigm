package com.trollCorporation.domain.ejb.configurations;

import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class HibernateContext {
	
	private static Logger LOG = Logger.getLogger(HibernateContext.class.getName());
	private static ConfigurableApplicationContext context;
	
	static {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	@SuppressWarnings("unchecked")
	public final static <E> Object getMappedImpl(final String namedBean) {
		try {
			return (E) context.getBean(namedBean);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
