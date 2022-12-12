package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

public class ReadOneProduct {
	String baseURL;
	SoftAssert softassert;
	
	public ReadOneProduct() {
		baseURL ="https://techfios.com/api-prod/api/product";
		softassert = new SoftAssert();
	}
	
	@Test
	public void readOneProduct() {
	Response response = (Response) given()
		.baseUri(baseURL)
		.header("Content-Type","application/json")
		.auth().preemptive().basic("demo@techfios.com", "abc123.")
		.queryParam("id", "6209").
	when()
		.get("/read_one.php").
	then()
	//.log().all()
	   .extract().response();
	
	long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
//	System.out.println("Response Time" + responseTime);
	
	if(responseTime<=2500) {
//		System.out.println("Response Time is within range");
	}else {
//		System.out.println("Response Time is out of range");
	}
	
	int responseStatusCode = response.getStatusCode();
	System.out.println("Response Status Code :" + responseStatusCode);
	softassert.assertEquals(responseStatusCode,200, "Response Code is not matching!");
	
	String responseHeaderContextType = response.getHeader("Content-Type");
	System.out.println("Response Header ContentType :" + responseHeaderContextType);
	softassert.assertEquals(responseHeaderContextType, "application/json2" , "Response Header ContentType is not matching ");
	
	String responseBody = response.getBody().asString();
	System.out.println("Response Body :" + responseBody);
	
	JsonPath jp = new JsonPath(responseBody);
	
	String ProductName = jp.getString("name");
	System.out.println("Product Name:" + ProductName);
	softassert.assertEquals(ProductName, "Amazing Headset 1.0 By MD", "Product Name is not matching");
	
	String ProductDescription = jp.getString("description");
	System.out.println("Product Description :" + ProductDescription);
	softassert.assertEquals(ProductDescription, "The best Headset for amazing programmers.", "Product Description is not matching");
	
	String ProductPrice = jp.getString("price");
	System.out.println("Product Price:" + ProductPrice);
	softassert.assertEquals(ProductPrice, "199", "Product Price is not matching");
	
	softassert.assertAll();
	}
}
