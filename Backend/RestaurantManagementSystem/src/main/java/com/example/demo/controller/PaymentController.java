package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Payment;
import com.example.demo.entity.TransactionDetails;
import com.example.demo.exception.PaymentNotFoundException;
import com.example.demo.service.EmailService;
import com.example.demo.service.PaymentService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/payments")
public class PaymentController {
	@Autowired
	private EmailService emailservice;

	@Autowired
	private PaymentService paymentService;

	// Endpoint to add an online payment for a specific order and customer
	@PostMapping("{orderId}/{customerId}")
    public ResponseEntity<Payment> addPayment(@RequestBody Payment payment, @PathVariable long orderId,
            @PathVariable long customerId) {
        System.out.println("Received a request to add payment for order ID: " + orderId + " and customer ID: " + customerId);
        
        Payment addedPayment = paymentService.addPayment(payment, orderId, customerId);
        
        System.out.println("Payment added: " + addedPayment);
        
        emailservice.sendEmail(payment.getCustomer().getEmail(), "Online Payment Confirmation for Order number : "+payment.getOrderId()+".",
        		"Your payment has been received for Order number : "+payment.getOrderId()+" , and we are now preparing your order."
        				+ "\nExpect your order to be ready for delivery within 30 minutes.\r\n" + "Thank you for choosing us.");
        return new ResponseEntity<>(addedPayment, HttpStatus.CREATED);
    }

	// Endpoint to fetch all payments
	@GetMapping
	public List<Payment> getAllPayments() {
		System.out.println("Received a request to fetch all payments.");

		List<Payment> payments = paymentService.getAllPayments();
		System.out.println("Returning all payments: " + payments);

		return payments;
	}

	// Endpoint to delete a payment by its ID
	@DeleteMapping("/delete/{paymentId}")
	public ResponseEntity<Map<String, String>> deletePayment(@PathVariable long paymentId) {
		System.out.println("Received a request to delete payment with ID: " + paymentId);

		paymentService.deletePayment(paymentId);
		System.out.println("Payment deleted for ID: " + paymentId);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Endpoint to fetch payments for a specific customer
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<Payment>> getPaymentsByCustomerId(@PathVariable long customerId) {
		System.out.println("Received a request to fetch payments for customer ID: " + customerId);

		List<Payment> payments = paymentService.findPaymentsByCustomerId(customerId);
		if (payments.isEmpty()) {
			System.out.println("No payments found for customer ID: " + customerId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		System.out.println("Returning payments for customer ID " + customerId + ": " + payments);
		return new ResponseEntity<>(payments, HttpStatus.OK);
	}

	// Endpoint to fetch a payment by its ID
	@GetMapping("/{paymentId}")
	public ResponseEntity<Payment> findPaymentById(@PathVariable long paymentId) {
		System.out.println("Received a request to fetch payment by ID: " + paymentId);

		Optional<Payment> payment = paymentService.findPaymentById(paymentId);
		if (!payment.isPresent()) {
			System.out.println("Payment not found for ID: " + paymentId);
			throw new PaymentNotFoundException("Payment with ID " + paymentId + " not found.");
		}

		System.out.println("Returning payment for ID " + paymentId + ": " + payment.get());
		return new ResponseEntity<>(payment.get(), HttpStatus.OK);
	}

	// Endpoint to fetch payments for a specific order
	@GetMapping("/orderId/{orderId}")
	public ResponseEntity<List<Payment>> getPaymentsByOrderId(@PathVariable long orderId) {
		System.out.println("Received a request to fetch payments for order ID: " + orderId);

		List<Payment> payments = paymentService.getPaymentsByOrderId(orderId);
		if (payments.isEmpty()) {
			System.out.println("No payments found for order ID: " + orderId);
			throw new PaymentNotFoundException("Payments with order ID " + orderId + " not found.");
		}

		System.out.println("Returning payments for order ID " + orderId + ": " + payments);
		return ResponseEntity.ok(payments);
	}

	// Endpoint to delete payments for a specific order
	@DeleteMapping("/deleteOrder/{orderId}")
	public ResponseEntity<String> deletePaymentsByOrderId(@PathVariable long orderId) {
		System.out.println("Received a request to delete payments for order ID: " + orderId);

		paymentService.deletePaymentsByOrderId(orderId);
		System.out.println("Payments for Order ID " + orderId + " have been deleted.");

		return ResponseEntity.ok("Payments for Order ID " + orderId + " have been deleted.");
	}

	// Endpoint to create a transaction
	@GetMapping("/createTransaction/{amount}")
	public TransactionDetails createTransaction(@PathVariable Double amount) {
		return paymentService.createTransaction(amount);
	}
	
	// Endpoint to add a cash payment for a specific order
	@PostMapping("/cash/{orderId}")
    public ResponseEntity<Payment> addCashPayment(@RequestBody Payment payment, @PathVariable long orderId) {
        System.out.println("Received a request to add payment for order ID: " + orderId + " and customer ID: ");
        
        Payment addedPayment = paymentService.addCashPayment(payment, orderId);
        
        System.out.println("Payment added: " + addedPayment);
        
        emailservice.sendEmail(payment.getCustomer().getEmail(), "Cash Payment Confirmation for Order number : "+payment.getOrderId()+".",
        		"Your payment has been received for Order number : "+payment.getOrderId()+" , and we are now preparing your order."
        				+ "\nExpect your order to be ready for delivery within 30 minutes.\r\n" + "Thank you for choosing us.");
        return new ResponseEntity<>(addedPayment, HttpStatus.CREATED);
    }

}
