package com.example.our_e_commerce.service.order;

import com.example.our_e_commerce.dto.OrderDto;
import com.example.our_e_commerce.model.Order;

import java.util.List;

public interface IOrderService {

    OrderDto placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
