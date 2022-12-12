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

public class UpdateOneProduct {
	String baseURL;
	SoftAssert softassert;
	String firstProductId;
	HashMap<String,String> updatePayLoad;
	
	public UpdateOneProduct() {
		baseURL ="https://techfios.com/api-prod/api/product";
		softassert = new SoftAssert();
		updatePayLoad = new HashMap<String,String>();
	}
	

	public HashMap<String, String> updatePayLoadMap(){
		updatePayLoad.put("id", "6277");
		updatePayLoad.put("name", "Amazing Headset 1.0 By MD");
		updatePayLoad.put("description", "The best Headset for amazing programmers.");
		updatePayLoad.put("price", "899");
		updatePayLoad.put("category_id", "2");
		updatePayLoad.put("category_name", "Electronics");
		return updatePayLoad;
	}

	@Test(priority = 1)
	public void updateOneProduct() {
		
	Response response = (Response) 
	given()
		.baseUri(baseURL)
		.header("Content-Type","application/json; charset=UTF-8")
		.auth().preemptive().basic("demo@techfios.com", "abc123.")
		.body(updatePayLoadMap()).
	when()
		.put("/update.php").
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
	softassert.assertEquals(ProductMessage, "Product was updated.", "Product message is not matching");
	
	softassert.assertAll();
	}
	
	@Test(priority = 2)
	public void readOneUpdatedProduct() {
	Response response =
	given()
		.baseUri(baseURL)
		.header("Content-Type","application/json")
		.auth().preemptive().basic("demo@techfios.com", "abc123.")
		.queryParam("id", updatePayLoadMap().get("id")).
	when()
		.get("/read_one.php").
	then()
	   .extract().response();
		
	String ActualresponseBody = response.getBody().asString();
	System.out.println("Actual Response Body :" + ActualresponseBody);
	
	JsonPath jp = new JsonPath(ActualresponseBody);
	
	String ActualProductName = jp.getString("name");
	System.out.println("Actual Product Name:" + ActualProductName);
	softassert.assertEquals(ActualProductName, updatePayLoadMap().get("name"), "Product Name is not matching");
	
	String ActualProductDescription = jp.getString("description");
	System.out.println("Product Description :" + ActualProductDescription);
	softassert.assertEquals(ActualProductDescription, updatePayLoadMap().get("description"), "Product Description is not matching");
	
	String ActualProductPrice = jp.getString("price");
	System.out.println("Product Price:" + ActualProductPrice);
	softassert.assertEquals(ActualProductPrice, updatePayLoadMap().get("price"), "Product Price is not matching");
	}
}
