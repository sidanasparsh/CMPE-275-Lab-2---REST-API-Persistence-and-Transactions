package io.reservation.Reservation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.reservation.Flight.Flight;

/**
* The ReservationController program provides the 
* mappings for Reservation methods.
*
* @author  Sparsh Sidana
* @version 1.0
* @since   2016-04-17
*/

@RestController
@Transactional
public class ReservationController {

	@Autowired
	private ReservationService reservationService;
	
	@RequestMapping(value="/reservation/{number}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getReservation(@PathVariable int number) {
		
		 return reservationService.getReservation(number);
	}
	
	@RequestMapping(value="/reservation", method=RequestMethod.POST, produces=MediaType.APPLICATION_XML_VALUE)// Chaged it form Applicatiion_JSON
	public ResponseEntity<?> addReservation(
			@RequestParam int passengerId, 
			@RequestParam("flightLists") List<Flight> flightLists
			) {
		
		 return reservationService.addReservation(passengerId, flightLists);
	}
	
	@RequestMapping(value="/reservation/{number}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteReservation(@PathVariable int number) {
		
		 return reservationService.deleteReservation(number);
	}

	@RequestMapping(value="/reservation/{number}", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateReservaton(
			@PathVariable int number,
			@RequestParam(value="flightsAdded", required=false) String flightsAdded, 
			@RequestParam(value="flightsRemoved", required=false) String flightsRemoved){
		
		List<Flight> flightAddedObects=null;
		List<Flight> flightRemovedObects=null;

		if(flightsAdded!=null){
			String[] flightsAddedList=flightsAdded.split(",");
			for(String s: flightsAddedList){
				if(reservationService.checkFlightExistance(s)!=null){
					return reservationService.checkFlightExistance(s);
				}
			}
			flightAddedObects=reservationService.getFlights(flightsAddedList);

		}
		
		if(flightsRemoved!=null){
			String[] flightsRemovedList=flightsRemoved.split(",");
			
			for(String s: flightsRemovedList){
				if(reservationService.checkFlightExistance(s)!=null){
					return reservationService.checkFlightExistance(s);
				}
				flightRemovedObects=reservationService.getFlights(flightsRemovedList);
			}
		}
		
		if(flightRemovedObects != null){
			ResponseEntity<?> obj = reservationService.updateReservatonRemoveFlights(number, flightRemovedObects);
			if(obj != null) return obj;
		}
		
		if(flightAddedObects!=null){
			ResponseEntity<?> response = reservationService.updateReservationAddFlights(number, flightAddedObects); 
			if(response.getStatusCode() == HttpStatus.NOT_FOUND){
				System.out.print("inside if of updateReservation() "); 
				
				return response;
			}
		}
		return reservationService.getReservation(number);
	}
	
	@RequestMapping(value="/reservation", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchReservation(
			@RequestParam(value="passengerId", defaultValue = "-100", required=false) int passengerId, 
			@RequestParam(value="from", defaultValue = "from", required=false) String from, 
			@RequestParam(value="to", defaultValue = "to", required=false) String to, 
			@RequestParam(value="flightNumber", defaultValue = "flightNumber", required=false) String flightNumber){
		
		return reservationService.searchReservation(passengerId, flightNumber, to, from);
	}
}