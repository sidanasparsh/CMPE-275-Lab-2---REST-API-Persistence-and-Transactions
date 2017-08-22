package io.reservation.Reservation;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import io.reservation.Passenger.Passenger;

/**
* The ReservationRepository program implements the services/methods customized by user that
* simply interact with the dB for each Reservation Mapping.
*
* @author  Sidharth Bhasin
* @version 1.0
* @since   2017-04-19 
*/
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

	Reservation findByGenOrderNumber(int number);

	List<Reservation> findByPassenger(Passenger passenger);

}
