package com.example.ps.api.service;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ps.api.entity.Payment;
import com.example.ps.api.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	public Payment doPayment(Payment payment)
	{
		payment.setPaymentStatus(paymentProcessing());
		payment.setTransactionId(UUID.randomUUID().toString());
		return	paymentRepository.save(payment);
	}

	public String paymentProcessing()
	{
		//api should be third party gateway
		return new Random().nextBoolean()?"Success":"false";
	}

	public Payment findPaymentHistoryByOrderId(int orderId) {
		// TODO Auto-generated method stub
		return paymentRepository.findByOrderId(orderId);
	}
	
}

