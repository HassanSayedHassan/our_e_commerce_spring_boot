package com.example.our_e_commerce.controller;

import com.example.our_e_commerce.dto.OrderDto;
import com.example.our_e_commerce.exceptions.ResourceNotFoundException;
import com.example.our_e_commerce.model.Order;
import com.example.our_e_commerce.responce.ApiResponse;
import com.example.our_e_commerce.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
       try {
           OrderDto order = orderService.placeOrder(userId);
           return ResponseEntity.ok(new ApiResponse("Success", order));
       }catch (Exception e) {
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
       }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        try {
            OrderDto order = orderService.getOrder(id);
            return ResponseEntity.ok(new ApiResponse("Success", order));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    //get all orders
    @GetMapping("/all/{userId}")
    public ResponseEntity<ApiResponse> getAllOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Success",orders ));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}

