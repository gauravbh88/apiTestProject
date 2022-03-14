package com.testAssignmentDnb.resources;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class UtilityClass {



	public static RequestSpecification requestSpec;
	public static ResponseSpecification responseSpec;
	public static ExtentTest test;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;


	@BeforeSuite

	public void beforeSuite() {

		htmlReporter = new ExtentHtmlReporter("TestReport.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		

	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, "Test failed " + result.getThrowable());
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, "Test skipped " + result.getThrowable());
		} else {
			test.log(Status.PASS, "Test passed");
		}

	}

	@AfterSuite
	public void tearDown(){
	    extent.flush();
	   }
	
	public static RequestSpecification requestSpecification() throws IOException
	{

		if(requestSpec==null)
		{
			
			{
				PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
				requestSpec = new  RequestSpecBuilder()
						.setContentType("application/json")
						.setBaseUri(getGlobalValue("baseUrl")).addFilter(RequestLoggingFilter.logRequestTo(log))
						 .addFilter(ResponseLoggingFilter.logResponseTo(log))
						.build();
				
				
				 return requestSpec;
				 
				 
		
			}
			
			
			
		}
		return requestSpec;
		
		
	}
	
	public static ResponseSpecification responseSpecification() throws IOException
	{

				responseSpec = new ResponseSpecBuilder()
						  .expectStatusCode(200)
						  .expectContentType(ContentType.JSON)
						  .build();
			
				return responseSpec;
		
	}
	
	
	public static HashMap<String, String> createBody() {
		
		Random random = new Random();
		String name = "TestUser" + random.nextInt();
		
		byte[] array = new byte[7]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String defaultpassword = new String(array, Charset.forName("UTF-8"));
		HashMap<String,String> postContent = new HashMap<String, String>();
		postContent.put("name",name);
		postContent.put("email",name+"@gmail.com");
		postContent.put("password",defaultpassword);
		return postContent;
	}
	
	
	public static String getGlobalValue(String key) throws IOException
	{
		Properties prop =new Properties();
		FileInputStream fis= new FileInputStream("src/main/resources/global.properties");
		prop.load(fis);
		return prop.getProperty(key);
	
		
		
	}
	
	public String getJsonPath(Response response,String key)
	{
		  String resp=response.asString();
		JsonPath   js = new JsonPath(resp);
		return js.get(key).toString();
	}
	
	
}
