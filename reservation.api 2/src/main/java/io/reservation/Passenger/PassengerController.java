package io.reservation.Passenger;
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

/**
* The PassengerController program implements an the services/methods that
* simply interact with the dB for each Passenger Mapping.
*
* @author  Sidharth Bhasin
* @version 1.0
* @since   2017-04-19 
*/
@Transactional
@RestController
public class PassengerController {

	@Autowired
	private PassengerService passengerService;
	
	@RequestMapping(value="/passenger/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<?> getPassenger(@PathVariable String id, 
			@RequestParam(value = "xml", required=false) String xml){
		
		String responseType="json";
		
		if(xml != null && xml.equals("true")){ // ?xml=true
			responseType="xml";
		}
		
		return passengerService.getPassenger(id, responseType);
	}
	
	@RequestMapping(value="/passenger", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addPassenger(
			@RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname,
			@RequestParam("age") String age,
			@RequestParam("gender") String gender,
			@RequestParam("phone") String phone
			) {
		
		 ResponseEntity<?> res= passengerService.addPassenger(firstname, 
				lastname, age, gender, phone);
		 //throw new RuntimeException();
		 
		 return res;
	}
	
	@RequestMapping(value="/passenger/{id}", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePassenger(
			@PathVariable String id, 
			@RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname,
			@RequestParam("age") String age,
			@RequestParam("gender") String gender,
			@RequestParam("phone") String phone
			) {
		
		 return passengerService.updatePassenger(id, firstname, 
				lastname, age, gender, phone);
	}
	
	@RequestMapping(value="/passengers", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getAllPassengers(){
		
		passengerService.getAllPassengers();
		
		return "";
	}
	
	@RequestMapping(value="/passenger/{id}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deletePassenger(@PathVariable String id) {
		
		
		return passengerService.deletePassenger(id);
	}
}
