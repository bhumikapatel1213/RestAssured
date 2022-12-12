package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.sun.xml.xsom.impl.scd.Iterators.Map;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DeleteOneProductAdvance {
	String baseURL;
	SoftAssert softassert;
	String createPayLoadPath;
	HashMap<String,String> createPayLoad;
	String firstProductId;
	HashMap<String,String> deletePayLoad;
	String deleteProductId;
	
	public DeleteOneProductAdvance() {
		baseURL ="https://techfios.com/api-prod/api/product";
		softassert = new SoftAssert();
		createPayLoadPath = "src\\main\\java\\data\\CreatePayLoad.json";
		createPayLoad = new HashMap<String,String>();
		deletePayLoad = new HashMap<String,String>();
	}
	
	public HashMap<String, String> createPayLoadMap(){
		createPayLoad.put("id", "6277");
		createPayLoad.put("name", "Amazing Headset 4.0 By MD");
		createPayLoad.put("description", "The best Headset for amazing programmers.");
		createPayLoad.put("price", "99");
		createPayLoad.put("category_id", "2");
		createPayLoad.put("category_name", "Electronics");
		return createPayLoad;
	}
	
	public HashMap<String, String> deletePayLoadMap(){
		deletePayLoad.put("id", deleteProductId);
		return deletePayLoad;
	}
	@Test(priority = 1)
	public void createOneProduct() {
		
	System.out.println(" Create Paylod Map" + createPayLoadMap());
	Response response = (Response) 
	given()
		.baseUri(baseURL)
		.header("Content-Type","application/json; charset=UTF-8")
		.auth().preemptive().basic("demo@techfios.com", "abc123.")
	//	.body(arg0).
		.body(createPayLoadMap()).
	when()
		.post("/create.php").
	then()
	   .extract().response();
	
	
	int responseStatusCode = response.getStatusCode();
	System.out.println("Response Status Code :" + responseStatusCode);
	softassert.assertEquals(responseStatusCode,201, "Response Code is not matching!");
	
	String responseHeaderContextType = response.getHeader("Content-Type");
	System.out.println("Response Header ContentType :" + responseHeaderContextType);
	softassert.assertEquals(responseHeaderContextType, "application/json; charset=UTF-8" , "Response Header ContentType is not matching ");
	
	String responseBody = response.getBody().asString();
	System.out.println("Response Body :" + responseBody);
	
	JsonPath jp = new JsonPath(responseBody);
	
	String ProductMessage = jp.getString("message");
	System.out.println("Product Message:" + ProductMessage);
	softassert.assertEquals(ProductMessage, "Product was created.", "Product message is not matching");
	
	softassert.assertAll();
	}
	
	@Test(priority = 2)
	public void readAllProducts() {
		Response response = (Response) given()
			.baseUri(baseURL)
			.header("Content-Type","application/json; charset=UTF-8")
			.auth().preemptive().basic("demo@techfios.com", "abc123.").
			
		when()
			.get("/read.php").
		then()
		.extract().response();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		firstProductId = jp.getString("records[0].id");
		System.out.println("First product id :" + firstProductId);	
		deleteProductId = firstProductId;
		}
	
	
	@Test(priority = 3)
	public void deleteOneProduct() {
		
	Response response = (Response) 
	given()
		.baseUri(baseURL)
		.header("Content-Type","application/json; charset=UTF-8")
		.auth().preemptive().basic("demo@techfios.com", "abc123.")
	//	.body(CreatePayLoad(File)).
		.body(deletePayLoadMap()).
	when()
		.delete("/delete.php").
	then()
	   .extract().response();

	int responseStatusCode = response.getStatusCode();
	System.out.println("Response Status Code :" + responseStatusCode);
	softassert.assertEquals(responseStatusCode,200, "Response Code is not matching!");
	
	String responseHeaderContextType = response.getHeader("Content-Type");
	System.out.println("Response Header ContentType :" + responseHeaderContextType);
	softassert.assertEquals(responseHeaderContextType, "application/json; charset=UTF-8" , "Response Header ContentType is not matching ");
	
	String responseBody = response.getBody().asString();
	System.out.println("Response Body :" + responseBody);
	
	JsonPath jp = new JsonPath(responseBody);
	
	String ProductMessage = jp.getString("message");
	System.out.println("Product Message:" + ProductMessage);
	softassert.assertEquals(ProductMessage, "Product was deleted.", "Product message is not matching");
	
	softassert.assertAll();
	}
	
	@Test(priority = 4)
	public void readOneDeletedProduct() {
	Response response =
	given()
		.baseUri(baseURL)
		.header("Content-Type","application/json")
		.auth().preemptive().basic("demo@techfios.com", "abc123.")
		.queryParam("id", deletePayLoadMap().get("id")).
	when()
		.get("/read_one.php").
	then()
	   .extract().response();
	
	int responseStatusCode = response.getStatusCode();
	System.out.println("Response Status Code :" + responseStatusCode);
	softassert.assertEquals(responseStatusCode,404, "Response Code is not matching!");
	
		
	String ActualresponseBody = response.getBody().asString();
	System.out.println("Actual Response Body :" + ActualresponseBody);
	
	JsonPath jp = new JsonPath(ActualresponseBody);
	
	String ActualDeleteMessage = jp.getString("message");
	System.out.println("Actual Product Name:" + ActualDeleteMessage);
	softassert.assertEquals(ActualDeleteMessage, "Product does not exist.", "Product Name is not matching");
	
	softassert.assertAll();
	}
}
