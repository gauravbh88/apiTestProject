package com.testAssignmentDnb.test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.HashMap;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.testAssignmentDnb.resources.UtilityClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


import io.restassured.response.Response;

public class APITests extends UtilityClass {

	HashMap<String, String> postContent;
	String createUserName;
	String createUserId;
	String createUserEmail;
	String password;
	String loginVerificationToken;
	String loginVerificationid;
	String loginVerificationName;
	String loginVerificationEmail;
	

	@Test(priority = 1)
	public void createUser() throws IOException {
		
		test = extent.createTest("Verify createUser api");
		test.log(Status.INFO, "createUsertest started Successfully");
		postContent = createBody();
		
		Response createUserResponse = given().spec(requestSpecification()).body(postContent).when().post("/authaccount/registration")
				.then().spec(responseSpecification()).extract().response();
		
		
		
		test.log(Status.INFO,"Response Code : " + "\n" + createUserResponse.statusCode());
		test.log(Status.INFO,"Response " + "\n" + createUserResponse.asString() );
		 
		
		createUserId = createUserResponse.jsonPath().getString("data.Id");
		createUserName = createUserResponse.jsonPath().getString("data.Name");
		createUserEmail = createUserResponse.jsonPath().getString("data.Email");

	}

	@Test(priority = 2)
	public void loginVerification() throws IOException {

		test = extent.createTest("Verify loginVerification api");
		test.log(Status.INFO, "loginVerificationrTest started Successfully");
		postContent.remove("name");
		Response loginVerificationResponse = given().spec(requestSpecification()).body(postContent).when().post("/authaccount/login").then()
				.spec(responseSpecification()).log().all().extract().response();
		

		test.log(Status.INFO,"Response Code : " + "\n" + loginVerificationResponse.statusCode());
		test.log(Status.INFO,"Response " + "\n" + loginVerificationResponse.asString() );
		
		assertEquals(createUserName, getJsonPath(loginVerificationResponse, "data.Name"));
		assertEquals(createUserEmail, getJsonPath(loginVerificationResponse, "data.Email"));
		assertEquals(createUserId, getJsonPath(loginVerificationResponse, "data.Id"));
		

		loginVerificationToken = loginVerificationResponse.jsonPath().getString("data.Token");
		loginVerificationid = loginVerificationResponse.jsonPath().getString("data.Id");
		loginVerificationName = loginVerificationResponse.jsonPath().getString("data.Name");
		loginVerificationEmail = loginVerificationResponse.jsonPath().getString("data.Email");

		
		
	}

	@Test(priority = 3)
	public void getUserById() throws IOException {

		test = extent.createTest("Verify getUserById api");
		test.log(Status.INFO, "getUserByIdTest started Successfully");
	
		Response getUserByIdResponse = given().spec(requestSpecification()).header("Authorization", "Bearer " + loginVerificationToken).when()
				.get("/users/" + loginVerificationid).then().spec(responseSpecification()).extract().response();
		
		
		test.log(Status.INFO,"Response Code : " + "\n" + getUserByIdResponse.statusCode());
		test.log(Status.INFO,"Response " + "\n" + getUserByIdResponse.asString() );

		
		assertEquals(loginVerificationName, getJsonPath(getUserByIdResponse, "name"));
		assertEquals(loginVerificationEmail, getJsonPath(getUserByIdResponse, "email"));
		assertEquals(loginVerificationid, getJsonPath(getUserByIdResponse, "id"));

		

	}

}
