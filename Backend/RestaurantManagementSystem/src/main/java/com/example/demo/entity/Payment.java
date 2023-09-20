package com.example.demo.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;	

@Entity
@Table(name = "payment_table")
@SequenceGenerator(name = "generator5", sequenceName = "gen5", initialValue = 20000)
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator5")
	@Column(name = "payment_id")
	private long paymentId;

	@Column(name = "total_price")
	private double totalPrice;

	@Column(name = "order_id", unique = true)
	private long orderId;

	@Column(name = "paid_date")
	private LocalDate PaidDate;

	@Column(name = "paid_amount", nullable = false)
	private double paidAmount;

	@NotBlank(message = "Id cannot be blank")
	@Column(name = "razorpay_id", nullable = false)
	private String razorpayId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "customer_id")

	private Customer customer;

	public Payment() {
	}

	public Payment(long paymentId, double totalPrice, long orderId, LocalDate paidDate, double paidAmount,
			String razorpayId, Customer customer) {
		super();
		this.paymentId = paymentId;
		this.totalPrice = totalPrice;
		this.orderId = orderId;
		PaidDate = paidDate;
		this.paidAmount = paidAmount;
		this.razorpayId = razorpayId;
		this.customer = customer;
	}

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public LocalDate getPaidDate() {
		return PaidDate;
	}

	public void setPaidDate(LocalDate paidDate) {
		PaidDate = paidDate;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getRazorpayId() {
		return razorpayId;
	}

	public void setRazorpayId(String razorpayId) {
		this.razorpayId = razorpayId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", totalPrice=" + totalPrice + ", orderId=" + orderId + ", PaidDate="
				+ PaidDate + ", paidAmount=" + paidAmount + ", razorpayId=" + razorpayId + ", customer=" + customer
				+ "]";
	}

}