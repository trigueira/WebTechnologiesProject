package com.ait.webtechnologies.project.learnon.test.rest;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ait.webtechnologies.project.learnon.data.CourseDAO;
import com.ait.webtechnologies.project.learnon.model.Course;
import com.ait.webtechnologies.project.learnon.rest.CourseWS;
import com.ait.webtechnologies.project.learnon.rest.JaxRsActivator;
import com.ait.webtechnologies.project.learnon.test.UtilsDAO;


@RunWith(Arquillian.class)
public class CourseWSTest {

	@EJB
	private CourseWS courseWS;

	@EJB
	private CourseDAO courseDAO;
	
	@EJB
	private UtilsDAO utilsDAO;

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(JavaArchive.class, "Test.jar")
				.addClasses(CourseDAO.class, Course.class, JaxRsActivator.class, CourseWS.class, UtilsDAO.class)
				.addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Before
	public void setUp() {
		utilsDAO.deleteCourseTable();
		Course course = new Course();
		course.setTitle("testing title");
		course.setDuration(4);
		course.setPrice(5.00);
		course.setInstructor("testing instructor");
		course.setSubject("testing subject");
		course.setDescription("testing description");
		course.setType("testing type");
		course.setLevel("testing level");
		course.setImage("testing image");
		courseDAO.saveCourse(course);
		course = new Course();
		course.setTitle("testing title 2");
		course.setDuration(8);
		course.setPrice(8.00);
		course.setInstructor("testing instructor");
		course.setSubject("testing subject");
		course.setDescription("testing description");
		course.setType("testing type");
		course.setLevel("testing level");
		course.setImage("testing image");
		courseDAO.saveCourse(course);
	}

	@Test
	public void testFindAllCourses() {
		Response response=courseWS.findAllCourses();
		List<Course> courseList = (List<Course>) response.getEntity();
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertEquals("Data fetch = data persisted", courseList.size(), 2);
		Course course=courseList.get(0);
		assertEquals("testing title",course.getTitle());
		course=courseList.get(1);
		assertEquals("testing title 2",course.getTitle());
	}
	
	@Test
	public void testFindCourseById() {
		Response response  = courseWS.findCourseById(1);
		Course course = (Course) response.getEntity();
		assertEquals(1, course.getId());
		assertEquals("testing title", course.getTitle());
		assertEquals(4, course.getDuration());
		assertEquals(5.00, course.getPrice(), 2);
		assertEquals("testing instructor", course.getInstructor());
		assertEquals("testing subject", course.getSubject());
		assertEquals("testing description", course.getDescription());
		assertEquals("testing type", course.getType());
		assertEquals("testing level", course.getLevel());
		assertEquals("testing image", course.getImage());
		response  = courseWS.findCourseById(2);
		course = (Course) response.getEntity();
		assertEquals(2, course.getId());
		assertEquals("testing title 2", course.getTitle());
		assertEquals(8, course.getDuration());
		assertEquals(8.00, course.getPrice(), 2);
		assertEquals("testing instructor", course.getInstructor());
		assertEquals("testing subject", course.getSubject());
		assertEquals("testing description", course.getDescription());
		assertEquals("testing type", course.getType());
		assertEquals("testing level", course.getLevel());
		assertEquals("testing image", course.getImage());
		
		response=courseWS.findCourseById(3);
		String whenFail = (String) response.getEntity();
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
		assertEquals("Entity not found for ID: 3", whenFail);
	}
	
	@Test
	public void testSaveCourse() {
		Course course = new Course();
		course.setTitle("testing title");
		course.setDuration(4);
		course.setPrice(5.00);
		course.setInstructor("testing instructor");
		course.setSubject("testing subject");
		course.setDescription("testing description");
		course.setType("testing type");
		course.setLevel("testing level");
		course.setImage("testing image");
		Response response = courseWS.saveCourse(course);
		assertEquals(HttpStatus.SC_CREATED, response.getStatus());
		course = (Course) response.getEntity();
		assertEquals(3, course.getId());
		assertEquals("testing title", course.getTitle());
		assertEquals(4, course.getDuration());
		assertEquals(5.00, course.getPrice(), 2);
		assertEquals("testing instructor", course.getInstructor());
		assertEquals("testing subject", course.getSubject());
		assertEquals("testing description", course.getDescription());
		assertEquals("testing type", course.getType());
		assertEquals("testing level", course.getLevel());
		assertEquals("testing image", course.getImage());
	}
	
	@Test
	public void testDeleteCourse() {
		Response response=courseWS.findAllCourses();
		List<Course> courseList = (List<Course>) response.getEntity();
		assertEquals(courseList.size(), 2);
		courseWS.deleteCourse(2);
		response=courseWS.findAllCourses();
		courseList = (List<Course>) response.getEntity();
		assertEquals(courseList.size(), 1);
		response=courseWS.deleteCourse(2);
		String whenFail = (String) response.getEntity();
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
		assertEquals("Delete not applied due inexistent entity for id 2", whenFail);
	}
	
	@Test
	public void testUpdateCourse() {
		Response response=courseWS.findCourseById(2);
		Course course = (Course) response.getEntity();
		course.setTitle("NEW TITLE");
		response=courseWS.updateCourse(2, course);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		course = (Course) response.getEntity();
		assertEquals(course.getTitle(), "NEW TITLE");
		assertEquals(course.getInstructor(), "testing instructor");
		
		response=courseWS.updateCourse(3, course);
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
		String whenFail = (String) response.getEntity();
		assertEquals("Update not possible due entity not found for id 3", whenFail);
		
		course.setId(5);
		response=courseWS.updateCourse(1, course);
		assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus());
		whenFail = (String) response.getEntity();
		assertEquals("Entity id (5) not match with path id (1)", whenFail);
	}
	
	@Test
	public void testFindMostPopularSubjects() {
		Response response=courseWS.findMostPopularSubjects();
		List<String> subjectList = (List<String>) response.getEntity();
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertEquals("Data fetch = data persisted", subjectList.size(), 1);
		String subjectPopular =subjectList.get(0);
		assertEquals("testing subject", subjectPopular);
	}
	
	@Test
	public void testFindAveragePaidCourses() {
		Response response=courseWS.findAveragePaidCourses();
		Double averagePrice = (Double) response.getEntity();
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertEquals(6.50, averagePrice, 2);
	}
	
	@Test
	public void testFindNumberPaidCourses() {
		Response response=courseWS.findNumberPaidCourses();
		BigInteger quantity = (BigInteger) response.getEntity();
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertEquals( BigInteger.valueOf(2), quantity);
	}
	
	@Test
	public void testFindNumberFreeCourses() {
		Response response=courseWS.findNumberFreeCourses();
		BigInteger quantity = (BigInteger) response.getEntity();
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertEquals(BigInteger.valueOf(0), quantity);
	}

}
