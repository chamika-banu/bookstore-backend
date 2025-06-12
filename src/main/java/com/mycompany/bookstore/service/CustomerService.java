package com.mycompany.bookstore.service;

import com.mycompany.bookstore.exception.CustomerNotFoundException;
import com.mycompany.bookstore.exception.InvalidInputException;
import com.mycompany.bookstore.exception.DuplicateCustomerException;
import com.mycompany.bookstore.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerService {
    private static final AtomicLong idCounter = new AtomicLong(0);

    // Collection of customers
    private static final Map<Long, Customer> customers = new HashMap<>();

    // Initialize singleton instance
    private static final CustomerService instance = new CustomerService();

    private CustomerService() {
        // Sample data to simulate some customers in memory
        customers.put(idCounter.incrementAndGet(), new Customer(idCounter.get(), "Alice Smith", "alice@example.com", "password123"));
        customers.put(idCounter.incrementAndGet(), new Customer(idCounter.get(), "Bob Johnson", "bob@example.com", "password456"));
    }

    public static CustomerService getInstance() {
        return instance;
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        // Convert Map values to a List
        return new ArrayList<>(customers.values());
    }

    // Create customer
    public Customer createCustomer(Customer customer) {
        validateCustomerInput(customer);                        

        Long id = idCounter.incrementAndGet();
        customer.setId(id);
        customers.put(id, customer);
        return customer;
    }

    // Get author by ID
    public Customer getCustomerById(Long id) {
        Customer customer = customers.get(id);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found.");
        }

        return customer;
    }

    // Update customer
    public Customer updateCustomer(Long id, Customer updatedCustomer) {                
        Customer existingCustomer = customers.get(id);

        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found.");
        }
        
        validateCustomerInput(updatedCustomer);

        // Update the existing author's details
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPassword(updatedCustomer.getPassword());

        customers.put(id, existingCustomer);

        return existingCustomer;
    }

    // Remove customer
    public Boolean removeCustomer(Long id) {
        Customer removed = customers.remove(id);
        return removed != null;
    }
    
    // Validate customer input
    private void validateCustomerInput(Customer customer) {
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new InvalidInputException("Customer name cannot be null or empty.");
        }

        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            throw new InvalidInputException("Customer email cannot be null or empty.");
        }

        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            throw new InvalidInputException("Customer password cannot be null or empty.");
        }

        // Check for duplicate customer by name or email
        for (Customer currentCustomer : customers.values()) {
            if (currentCustomer.getName().equalsIgnoreCase(customer.getName())) {
                throw new DuplicateCustomerException("Customer with name '" + customer.getName() + "' already exists.");
            }

            if (currentCustomer.getEmail().equalsIgnoreCase(customer.getEmail())) {
                throw new DuplicateCustomerException("Customer with email '" + customer.getEmail() + "' already exists.");
            }
        }
    }



}
