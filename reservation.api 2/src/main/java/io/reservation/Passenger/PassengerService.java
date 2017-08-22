package io.reservation.Passenger;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.reservation.Flight.Flight;
import io.reservation.Plane.Plane;
import io.reservation.Reservation.Reservation;
import io.reservation.Reservation.ReservationRepository;

/**
* The PassengerService program provides the 
* class to implement logic for mappings for 
* Passenger Repository.
*
* @author  Sparsh Sidana
* @version 1.0
* @since   2016-04-19
*/

@Service
public class PassengerService {

	@Autowired
	private PassengerRepository passengerRepository;
	
	@Autowired
	private ReservationRepository reservationRepository;

	public ResponseEntity<?> addPassenger(String firstname, String lastname,
			String age, String gender, String phone) {
		System.out.println("inside addPassenger()");
		Passenger passenger = passengerRepository.findByPhone(phone);
		JSONObject passengerJSON = new JSONObject();
		JSONObject json = new JSONObject();
		JSONObject reservations = new JSONObject();
		JSONObject arr[] = new JSONObject[0];
		if(passenger == null){
			Passenger newPassenger = new Passenger(firstname, lastname, Integer.parseInt(age), gender, phone);
			passengerRepository.save(newPassenger);
			System.out.println("New User");
			try {
				newPassenger.setId(""+newPassenger.getGenId());
				passengerJSON.put("passenger", json);
				
				json.put("id", newPassenger.getId());
				json.put("firstname", firstname);
				json.put("lastname", lastname);
				json.put("age", age);
				json.put("gender", gender);
				json.put("phone", phone);
				
				reservations.put("reservation", arr);
				json.put("reservations", reservations);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new ResponseEntity<>(passengerJSON.toString(),HttpStatus.CREATED);
		}
		else{
			System.out.println("User already Exists");
			return new ResponseEntity<>(generateErrorMessage("BadRequest", "400", "Another passenger with the same number already exists" ),HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<?> getPassenger(String id, String responseType){
		System.out.println("getPassenger()");
		Passenger passenger = passengerRepository.findByGenId(Integer.parseInt(id));
		
		if(passenger != null){
	
			if(responseType.equals("json"))
				return  new ResponseEntity<>(passengerToJSONString(passenger),HttpStatus.OK);
			else
				return  new ResponseEntity<>(XML.toString(new JSONObject(passengerToJSONString(passenger))),HttpStatus.OK);
		}
		else{
			//generateErrorMessage("BadRequest", "404", "Sorry, the requested passenger with id " + id +" does not exist" );
			return new ResponseEntity<>(generateErrorMessage("BadRequest", "404", "Sorry, the requested passenger with id " 
			+ id +" does not exist"), HttpStatus.NOT_FOUND);
		}
	}
	
	public List<Passenger> getAllPassengers() {
		List<Passenger> passengers = new ArrayList<>();
		passengerRepository.findAll()
		.forEach(passengers::add);
		
		
		for(int i=0;i<passengers.size();i++){
			System.out.println("Passenger "+(i+1));
			System.out.println("Passenger id"+passengers.get(i).getId());
			
			System.out.println("Firstname "+passengers.get(i).getFirstname());
			System.out.println("last "+passengers.get(i).getLastname());
			System.out.println("age "+passengers.get(i).getAge());
			System.out.println("gender "+passengers.get(i).getGender());
			System.out.println("phone "+passengers.get(i).getPhone());
		}
		
		return passengers;
	}
	
	public ResponseEntity<?> updatePassenger(String id, String firstname, 
			String lastname, String age, String gender, String phone) {
		//topics.add(topic);
		System.out.println("inside updatePassenger()");
		Passenger passenger = passengerRepository.findByGenId(Integer.parseInt(id));
		JSONObject json = new JSONObject();

		if(passenger == null){
			return  new ResponseEntity<>(generateErrorMessage("BadRequest", "404", 
					"Sorry, the requested passenger was not updated" ),HttpStatus.BAD_REQUEST);
		}
		System.out.println("Updating User");

		try{
			Passenger tempPassenger=passengerRepository.findByPhone(phone);
			if(tempPassenger != null && tempPassenger.getId() != id)
				return  new ResponseEntity<>(generateErrorMessage("BadRequest", "404", 
						"Sorry, the passenger with phone number "+tempPassenger.getPhone()
						+" already exists in the DB!" ),HttpStatus.NOT_FOUND);
			
			passenger.setAge(Integer.parseInt(age));
			passenger.setFirstname(firstname);
			passenger.setGender(gender);
			passenger.setLastname(lastname);
			passenger.setPhone(phone);
			passengerRepository.save(passenger); //Added to save to DB
			json.put("id", passenger.getId());
			json.put("firstname", firstname);
			json.put("lastname", lastname);
			json.put("age", age);
			json.put("gender", gender);
			json.put("phone", phone);
			
		} catch (JSONException e) {
			//return generateErrorMessage("BadRequest", "404", "Sorry, the requested passenger was not updated" );
			return  new ResponseEntity<>(generateErrorMessage("BadRequest", "404", "Sorry, the requested passenger was not updated" ),HttpStatus.NOT_FOUND);

		}
		
		//return passengerToJSONString(passenger);
		return  new ResponseEntity<>(passengerToJSONString(passenger),HttpStatus.OK);

	}

	public ResponseEntity<?> deletePassenger(String id) {
		
		System.out.println("inside deletePassenger()");
		Passenger passenger = passengerRepository.findByGenId(Integer.parseInt(id));
		if(passenger == null){
			System.out.println("deletePassenger() if");
			return new ResponseEntity<>(generateErrorMessage("BadRequest", "404", "Sorry, the requested passenger with id " + id +" does not exist" ), HttpStatus.NOT_FOUND);
		}
		else{
			System.out.println("deletePassenger() else");
			
			try{
				System.out.println("deletePassenger() else try");
				
				List<Reservation> reservations = reservationRepository.findByPassenger(passenger);

				for(Reservation reservation : reservations){
					System.out.println("deletePassenger() else for");
					deleteReservation(reservation, passenger);
				}
				//throw new RuntimeException("Testing Transections");
				System.out.println("deletePassenger() before delete");
				passengerRepository.delete(passenger);
			}
			catch(Exception e){
				System.out.println("Firstname "+passenger.getFirstname());
				System.out.println("Id "+passenger.getId());
				System.out.println("last "+passenger.getLastname());
				System.out.println("age "+passenger.getAge());
				System.out.println("gender "+passenger.getGender());
				System.out.println("phone "+passenger.getPhone());
				System.out.println("deletePassenger() catch");
				e.printStackTrace();
				return new ResponseEntity<>(generateErrorMessage("BadRequest", "404", "Sorry, there was some problem deleting the passenger. Please try again."), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		return new ResponseEntity<>(XML.toString(new JSONObject(generateErrorMessage("Response", "200", "Passenger with id " + id + " is deleted successfully"))),HttpStatus.OK);		
	}
	
	public void deleteReservation(Reservation reservation, Passenger passenger){
		System.out.println("inside deleteReservation()");
		
		try{
			System.out.println("deleteReservation() try");
			
			for(Flight flight : reservation.getFlights()){
				System.out.println("deleteReservation() else for");
				updateFlightSeats(flight);
				flight.getPassenger().remove(passenger);
			}
			
			System.out.println("deleteReservation() before delete");
			passenger.getReservation().remove(reservation);
			reservationRepository.delete(reservation);
		}
		catch(Exception e){
			System.out.println("deleteReservation() catch");
		}
	}
	
	public void updateFlightSeats(Flight flight){
		System.out.println("inside updateFlightSeats()");
		
		try{
			System.out.println("updateFlightSeats() try");
			
			flight.setSeatsLeft(flight.getSeatsLeft()+1);
		}
		catch(Exception e){}
	}
	
	public String generateErrorMessage(String header, String code, String message){
		JSONObject result = new JSONObject();
		JSONObject error = new JSONObject();
		
		try{
			result.put(header, error);
			error.put("code", code);
			error.put("msg", message);
		}catch(Exception e){
			System.out.println("generateErrorMessage() catch");
		}
		
		return result.toString();
	}

	
	public String passengerToJSONString(Passenger passenger){
		JSONObject result = new JSONObject();
		JSONObject fields = new JSONObject();
		JSONObject reservationsJSON = new JSONObject();
		JSONObject arr[] = null;
		System.out.println("inside passengerToJSONString()#####");
		
		try {
			System.out.println("inside passengerToJSONString() try");
			result.put("passenger", fields);
			
			fields.put("id", ""+passenger.getGenId());
			fields.put("firstname", passenger.getFirstname());
			fields.put("lastname", passenger.getLastname());
			fields.put("age", ""+passenger.getAge());
			fields.put("gender", passenger.getGender());
			fields.put("phone", passenger.getPhone());
			
			System.out.println("id "+passenger.getGenId());
			System.out.println("firstname "+passenger.getFirstname());
			System.out.println("lastname "+passenger.getLastname());
			System.out.println("age "+passenger.getAge());
			System.out.println("gender "+passenger.getGender());
			System.out.println("phone "+passenger.getPhone());
			
			int i = 0;
			List<Reservation> reservations = passenger.getReservation();
			arr = new JSONObject[reservations.size()];
			System.out.println("reservations size() "+reservations.size());

			for(Reservation reservation : reservations){
				System.out.println("Reservation");
				arr[i++] = reservationToJSONString(reservation);
				System.out.println(reservation.getGenOrderNumber());
				System.out.println(reservation.getPrice());
			}
			reservationsJSON.put("reservation", arr);
			fields.put("reservations", reservationsJSON);
			
		} catch (JSONException e) {
			System.out.println("inside passengerToJSONString() catch");

			e.printStackTrace();
		}

		return result.toString();
	}
	
	public JSONObject reservationToJSONString(Reservation reservation){
		
		JSONObject result = new JSONObject();
		JSONObject flightsJSON = new JSONObject();
		JSONObject arr[] = new JSONObject[reservation.getFlights().size()];
		int i = 0, price = 0;
		
		System.out.println("inside reservationToJSONString()");
		System.out.println("getReservation() flight size "+reservation.getFlights().size());
		
		try {
			result.put("orderNumber", ""+reservation.getGenOrderNumber());
					
			for(Flight flight : reservation.getFlights()){
				arr[i++] =  flightToJSONString(flight);
				price += flight.getPrice();
			}
			result.put("price", ""+price);
			flightsJSON.put("flight", arr);
			result.put("flights", flightsJSON);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public JSONObject flightToJSONString(Flight flight){
		JSONObject json = new JSONObject();
		JSONObject flightJSON = new JSONObject();
		System.out.println("inside flightToJSONString()");

		try {
			System.out.println("inside flightToJSONString() try 1");
			json.put("flight", flightJSON);
			flightJSON.put("number", flight.getNumber());
			flightJSON.put("price", ""+flight.getPrice());
			flightJSON.put("from", flight.getFromSource());
			System.out.println("inside flightToJSONString() try 2");
			flightJSON.put("to", flight.getFromSource());
			flightJSON.put("departureTime", flight.getDepartureTime());
			flightJSON.put("arrivalTime", flight.getArrivalTime());
			flightJSON.put("description", flight.getDescription());
			flightJSON.put("plane", planeToJSONString(flight.getPlane()));
		} catch (JSONException e) {
			System.out.println("inside flightToJSONString() catch");
			e.printStackTrace();
		}
		System.out.println("inside flightToJSONString() retruning");
		return json;
	}
	
	
	public JSONObject planeToJSONString(Plane plane){
		JSONObject planeJSON = new JSONObject();

		try {
			planeJSON.put("capacity", ""+plane.getCapacity());
			planeJSON.put("model", plane.getModel());
			planeJSON.put("manufacturer", plane.getManufacturer());
			planeJSON.put("yearOfManufacture", ""+plane.getYearOfManufacture());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return planeJSON;
	}
}