
package com.sample.tests;

import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.jayway.restassured.response.Response;
import com.sample.resources.User;

public class ApiTest {

	private final String url = "http://jsonplaceholder.typicode.com";
	private final String basePath = "/posts";
	private final Gson gson = new Gson();

	@BeforeClass
	public void configureRest() {
		RestAssured.baseURI = url;
		RestAssured.basePath = basePath;
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json; charset=utf-8")
				.setAccept("application/json; charset=utf-8").build();
		RestAssured.config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON));
	}

	@Test
	public void testGetUsers() {
		final Response resp = given().when().get();
		final List<User> users = gson.fromJson(resp.asString(), new TypeToken<List<User>>() {
		}.getType());
		assertFalse(users.isEmpty(), "Response shouldn't be empty");
	}

	@Test
	public void testGetUser() {
		final Integer id = 2;
		final Response resp = given().when().get("/" + id);

		final String actualId = resp.jsonPath().getString("id");
		assertEquals(id, Integer.valueOf(actualId), "Id didn't match");

		// or

		final User userResponse = resp.as(User.class);
		assertEquals(id, userResponse.getId(), "Id didn't match");
	}

	@Test
	public void testCreateUser() {

		final String title = "Doctor";

		final User user = new User();
		user.setTitle(title);
		user.setUserId(101);

		final Response resp = given().when().content(user).post();

		final User userResponse = resp.as(User.class);
		assertEquals(title, userResponse.getTitle(), "Title didn't match");
	}
}
