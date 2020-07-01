package com.ait.webtechnologies.project.learnon.data;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ait.webtechnologies.project.learnon.model.Course;

@Stateless
@LocalBean
public class CourseDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
    public List<Course> getAllCourses() {
        Query query = entityManager.createQuery("SELECT c FROM Course c");
        return query.getResultList();
    } 
    
    public Course getCourse(int id) {
        return entityManager.find(Course.class, id);
    } 
    
    public void saveCourse(Course course) {
    	entityManager.persist(course);
    } 
    
    public void updateCourse(Course course) {
    	entityManager.merge(course);
    }
    
    public void deleteCourse(int id) {
    	entityManager.remove(getCourse(id));
    } 
    
    public List<String> getMostPopularSubjects() {
        Query query = entityManager.createNativeQuery("select  x.subject from (select u.subject, count(*) as subject_count,     rank() over (order by count(*) desc) as ranks   from     courses u   group by     u.subject) x where   x.ranks = 1;");
        return query.getResultList();
    }
    
    public Double getAveragePaidCourses() {
    	Query query = entityManager.createNativeQuery("select avg(price) from courses where price >0;");
    	return (Double) query.getSingleResult();
    } 
    
    public BigInteger getNumberPaidCourses() {
    	Query query = entityManager.createNativeQuery("select count(1) from courses where price >0;");
    	return (BigInteger) query.getSingleResult();
    } 
    
    public BigInteger getNumberFreeCourses() {
    	Query query = entityManager.createNativeQuery("select count(1) from courses where price =0;");
    	return (BigInteger) query.getSingleResult();
    } 
}