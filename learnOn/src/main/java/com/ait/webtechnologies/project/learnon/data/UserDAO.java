package com.ait.webtechnologies.project.learnon.data;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ait.webtechnologies.project.learnon.model.User;

@Stateless
@LocalBean
public class UserDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
    public List<User> getAllUsers() {
        Query query = entityManager.createQuery("SELECT u FROM User u");
        return query.getResultList();
    } 
    
    public User getUser(String username) {
        return entityManager.find(User.class, username);
    } 
    
    public void saveUser(User user) {
    	entityManager.persist(user);
    } 
    
    public void updateUser(User user) {
    	entityManager.merge(user);
    }
    
    public void deleteUser(String username) {
    	entityManager.remove(getUser(username));
    } 
    
	public List<User> loginUser(String username, String password) {
		final String select = "SELECT u FROM User u WHERE u.username=:username and u.password=:password";
		Query query = entityManager.createQuery(select);
		query.setParameter("username", username);
		query.setParameter("password", password);
		return query.getResultList();
	}
}