package com.ait.webtechnologies.project.learnon.rest;

import java.math.BigInteger;
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

import com.ait.webtechnologies.project.learnon.data.CourseDAO;
import com.ait.webtechnologies.project.learnon.model.Course;

@Path("/course")
@Stateless
@LocalBean
public class CourseWS {

	@EJB
	private CourseDAO courseDAO;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findAllCourses() {
		List<Course> course = courseDAO.getAllCourses();
		return Response.status(200).entity(course).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{id}")
	public Response findCourseById(@PathParam("id") int id) {
		Course course = courseDAO.getCourse(id);
		if (course == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for ID: " + id).build();
		}
		return Response.status(200).entity(course).build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/mostPopular")
	public Response findMostPopularSubjects() {
		List<String> popularSubjects = courseDAO.getMostPopularSubjects();
		return Response.status(200).entity(popularSubjects).build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/averagepaidcourses")
	public Response findAveragePaidCourses() {
		Double average = courseDAO.getAveragePaidCourses();
		return Response.status(200).entity(average).build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/numberpaidcourses")
	public Response findNumberPaidCourses() {
		BigInteger quantity = courseDAO.getNumberPaidCourses();
		return Response.status(200).entity(quantity).build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/numberfreecourses")
	public Response findNumberFreeCourses() {
		BigInteger quantity = courseDAO.getNumberFreeCourses();
		return Response.status(200).entity(quantity).build();
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response saveCourse(Course course) {
		courseDAO.saveCourse(course);
		return Response.status(201).entity(course).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes("application/json")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateCourse(@PathParam("id") int id, Course course) {
		if (courseDAO.getCourse(id) == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Update not possible due entity not found for id " + id).build();
		}
		if (id != course.getId()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Entity id (" + course.getId() + ") not match with path id (" + id + ")").build();
		}
		courseDAO.updateCourse(course);
		return Response.status(200).entity(course).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteCourse(@PathParam("id") int id) {
		if (courseDAO.getCourse(id) == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Delete not applied due inexistent entity for id " + id).build();
		}
		courseDAO.deleteCourse(id);
		return Response.status(204).build();
	}
}