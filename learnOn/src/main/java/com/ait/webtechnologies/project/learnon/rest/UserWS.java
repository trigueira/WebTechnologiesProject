package com.ait.webtechnologies.project.learnon.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ait.webtechnologies.project.learnon.data.UserDAO;
import com.ait.webtechnologies.project.learnon.model.User;

@Path("/user")
@Stateless
@LocalBean
public class UserWS {

	@EJB
	private UserDAO userDAO; 

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findAllUsers() {
		List<User> users = userDAO.getAllUsers();
		return Response.status(200).entity(users).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{username}")
	public Response findUserByUsername(@PathParam("username") String username) {
		User user = userDAO.getUser(username);
		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for username: " + username)
					.build();
		}
		return Response.status(200).entity(user).build();
	}
	
	@POST
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response loginUser(User user) {
		final List<User> getUsers = userDAO.loginUser(user.getUsername(), user.getPassword());
		if (getUsers.isEmpty()) {
			return Response.status(500).build();
		}
		return Response.status(200).entity(getUsers).build();
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response saveUser(User user) {
	    String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}";
		if(!user.getPassword().matches(pattern)) {
			return Response.status(500).build();
		}
		if(user.getName().matches("[0-9]+") || user.getName().isEmpty()) {
			return Response.status(500).build();
		}
		userDAO.saveUser(user);
		return Response.status(201).entity(user).build();
	}

	@PUT
	@Path("/{username}")
	@Consumes("application/json")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateUser(@PathParam("username") String username, User user) {
		User userUnmodified = userDAO.getUser(username);
		if (userUnmodified == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Update not possible due entity not found for username " + username).build();
		}
		if(user.getName().matches("[0-9]+") || user.getName().isEmpty()) {
			return Response.status(500).build();
		}
		if (!username.equals(user.getUsername())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Entity username (" + user.getUsername() + ") not match with path username (" + username + ")").build();
		}
		user.setPassword(userUnmodified.getPassword());
		userDAO.updateUser(user);
		return Response.status(200).entity(user).build();
	}

	@DELETE
	@Path("/{username}")
	public Response deleteUser(@PathParam("username") String username) {
		if (userDAO.getUser(username) == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Delete not applied due inexistent entity for username " + username).build();
		}
		userDAO.deleteUser(username);
		return Response.status(204).build();
	}
}
