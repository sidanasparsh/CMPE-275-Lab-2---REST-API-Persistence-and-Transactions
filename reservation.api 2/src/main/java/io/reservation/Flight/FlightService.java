package io.reservation.Flight;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.reservation.Passenger.Passenger;
import io.reservation.Passenger.PassengerRepository;
import io.reservation.Plane.Plane;
import io.reservation.Reservation.Reservation;
import io.reservation.Reservation.ReservationRepository;

/**
* The FlightService program provides the 
* class to implement logic for mappings for 
* Flight Repository.
*
* @author  Sparsh Sidana
* @version 1.0
* @since   2016-04-19
*/

@Service
public class FlightService {

	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private PassengerRepository passengerRepository;
	
	public ResponseEntity<?> addFlight(String flightNumber, int price, String from, String to, String departureTime,
			String arrivalTime, String description, int capacity, String model, int yearOfManufacture,
			String manufacturer) {
		
		Date departure = null, arrival = null;
		try {
			System.out.println("inside addFlight()");
			if(flightRepository.findByNumber(flightNumber)!=null)
				return updateFlight(flightNumber, price, from, to, departureTime, arrivalTime, 
						description, capacity, model, yearOfManufacture, manufacturer);
			//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
			
				departure = dateFormat.parse(departureTime);
				arrival = dateFormat.parse(arrivalTime);
				if((departure).compareTo(arrival)>0){
					return  new ResponseEntity<>(generateErrorMessage("BadRequest", "404", 
							"Sorry, the departure time cannot be greater than the arrival time"), 
							HttpStatus.NOT_FOUND);
				}
		

			
			Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
			plane.setNumber(flightNumber);
			Flight flight = new Flight(flightNumber, price, from, to, departure, arrival, description, new ArrayList<Passenger>(), plane);
			
			flightRepository.save(flight);
		
		} catch (Exception e) {
			System.out.println("EXCEPTION########");
			
			return  new ResponseEntity<>(generateErrorMessage("BadRequest", "404", 
					"Sorry, there was some problem."), 
					HttpStatus.NOT_FOUND);
			
//			e.printStackTrace();
		}
		
		return getFlight(flightNumber, "xml");
	}

	/*public ResponseEntity<?> getFlight(String flightNumber) {

		System.out.println("inside getFlight()");
		Flight flight = flightRepository.findOne(flightNumber);

		if(flight != null){
			System.out.println("inside getFlight() if");			
			//return flightToJSONString(flight).toString();
			return  new ResponseEntity<>(flightToJSONString(flight),HttpStatus.OK);

		}
		else{
			System.out.println("inside getFlight() else");
			//return generateErrorMessage("BadRequest", "404", "Sorry, the requested flight with number " + flightNumber +" does not exist");
			return  new ResponseEntity<>(generateErrorMessage("BadRequest", "404", "Sorry, the requested flight with number " + flightNumber +" does not exist") ,HttpStatus.NOT_FOUND);
		}
	}*/
	public ResponseEntity<?> getFlight(String flightNumber, String responseType) {
		
		System.out.println(responseType);
		System.out.println("inside getFlight()");
		Flight flight = flightRepository.findOne(flightNumber);
		if(flight != null){
			System.out.println("inside getFlight() if");			
			//return flightToJSONString(flight).toString();
			if(responseType.equals("json"))
				return  new ResponseEntity<>(flightToJSONString(flight).toString(),HttpStatus.OK);
			else
				return new ResponseEntity<>(XML.toString((flightToJSONString(flight))),HttpStatus.OK);
		}
		else{
			System.out.println("inside getFlight() else");
			//return generateErrorMessage("BadRequest", "404", "Sorry, the requested flight with number " + flightNumber +" does not exist");
			if(responseType.equals("json"))
				return  new ResponseEntity<>(generateErrorMessage("BadRequest", "404", "Sorry, the requested flight with number " + flightNumber +" does not exist") ,HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<>(generateErrorMessage("BadRequest", "404", "Sorry, the requested flight with number " 
			+ flightNumber +" does not exist"), HttpStatus.NOT_FOUND);
		}
	}

	
	public String deleteFlight(String flightNumber) {
		System.out.println("inside deleteFlight()");
		Flight flight = flightRepository.findOne(flightNumber);
		
		if(flight != null){
			try{
				//flight.getPassenger().clear();
				flightRepository.delete(flight);
			}
			catch(Exception e){
				System.out.println("inside deleteFlight() catch");
				List<Reservation> reservations=(List<Reservation>) reservationRepository.findAll();
				for(Reservation reservation: reservations){
					if(reservation.getFlights().contains(flight)){
						//System.out.println(reser);
						return generateErrorMessage("BadRequest", "400", "Flight "+ flight.getNumber()+" cannot be deleted as it is being used in reservation "+ reservation.getId());
					}
				}
				return generateErrorMessage("BadRequest", "400", "Flight cannot be romved, their are some passenger(s) who have booked this flight");
			}
			return generateErrorMessage("Response", "200", "Flight with number " + flightNumber + " is deleted successfully");
		}
		return generateErrorMessage("BadRequest", "404", "Flight with number " + flightNumber + " not found");
	}

	/*public String updateFlight(String flightNumber, int price, String from, String to, String departureTime,
			String arrivalTime, String description, int capacity, String model, int yearOfManufacture,
			String manufacturer) {
		System.out.println("inside updateFlight()");
		Flight flight = flightRepository.findOne(flightNumber);
		
		if(flight != null){
			try{
				flightRepository.delete(flight);
			}
			catch(Exception e){
				System.out.println("inside deleteFlight() catch");
				return generateErrorMessage("Response", "400", "There was some error deleting flight.");
			}
			return generateErrorMessage("Response", "200", "Flight with number " + flightNumber + " is deleted successfully");
		}
		return generateErrorMessage("Response", "404", "Flight with number " + flightNumber + " not found");
	}*/
	public Flight checkFlightUpdateForPassengers(List<Flight> passengerFlights, Flight updatedFlight){
		for(Flight flight: passengerFlights){
			if(flight.getNumber().equals(updatedFlight.getNumber()))
				continue;
			Date currentFlightDepartureDate=flight.getDepartureTime();
			Date currentFlightArrivalDate=flight.getArrivalTime();
			Date min=updatedFlight.getDepartureTime();
			Date max=updatedFlight.getArrivalTime();
			if((currentFlightArrivalDate.compareTo(min)>=0 && currentFlightArrivalDate.compareTo(max)<=0) || (currentFlightDepartureDate.compareTo(min)>=0 && currentFlightDepartureDate.compareTo(max)<=0)){
				System.out.println("I am failing update flight here checkCurrentreservationFlightsTimings");
				//List<Flight> flightList= new ArrayList<Flight>();
				return flight;
			}
		}
		return null;
	}
	public ResponseEntity<?> updateFlight(String flightNumber, int price, String from, String to, String departureTime,
			String arrivalTime, String description, int capacity, String model, int yearOfManufacture,
			String manufacturer) {
		System.out.println("inside updateFlight()");
		Flight flight = flightRepository.findOne(flightNumber);
		System.out.println("CHECKK MEEE:"+flight.getNumber());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
		Date departure = null, arrival = null;
		try {
			departure = dateFormat.parse(departureTime);
			arrival = dateFormat.parse(arrivalTime);
			if(departure.compareTo(arrival)>=0)
				return  new ResponseEntity<>(generateErrorMessage("BadRequest", "404", "Sorry, the departure time cannot be greater than the arrival time") ,HttpStatus.NOT_FOUND);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(flight != null){
			if(departure.compareTo(flight.getDepartureTime())!=0 || arrival.compareTo(flight.getArrivalTime())!=0){
			//	checkTimingsWithExistingFlightsForAllPassenters(departure, arrival);
				List<Passenger> passengers = (List<Passenger>) passengerRepository.findAll();
				for(Passenger passenger : passengers){
					List<Reservation> reservations=passenger.getReservation();
					List<Flight> currentPassengerFlights=new ArrayList<Flight>();
					for(Reservation reservation:reservations){
						for(Flight currentPassegnerFlight:reservation.getFlights()){
							currentPassengerFlights.add(currentPassegnerFlight);
						}
					}
					//System.out.println(passengers.size());
					//System.out.println(passenger.getGenId()+"  "+passenger.getFirstname());
					//System.out.println(currentPassengerFlights.size());
				//	System.out.println(passenger.());
					for(Flight currentFlight: currentPassengerFlights){
						if(currentFlight.getNumber().equals(flightNumber)){
							System.out.println("in .containts");
							flight.setDepartureTime(departure);
							flight.setArrivalTime(arrival);
							Flight returnedFlight=checkFlightUpdateForPassengers(currentPassengerFlights,flight);
							if(returnedFlight!=null){
								return  new ResponseEntity<>(generateErrorMessage("BadRequest", "404", "Flight with number " + flightNumber + " cannot be updated as it overlaps with the flight "+returnedFlight.getNumber()+" for passenger with id:"+ passenger.getGenId()),HttpStatus.NOT_FOUND);
							}
							System.out.println("after .containts");
						}
					}
				}
			}
			System.out.println("NO CONFLICT");

		//flightRepository.save(flight);
		//return new ResponseEntity<>(XML.toString(flightToJSONString(flight)),HttpStatus.OK);
		//	List<Reservation> reservations= (List<Reservation>)
		
			try{
				//flightRepository.delete(flight);
				flight.setPrice(price);
				flight.setFromSource(from);
				flight.setToDestination(to);
				flight.setDepartureTime(departure);
				flight.setArrivalTime(arrival);
				flight.setDescription(description);
				flight.getPlane().setCapacity(capacity);
				flight.getPlane().setManufacturer(manufacturer);
				flight.getPlane().setModel(model);
				flight.getPlane().setYearOfManufacture(yearOfManufacture);;
				flightRepository.save(flight);

			}
			catch(Exception e){
				System.out.println("inside deleteFlight() catch");
				return  new ResponseEntity<>(generateErrorMessage("BadRequest", "400", "There was some error deleting flight." ),HttpStatus.NOT_FOUND);
			}
		}
		return getFlight(flightNumber, "xml");
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
	
	public JSONObject flightToJSONString(Flight flight){
		JSONObject json = new JSONObject();
		JSONObject flightJSON = new JSONObject();
		JSONObject passenger = new JSONObject();
		JSONObject arr[] = new JSONObject[flight.getPassenger().size()];
		int i = 0;
		
		try {
			json.put("flight", flightJSON);
			flightJSON.put("flightNumber", flight.getNumber());
			flightJSON.put("price", ""+flight.getPrice());
			flightJSON.put("from", flight.getFromSource());
			flightJSON.put("to", flight.getFromSource());
			flightJSON.put("departureTime", flight.getDepartureTime());
			flightJSON.put("arrivalTime", flight.getArrivalTime());
			flightJSON.put("description", flight.getDescription());
			flightJSON.put("seatsLeft", ""+flight.getSeatsLeft());
			flightJSON.put("plane", planeToJSONString(flight.getPlane()));
			flightJSON.put("passengers", passenger);
			
			
			for(Passenger pass : flight.getPassenger()){
				
				arr[i++] = passengerToJSONString(pass);
			}
			passenger.put("passenger", arr);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	private JSONObject passengerToJSONString(Passenger passenger) {
		JSONObject json = new JSONObject();
		System.out.println("inside passengerToJSONString");

		try {
			json.put("id", ""+passenger.getGenId());
			json.put("firstname", ""+passenger.getFirstname());
			json.put("lastname", ""+passenger.getLastname());
			json.put("age", ""+passenger.getAge());
			json.put("gender", ""+passenger.getGender());
			json.put("phone", ""+passenger.getPhone());
			
			System.out.println("Firstname "+passenger.getFirstname());
			System.out.println("Id "+passenger.getGenId());
			System.out.println("last "+passenger.getLastname());
			System.out.println("age "+passenger.getAge());
			System.out.println("gender "+passenger.getGender());
			System.out.println("phone "+passenger.getPhone());
			
		} catch (JSONException e) {
			System.out.println("inside passengerToJSONString() catch");
			e.printStackTrace();
		}
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