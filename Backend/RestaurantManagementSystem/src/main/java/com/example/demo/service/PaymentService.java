package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Payment;
import com.example.demo.entity.TransactionDetails;
import com.razorpay.Order;

public interface PaymentService {
	Payment addPayment(Payment payment, long orderId, long customerId);

	List<Payment> getAllPayments();

	public Optional<Payment> findPaymentById(long id);

	void deletePayment(long paymentId);

	List<Payment> getPaymentsByOrderId(long orderId);

	public void deletePaymentsByOrderId(long orderId);

	public List<Payment> findPaymentsByCustomerId(long customerId);
	
	public TransactionDetails createTransaction(Double amount);
	
	public TransactionDetails prepareTransactionDetails(Order order);
	
	public Payment addCashPayment(Payment payment, long orderId);
	
}
