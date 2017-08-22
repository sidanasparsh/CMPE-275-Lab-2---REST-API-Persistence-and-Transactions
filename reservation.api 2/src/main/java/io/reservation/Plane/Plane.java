package io.reservation.Plane;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
* The Plane program implements the Plane Entity that
* makes schema and dependency for Plane.
*
* @author  Sparsh Sidana
* @version 1.0
* @since   2017-04-18 
*/


@Entity
public class Plane {
	
	@Id
	private String number;
	private int capacity;
	private String model; 
	private String manufacturer;
	private int yearOfManufacture;
	
	public Plane(){
		
	}
	
	public Plane(int capacity, String model, 
			String manufacturer, int yearOfManufacture){
		this.capacity = capacity;
		this.model = model;
		this.manufacturer = manufacturer;
		this.yearOfManufacture = yearOfManufacture;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public int getYearOfManufacture() {
		return yearOfManufacture;
	}
	public void setYearOfManufacture(int yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}
}
