package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dao.PaymentRepository;
import com.example.demo.entity.Payment;
import com.example.demo.service.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PaymentTestCase {
	@InjectMocks
	PaymentServiceImpl payt;

	@Mock
	PaymentRepository paydao;

	@Test
	public void getAllPaymentsTest() throws ParseException {
		
		
		
		List<Payment> payments = new ArrayList<>();
		payments.add(new Payment());
		payments.add(new Payment());
		

		when(paydao.findAll()).thenReturn(payments);

		List<Payment> result = paydao.findAll();

		assertEquals(2, result.size());
		verify(paydao, times(1)).findAll();
	}

	@Test
	public void deletePaymentTest() throws ParseException {
		long paymentId = 1L;
		when(paydao.existsById(paymentId)).thenReturn(true);
		doNothing().when(paydao).deleteById(paymentId);

		payt.deletePayment(paymentId);

		verify(paydao, times(1)).deleteById(paymentId);
	}
	@Test
	public void getAllPaymentsByCustomerIdTest() throws ParseException {
		List<Payment> payments = new ArrayList<>();
		payments.add(new Payment());
		payments.add(new Payment());

		when(paydao.findByOrderId(anyLong())).thenReturn(payments);

		List<Payment> result = payt.findPaymentsByCustomerId(1L);

		assertEquals(2, result.size());
		verify(paydao, times(1)).findByOrderId(1L);
	}
	
	
//	@Test
//	public void addPaymentTest() throws ParseException {
//		Order order = new Order();
//		order.setTotalPrice(100.0);
//
//		Customer customer = new Customer();
//
//		Optional<Order> optionalOrder = Optional.of(order);
//
//		when(oderdao.findById(anyLong())).thenReturn(optionalOrder);
//		when(customert.findUserById(anyLong())).thenReturn(Optional.of(customer));
//
//		Payment payment = new Payment();
//		Payment addedPayment = payt.addPayment(payment, 1L, 1L);
//
//		assertNotNull(addedPayment);
//		assertEquals("PAID", order.getStatus());
//		verify(paydao, times(1)).save(payment);
//	}

}
