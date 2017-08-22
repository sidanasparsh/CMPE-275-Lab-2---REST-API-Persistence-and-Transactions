package io.reservation.Flight;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import io.reservation.Passenger.Passenger;
import io.reservation.Plane.Plane;

/**
* The Flight program implements the Flight Entity that
* makes schema and dependency for Flights.
*
* @author  Sidharth Bhasin
* @version 1.0
* @since   2017-04-18 
*/

@Entity
public class Flight {

	@Id
	private String number;
	private int price;
	private String fromSource;
	private String toDestination;
	private Date departureTime;
	private Date arrivalTime;
	private int seatsLeft;
	private String description;
	@OneToOne(targetEntity=Plane.class, cascade=CascadeType.ALL)
	private Plane plane;
	@ManyToMany(targetEntity=Passenger.class)
    @Column(name = "passlist")  
	private List<Passenger> passengers;
	
	public Flight(){
		
	}
	
	public Flight(String number, int price, String fromSource, 
			String toDestionation, Date departureTime, Date arrivalTime,
			String description, List<Passenger> passengers, Plane plane){
		this.number = number;
		this.price = price;
		this.fromSource = fromSource;
		this.toDestination = toDestionation;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.description = description;
		this.plane = plane;
		this.seatsLeft = plane.getCapacity();
		this.passengers = passengers;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getFromSource() {
		return fromSource;
	}

	public void setFromSource(String fromSource) {
		this.fromSource = fromSource;
	}

	public String getToDestination() {
		return toDestination;
	}

	public void setToDestination(String toDestination) {
		this.toDestination = toDestination;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getSeatsLeft() {
		return seatsLeft;
	}

	public void setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}
	
	public List<Passenger> getPassenger() {
		return passengers;
	}
	
	public void setPassenger(List<Passenger> passengers) {
		this.passengers = passengers;
	}
}
