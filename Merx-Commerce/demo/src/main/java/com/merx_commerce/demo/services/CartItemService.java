package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.CartItem;
import com.merx_commerce.demo.entities.DTOS.CartItemRequestDTO;
import com.merx_commerce.demo.entities.DTOS.CartItemResponseDTO;
import com.merx_commerce.demo.entities.DTOS.UserResponseDTO;
import com.merx_commerce.demo.entities.User;
import com.merx_commerce.demo.exceptions.IsNotYoursException;
import com.merx_commerce.demo.exceptions.NotFoundException;
import com.merx_commerce.demo.repositories.CartItemRepository;
import com.merx_commerce.demo.repositories.CartRepository;
import com.merx_commerce.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, UserService userService, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public List<CartItemResponseDTO> findAllCartsItems(){
        return cartItemRepository.findAll().stream().map(CartItemResponseDTO::new).toList();
    }

    public CartItemResponseDTO findById(Long id){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist CartItem with this ID"));
        return new CartItemResponseDTO(cartItem);
    }

    public CartItemResponseDTO createCartItem(CartItemRequestDTO cartItem, String authHeader){
        UserResponseDTO userDTO = userService.findUserByToken(authHeader);
        User user = userRepository.findUserByEmail(userDTO.email());
        CartItem newCartItem = new CartItem(cartItem);
        newCartItem.setCart(user.getCart());
        user.getCart().getItems().add(newCartItem);
        cartRepository.save(user.getCart());
        return new CartItemResponseDTO(newCartItem);
    }

    public CartItemResponseDTO updateCartItem(CartItemRequestDTO cartItem, Long id, String authHeader){
        UserResponseDTO userDTO = userService.findUserByToken(authHeader);
        User user = userRepository.findUserByEmail(userDTO.email());
        CartItem updatedCartItem = cartItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist CartItem with this ID"));
        if(!user.getCart().getItems().contains(updatedCartItem)) throw new IsNotYoursException("This CartItem is not your");
        updatedCartItem.setProduct(cartItem.product());
        updatedCartItem.setPrice(cartItem.price());
        updatedCartItem.setQuantity(cartItem.quantity());
        updatedCartItem.setCart(cartItem.cart());
        cartItemRepository.save(updatedCartItem);
        return new CartItemResponseDTO(updatedCartItem);
    }
}
