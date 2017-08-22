package io.reservation.Reservation;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import io.reservation.Passenger.Passenger;
import io.reservation.Flight.Flight;

/**
* The Reservation program implements the Reservation Entity that
* makes schema and dependency for Reservation.
*
* @author  Sidharth Bhasin
* @version 1.0
* @since   2017-04-18 
*/
@Entity
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int genOrderNumber;
	private String id;
	@OneToOne(targetEntity=Passenger.class, cascade=CascadeType.DETACH)
	private Passenger passenger;
	private int price;
	@ManyToMany(targetEntity=Flight.class)
	private List<Flight> flights;
	
	public Reservation(){
		
	}
	
	public Reservation(int price, Passenger passenger, List<Flight> flights){
		this.price = price;
		this.passenger = passenger;
		this.flights = flights;
	}
	
	
	public int getGenOrderNumber() {
		return genOrderNumber;
	}

	public void setGenOrderNumber(int genOrderNumber) {
		this.genOrderNumber = genOrderNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Passenger getPassenger() {
		return passenger;
	}
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
}