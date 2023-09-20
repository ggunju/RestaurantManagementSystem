package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Payment;
import com.example.demo.entity.TransactionDetails;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.example.demo.entity.Customer;

import com.example.demo.dao.OrdersRepository;
import com.example.demo.dao.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {
	private static final String KEY = "rzp_test_5CdqjnozwLTyLd";
	private static final String KEY_SECRET = "nYVGveoMLvZ2c7nNLhRq1e5z";
	private static final String CURRENCY = "INR";

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private OrdersRepository orderRepository;

	@Autowired
	private CustomerService customerService;

	public PaymentServiceImpl(PaymentRepository paymentRepository, CustomerService customerService) {
		super();
		this.paymentRepository = paymentRepository;

		this.customerService = customerService;
	}

	@Override
	public Payment addPayment(Payment payment, long orderId, long customerId) {
		@SuppressWarnings("deprecation")
		com.example.demo.entity.Order order = orderRepository.getById(orderId);
		payment.setOrderId(orderId);
		payment.setTotalPrice(order.getTotalPrice());
		payment.setPaidDate(LocalDate.now());
		payment.setPaidAmount(order.getTotalPrice());

		if (payment.getTotalPrice() == payment.getPaidAmount()) {
			order.setStatus("Paid");
		} else {

			order.setStatus("Not Paid");
		}
		Customer customer = customerService.findUserById(customerId).orElse(null);
		payment.setCustomer(customer);
		return paymentRepository.save(payment);
	}

	@Override
	public List<Payment> getAllPayments() {
		return paymentRepository.findAll();
	}

	@Override
	public void deletePayment(long paymentId) {
		paymentRepository.findById(paymentId);
		if (paymentRepository.existsById(paymentId)) {
			paymentRepository.deleteById(paymentId);
		}
	}

	@Override
	public Optional<Payment> findPaymentById(long id) {
		return this.paymentRepository.findById(id);
	}

	@Override
	public List<Payment> getPaymentsByOrderId(long orderId) {
		return paymentRepository.findByOrderId(orderId);
	}

	@Override
	public void deletePaymentsByOrderId(long orderId) {
		paymentRepository.findByOrderId(orderId).forEach(payment -> paymentRepository.delete(payment));

	}

	@Override
	public List<Payment> findPaymentsByCustomerId(long customerId) {
		return paymentRepository.findByCustomerCustomerId(customerId);
	}

	@Override
	public TransactionDetails createTransaction(Double amount) {
		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("amount", (amount * 100));// paise to ruppees
			jsonObject.put("currency", CURRENCY);

			RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);

			Order order = razorpayClient.orders.create(jsonObject);

			return prepareTransactionDetails(order);
		} catch (RazorpayException e) {

			System.out.print(e.getMessage());
		}
		return null;
	}

	@Override
	public TransactionDetails prepareTransactionDetails(Order order) {
		String rorderId = order.get("id");
		String currency = order.get("currency");
		Integer amount = order.get("amount");

		TransactionDetails transactionDetails = new TransactionDetails(rorderId, currency, amount, KEY);
		return transactionDetails;

	}

	@SuppressWarnings("deprecation")
	@Override
	public Payment addCashPayment(Payment payment, long orderId ){
		com.example.demo.entity.Order order = orderRepository.getById(orderId);
		Customer customer = customerService.findUserById(order.getCustomer().getCustomerId()).orElse(null);
		payment.setOrderId(orderId);
		payment.setCustomer(customer);
		payment.setTotalPrice(order.getTotalPrice());
		payment.setPaidDate(LocalDate.now());
		payment.setPaidAmount(order.getTotalPrice());
		payment.setRazorpayId("CASH");
		order.setStatus("Paid");
		return paymentRepository.save(payment);
	}

}
