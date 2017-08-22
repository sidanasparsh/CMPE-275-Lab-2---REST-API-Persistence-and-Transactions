package io.javabrains;

import static org.junit.Assert.*;
import org.json.JSONObject;
import org.junit.Test;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
* The RegisterApplicationTests program provides the 
* class to implement test cases for mappings.
*
* @author  Sparsh Sidana
* @version 1.0
* @since   2016-04-20
*/

public class RegisterApplicationTests {

	@Test
	public void getPassenger() {
		
		try {
			// Retrieve details of passenger 1
			// We have saved first passenger with below details for testing purpose
			HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:8089/passenger/1")
					  .header("accept", "application/json")
					  //.queryString("passengerId", 1)
					  .asJson();
			
			JSONObject res = jsonResponse.getBody().getObject();
			JSONObject passenger = res.getJSONObject("passenger");
					
			System.out.println("jsonResponse "+jsonResponse.getBody().toString());
			System.out.println("jsonResponse firstname"+passenger.get("firstname"));
			
			assertEquals("Sparsh", passenger.get("firstname"));
			assertEquals("Male", passenger.get("gender"));
			assertEquals("90", passenger.get("phone"));
			assertEquals("1", passenger.get("id"));
			assertEquals("23", passenger.get("age"));
			assertEquals("Sidana", passenger.get("lastname"));
			
		} catch (Exception e) {
			System.out.println("inside getPassenger catch");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void getPassengerXML() {
		
		try {
			// Retrieve details of passenger -1
			// No such passenger possible, so we should get error response
			
			HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:8089/passenger/-1?xml=true")
					  .header("accept", "application/json")
					  .asJson();
			
			JSONObject res = jsonResponse.getBody().getObject();
			JSONObject passenger = res.getJSONObject("BadRequest");
					
			System.out.println("jsonResponse "+jsonResponse.getBody().toString());
			
			assertEquals("Sorry, the requested passenger with id -1 does not exist", passenger.get("msg"));
			assertEquals("404", passenger.get("code"));
			
			
		} catch (Exception e) {
			System.out.println("inside getPassengerXML catch");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void addPassenger() {
		
		try {
			// Set details of new passenger 
			// We should get details of new passenger
			// But since we are trying to enter same mobile number, 
			// we will get error message as response.
			
			HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:8089/passenger?firstname=Sparsh&lastname=Sidana&age=22&gender=Male&phone=99")
					  .header("accept", "application/json")
					  //.queryString("passengerId", 1)
					  //.field("parameter", "value")
					  .asJson();
			
			JSONObject res = jsonResponse.getBody().getObject();
			JSONObject passenger = res.getJSONObject("BadRequest");
					
			System.out.println("jsonResponse "+jsonResponse.getBody().toString());
			
			assertEquals("Another passenger with the same number already exists", passenger.get("msg"));
			assertEquals("400", passenger.get("code"));
			
			
		} catch (Exception e) {
			System.out.println("inside getPassengerXML catch");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void updatePassenger() {
		
		try {
			// Update details of existing passenger 
			// We should get error message as mobile number will
			// already exist
			
			
			HttpResponse<JsonNode> jsonResponse = Unirest.put("http://localhost:8089/passenger/1?"
					+ "firstname=Sparsh&lastname=Sidana&age=23&gender=Male&phone=99")
					  .header("accept", "application/json")
					  //.queryString("passengerId", 1)
					  //.field("parameter", "value")
					  .asJson();
			
			JSONObject res = jsonResponse.getBody().getObject();
			JSONObject passenger = res.getJSONObject("BadRequest");
					
			System.out.println("jsonResponse "+jsonResponse.getBody().toString());
			
			assertEquals("Sorry, the passenger with phone number 99 already exists in the DB!", passenger.get("msg"));
			assertEquals("404", passenger.get("code"));
			
			
		} catch (Exception e) {
			System.out.println("inside updatePassenger catch");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void deletePassenger() {
		
		try {
			// Delete details of passenger -1
			// No such passenger possible, so we should get error response
			
			HttpResponse<JsonNode> jsonResponse = Unirest.delete("http://localhost:8089/passenger/-1")
					  .header("accept", "application/json")
					  //.queryString("passengerId", 1)
					  //.field("parameter", "value")
					  .asJson();
			
			JSONObject res = jsonResponse.getBody().getObject();
			JSONObject passenger = res.getJSONObject("BadRequest");
					
			System.out.println("jsonResponse "+jsonResponse.getBody().toString());
			
			assertEquals("Sorry, the requested passenger with id -1 does not exist", passenger.get("msg"));
			assertEquals("404", passenger.get("code"));
			
			
		} catch (Exception e) {
			System.out.println("inside getPassengerXML catch");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void deleteReservation() {
		
		try {
			// Delete details of reservation -1
			// No such reservation possible, so we should get error response
			
			HttpResponse<JsonNode> jsonResponse = Unirest.delete("http://localhost:8089/reservation/-1")
					  .header("accept", "application/json")
					  //.queryString("passengerId", 1)
					  //.field("parameter", "value")
					  .asJson();
			
			JSONObject res = jsonResponse.getBody().getObject();
			JSONObject passenger = res.getJSONObject("BadRequest");
					
			System.out.println("jsonResponse "+jsonResponse.getBody().toString());
			
			assertEquals("Sorry, the requested reservation with number -1 does not exist", passenger.get("msg"));
			assertEquals("404", passenger.get("code"));
			
			
		} catch (Exception e) {
			System.out.println("inside getPassengerXML catch");
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void deleteFlight() {
		
		try {
			// Delete details of flight -1
			// No such flight possible, so we should get error response
			
			HttpResponse<JsonNode> jsonResponse = Unirest.delete("http://localhost:8089/flight/-2")
					  .header("accept", "application/json")
					  //.queryString("passengerId", 1)
					  //.field("parameter", "value")
					  .asJson();
			
			JSONObject res = jsonResponse.getBody().getObject();
			JSONObject passenger = res.getJSONObject("BadRequest");
					
			System.out.println("jsonResponse "+jsonResponse.getBody().toString());
			
			assertEquals("Flight with number -2 not found", passenger.get("msg"));
			assertEquals("404", passenger.get("code"));
			
			
		} catch (Exception e) {
			System.out.println("inside getPassengerXML catch");
			e.printStackTrace();
		}
		
	}

	@Test
	public void getFlightXML() {
		
		try {
			// Retrieve details of flight -2
			// No such flight possible, so we should get error response
			
			HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:8089/flight/-2?xml=true")
					  .header("accept", "application/json")
					  //.queryString("passengerId", 1)
					  //.field("parameter", "value")
					  .asJson();
			
			JSONObject res = jsonResponse.getBody().getObject();
			JSONObject passenger = res.getJSONObject("BadRequest");
					
			System.out.println("jsonResponse "+jsonResponse.getBody().toString());
			
			assertEquals("Sorry, the requested flight with number -2 does not exist", passenger.get("msg"));
			assertEquals("404", passenger.get("code"));
			
			
		} catch (Exception e) {
			System.out.println("inside getPassengerXML catch");
			e.printStackTrace();
		}
		
	}
}
