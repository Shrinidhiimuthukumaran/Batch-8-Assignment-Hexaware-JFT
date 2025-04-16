package com.hexaware.bankingsystem.bean;

public class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;

    public Customer() {
    	
    }

    public Customer(int customerId, String firstName, String lastName, String email, String phoneNumber, String address) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        setEmail(email);
        setPhoneNumber(phoneNumber);
        this.address = address;
    }

    // here we have to do validations for the mail and phone 
    private boolean isValidEmail(String email) {
        return email.matches("^(.+)@(.+)$");
    }

    private boolean isValidPhone(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    // Getters and setters
    public int getCustomerId() {
    	return customerId;
    }
    public void setCustomerId(int customerId) { 
    	this.customerId = customerId;
    }

    public String getFirstName() { 
    	return firstName; 
    }
    public void setFirstName(String firstName) { 
    	this.firstName = firstName;
    }

    public String getLastName() { 
    	return lastName;
    }
    public void setLastName(String lastName) { 
    	this.lastName = lastName;
    }

    public String getEmail() { 
    	return email;
    }
    public void setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            this.email = "Invalid Email";
        }
    }

    public String getPhoneNumber() { 
    	return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        if (isValidPhone(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            this.phoneNumber = "Invalid Phone";
        }
    }

    public String getAddress() { 
    	return address;
    }
    public void setAddress(String address) { 
    	this.address = address;
    }

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
	}

}
