package com.ait.webtechnologies.project.learnon.test;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class UtilsDAO {

	@PersistenceContext
	private EntityManager em;
	
	public void deleteUserTable() {
		em.createQuery("DELETE FROM User").executeUpdate();
	}

	public void deleteCourseTable() {
		em.createQuery("DELETE FROM Course").executeUpdate();
		em.createNativeQuery("ALTER TABLE courses AUTO_INCREMENT=1").executeUpdate();
	}
}