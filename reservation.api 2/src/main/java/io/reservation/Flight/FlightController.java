package io.reservation.Flight;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* The FlightController program implements an the services/methods that
* simply interact with the dB for each Flight Mapping.
*
* @author  Sidharth Bhasin
* @version 1.0
* @since   2017-04-19 
*/

@RestController
@Transactional
public class FlightController {

	@Autowired
	private FlightService flightService;
	@RequestMapping(value = "/flight/{flightNumber}", method = RequestMethod.POST, produces=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> addFlight(@PathVariable String flightNumber,
			@RequestParam("price") int price,
			@RequestParam("from") String from,
			@RequestParam("to") String to,
			@RequestParam("departureTime") String departureTime,
			@RequestParam("arrivalTime") String arrivalTime,
			@RequestParam("description") String description,
			@RequestParam("capacity") int capacity,
			@RequestParam("model") String model,
			@RequestParam("yearOfManufacture") int yearOfManufacture,
			@RequestParam("manufacturer") String manufacturer){
		
		System.out.println("addFlight()#################");
		return flightService.addFlight(flightNumber, price, from, to, 
				departureTime, arrivalTime, description, capacity,
				model, yearOfManufacture, manufacturer);
	}
	
	@RequestMapping(value = "/flight/{flightNumber}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getFlight(
			@PathVariable String flightNumber, 
			@RequestParam(value="xml", required=false) String xml){
		
		String responseType = "json";
		if(xml != null && xml.equals("true")){
			responseType = "xml";
		}
		return flightService.getFlight(flightNumber, responseType);
		
		//else
		//	return (new ResponseEntity<>("{\"BadRequest\":{"
		//			+ "\"code\":\"404\","
		//		+ "\"msg\":\"Please provide json=true or xml=true\"}}",HttpStatus.NOT_FOUND));
			
	}
	
	@RequestMapping(value = "/flight/{flightNumber}", method = RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	public String deleteFlight(@PathVariable String flightNumber){
		
		return flightService.deleteFlight(flightNumber);
	}

	@RequestMapping(value = "/flight/{flightNumber}", method = RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateFlight(@PathVariable String flightNumber,
			@RequestParam("price") int price,
			@RequestParam("from") String from,
			@RequestParam("to") String to,
			@RequestParam("departureTime") String departureTime,
			@RequestParam("arrivalTime") String arrivalTime,
			@RequestParam("description") String description,
			@RequestParam("capacity") int capacity,
			@RequestParam("model") String model,
			@RequestParam("yearOfManufacture") int yearOfManufacture,
			@RequestParam("manufacturer") String manufacturer	){
		
		return flightService.updateFlight(flightNumber, price, from, to, 
				departureTime, arrivalTime, description, capacity,
				model, yearOfManufacture, manufacturer);
	}
}