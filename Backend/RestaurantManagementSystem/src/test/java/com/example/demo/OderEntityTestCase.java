package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dao.OrdersRepository;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Order;
import com.example.demo.service.CustomerServiceImpl;
import com.example.demo.service.MenuServiceImpl;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class OderEntityTestCase {
	@InjectMocks
	OrderServiceImpl ordert;

	@Mock
	OrdersRepository oderdao;

	@Mock
	private CustomerServiceImpl customerservice;

	@Mock
	private MenuServiceImpl menuservice;

	@Before(value = "")
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	// OrderController
	@Test
	void testGetAllOrders() throws ParseException {

		Order order1 = new Order();
		order1.setOrderId(1L);
		Order order2 = new Order();
		order2.setOrderId(2L);

		List<Order> OrderList = new ArrayList<>();
		OrderList.add(order1);
		OrderList.add(order2);

		when(oderdao.findAll()).thenReturn(OrderList);

		List<Order> resultOrderList = oderdao.findAll();

		assertEquals(2, resultOrderList.size());
		assertEquals(order1, resultOrderList.get(0));
		assertEquals(order2, resultOrderList.get(1));

	}

	@Test
	public void testSaveOrder() {
		// Mock customer and menu
		Customer customer = new Customer();
		when(customerservice.findUserById(anyLong())).thenReturn(Optional.of(customer));

		Menu menu = new Menu();
		when(menuservice.findByMenuId(anyInt())).thenReturn(Optional.of(menu));

		// Create an order
		Order order = new Order();
		order.setQuantity(2); // Set some quantity
		order.setStatus("Not Paid"); // Set status
		// You might need to set other properties as needed

		// Mock save method
		when(oderdao.save(any(Order.class))).thenReturn(order);

		// Call the method
		Order savedOrder = ordert.saveOrder(order, 1L, 1);

		// Verify the interactions

		verify(customerservice).findUserById(1L); 
        verify(menuservice).findByMenuId(1); 
        verify(oderdao).save(order); 

		// Assert the result
		assertNotNull(savedOrder);
		assertEquals(order.getCustomer(), customer);
		assertEquals(order.getMenu(), menu);
		assertEquals("Not Paid", order.getStatus());

	}

	@Test
	public void findOrderByIdTest() {
		oderdao.findById(1L);
		verify(oderdao).findById(1L);
	}

	@Test
	public void deleteOrderTest() throws ParseException {

		oderdao.deleteById(1L);
		verify(oderdao).deleteById(1L);

	}
}