package com.example.ps.api.service;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ps.api.entity.Payment;
import com.example.ps.api.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	private Logger logger = LoggerFactory.getLogger(PaymentService.class);
	
	public Payment doPayment(Payment payment) throws JsonProcessingException
	{
		payment.setPaymentStatus(paymentProcessing());
		payment.setTransactionId(UUID.randomUUID().toString());
		logger.info("Payment Service request : {}",new ObjectMapper().writeValueAsString(payment));
		return	paymentRepository.save(payment);
	}

	public String paymentProcessing()
	{
		//api should be third party gateway
		return new Random().nextBoolean()?"Success":"false";
	}

	public Payment findPaymentHistoryByOrderId(int orderId) throws JsonProcessingException {
		Payment payment = paymentRepository.findByOrderId(orderId);		
		logger.info("Payment Service findPaymentHistoryByOrderId : {}",new ObjectMapper().writeValueAsString(payment));
		return payment;
	}
	
}

