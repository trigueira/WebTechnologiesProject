package com.ait.webtechnologies.project.learnon.test.rest;

import static org.junit.Assert.assertEquals;

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

import com.ait.webtechnologies.project.learnon.data.UserDAO;
import com.ait.webtechnologies.project.learnon.model.User;
import com.ait.webtechnologies.project.learnon.rest.JaxRsActivator;
import com.ait.webtechnologies.project.learnon.rest.UserWS;
import com.ait.webtechnologies.project.learnon.test.UtilsDAO;


@RunWith(Arquillian.class)
public class UserWSTest {

	@EJB
	private UserWS userWS;

	@EJB
	private UserDAO userDAO;
	
	@EJB
	private UtilsDAO utilsDAO;

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(JavaArchive.class, "Test.jar")
				.addClasses(UserDAO.class, User.class, JaxRsActivator.class, UserWS.class, UtilsDAO.class)
				.addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Before
	public void setUp() {
		utilsDAO.deleteUserTable();
		User user = new User();
		user.setUsername("testing username");
		user.setPassword("Password1");
		user.setName("testing name");
		user.setImage("newUser.png");
		userDAO.saveUser(user);
		user = new User();
		user.setUsername("testing username 2");
		user.setPassword("Password1");
		user.setName("testing name");
		user.setImage("newUser.png");
		userDAO.saveUser(user);
	}

	@Test
	public void testFindAllUsers() {
		Response response=userWS.findAllUsers();
		List<User> userList = (List<User>) response.getEntity();
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		assertEquals("Data fetch = data persisted", userList.size(), 2);
		User user=userList.get(0);
		assertEquals("testing username",user.getUsername());
		user=userList.get(1);
		assertEquals("testing username 2",user.getUsername());
	}
	
	@Test
	public void testFindUserByUsername() {
		Response response  = userWS.findUserByUsername("testing username");
		User user = (User) response.getEntity();
		assertEquals("testing name", user.getName());
		assertEquals("testing username", user.getUsername());
		assertEquals("Password1", user.getPassword());
		assertEquals("newUser.png", user.getImage());
		response  = userWS.findUserByUsername("testing username 2");
		user = (User) response.getEntity();
		assertEquals("testing name", user.getName());
		assertEquals("testing username 2", user.getUsername());
		assertEquals("Password1", user.getPassword());
		assertEquals("newUser.png", user.getImage());
		
		response=userWS.findUserByUsername("username");
		String whenFail = (String) response.getEntity();
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
		assertEquals("Entity not found for username: username", whenFail);
	}
	
	@Test
	public void testSaveUser() {
		User user = new User();
		user.setUsername("testing username 3");
		user.setPassword("Password1");
		user.setName("testing name");
		user.setImage("newUser.png");
		Response response = userWS.saveUser(user);
		assertEquals(HttpStatus.SC_CREATED, response.getStatus());
		user = (User) response.getEntity();
		assertEquals("testing name", user.getName());
		assertEquals("testing username 3", user.getUsername());
		assertEquals("Password1", user.getPassword());
		assertEquals("newUser.png", user.getImage());

		user = new User();
		user.setUsername("testing username 4");
		user.setPassword("Password1");
		user.setName("12345");
		user.setImage("newUser.png");
		response = userWS.saveUser(user);
		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus());
		
		user = new User();
		user.setUsername("testing username 4");
		user.setPassword("testing innapropriate password");
		user.setName("testing name");
		user.setImage("newUser.png");
		response = userWS.saveUser(user);
		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testDeleteUser() {
		Response response=userWS.findAllUsers();
		List<User> userList = (List<User>) response.getEntity();
		assertEquals(userList.size(), 2);
		userWS.deleteUser("testing username");
		response=userWS.findAllUsers();
		userList = (List<User>) response.getEntity();
		assertEquals(userList.size(), 1);
		response=userWS.deleteUser("testing username");
		String whenFail = (String) response.getEntity();
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
		assertEquals("Delete not applied due inexistent entity for username testing username", whenFail);
	}
	
	@Test
	public void testUpdateUser() {
		Response response=userWS.findUserByUsername("testing username");
		User user = (User) response.getEntity();
		user.setName("NEW NAME");
		response=userWS.updateUser("testing username", user);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		user = (User) response.getEntity();
		assertEquals(user.getName(), "NEW NAME");
		assertEquals(user.getImage(), "newUser.png");
		
		response=userWS.updateUser("testing username 5", user);
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
		String whenFail = (String) response.getEntity();
		assertEquals("Update not possible due entity not found for username testing username 5", whenFail);
	
		user.setUsername("testing username 100");
		response=userWS.updateUser("testing username", user);
		assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus());
		whenFail = (String) response.getEntity();
		assertEquals("Entity username (testing username 100) not match with path username (testing username)", whenFail);
		
		user.setName("12345");
		response = userWS.updateUser("testing username", user);
		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testLoginUser() {
		Response response=userWS.findUserByUsername("testing username");
		User user = (User) response.getEntity();
		response=userWS.loginUser(user);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
		
		user = new User();
		response=userWS.loginUser(user);
		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus());
	}
}
