package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.DTOS.OrderResponseDTO;
import com.merx_commerce.demo.entities.DTOS.UserResponseDTO;
import com.merx_commerce.demo.entities.Order;
import com.merx_commerce.demo.entities.User;
import com.merx_commerce.demo.exceptions.IsNotYoursException;
import com.merx_commerce.demo.exceptions.NotFoundException;
import com.merx_commerce.demo.repositories.OrderRepository;
import com.merx_commerce.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserService userService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    //admin
    public List<OrderResponseDTO> findAllOrders(){
        return orderRepository.findAll().stream().map(OrderResponseDTO::new).toList();
    }

    //user
    public List<OrderResponseDTO> findOrderByToken(String authHeader){
        UserResponseDTO userDTO = userService.findUserByToken(authHeader);
        User user = userRepository.findUserByEmail(userDTO.email());
        return user.getOrders().stream().map(OrderResponseDTO::new).toList();
    }

    //user e admin
    public OrderResponseDTO findOrderById(Long id,String authHeader){
        UserResponseDTO userDTO = userService.findUserByToken(authHeader);
        User user = userRepository.findUserByEmail(userDTO.email());
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist Order with this ID"));
        if(!user.getOrders().contains(order)) throw new IsNotYoursException("This Order is not your");
        return new OrderResponseDTO(order);
    }

}
