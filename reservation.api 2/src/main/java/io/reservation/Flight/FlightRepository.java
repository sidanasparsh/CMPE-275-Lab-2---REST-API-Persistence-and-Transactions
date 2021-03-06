package io.reservation.Flight;

import org.springframework.data.repository.CrudRepository;

/**
* The FlightRepository program implements the services/methods customized by user that
* simply interact with the dB for each Flight Mapping.
*
* @author  Sidharth Bhasin
* @version 1.0
* @since   2017-04-19 
*/


public interface FlightRepository extends CrudRepository<Flight, String>{

	public Flight findByNumber(String flightNumber);

}
