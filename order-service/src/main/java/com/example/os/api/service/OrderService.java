package com.example.os.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.os.api.common.Payment;
import com.example.os.api.common.TransactionRequest;
import com.example.os.api.common.TransactionResponse;
import com.example.os.api.entity.Order;
import com.example.os.api.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RefreshScope
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	@Lazy
	private RestTemplate restTemplate;
	
	@Value("${microservice.payment-service.endpoints.endpoint.uri}")
	private String PAYMENT_ENDPOINT_URL;
	
	private Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	public TransactionResponse saveOrder(TransactionRequest request) throws JsonProcessingException
	{
		String response = "";
		Order order = request.getOrder();
		Payment payment = request.getPayment();
		payment.setOrderId(order.getId());
		payment.setAmount(order.getPrice());
		logger.info("Order Service request : {}",new ObjectMapper().writeValueAsString(request));
		Payment paymentResponse = restTemplate.postForObject(PAYMENT_ENDPOINT_URL, payment, Payment.class);
		logger.info("Payment-Service response from Order Service rest call : {}",new ObjectMapper().writeValueAsString(paymentResponse));
		response= paymentResponse.getPaymentStatus().equals("Success")?"Payment Processing Successful and Order Placed":"There is failure in Payment APi";
		
		orderRepository.save(order);
		return	new TransactionResponse(order,paymentResponse.getAmount(),paymentResponse.getTransactionId(),response);
	}
}
