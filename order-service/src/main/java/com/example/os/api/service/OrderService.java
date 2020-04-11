package com.example.os.api.service;

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
	
	
	public TransactionResponse saveOrder(TransactionRequest request)
	{
		String response = "";
		Order order = request.getOrder();
		Payment payment = request.getPayment();
		payment.setOrderId(order.getId());
		payment.setAmount(order.getPrice());
		Payment paymentResponse = restTemplate.postForObject(PAYMENT_ENDPOINT_URL, payment, Payment.class);
		
		response= paymentResponse.getPaymentStatus().equals("Success")?"Payment Processing Successful and Order Placed":"There is failure in Payment APi";
		
		orderRepository.save(order);
		return	new TransactionResponse(order,paymentResponse.getAmount(),paymentResponse.getTransactionId(),response);
	}
}
