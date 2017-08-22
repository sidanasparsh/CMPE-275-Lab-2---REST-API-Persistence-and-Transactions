package io.reservation.Passenger;

import org.springframework.data.repository.CrudRepository;

/**
* The PassengerRepository program implements the services/methods customized by user that
* simply interact with the dB for each Passenger Mapping.
*
* @author  Sidharth Bhasin
* @version 1.0
* @since   2017-04-19 
*/

public interface PassengerRepository extends CrudRepository<Passenger, String>{
	public Passenger findByPhone(String phone);

	//public Passenger findById(int id);

	public void deleteById(int id);

	public Passenger findByGenId(int passengerId);

}
