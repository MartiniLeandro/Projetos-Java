package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.Cart;
import com.merx_commerce.demo.entities.DTOS.CartRequestDTO;
import com.merx_commerce.demo.entities.DTOS.CartResponseDTO;
import com.merx_commerce.demo.entities.DTOS.UserResponseDTO;
import com.merx_commerce.demo.entities.User;
import com.merx_commerce.demo.exceptions.AlreadyExistsException;
import com.merx_commerce.demo.exceptions.NotFoundException;
import com.merx_commerce.demo.repositories.CartRepository;
import com.merx_commerce.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, UserService userService, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    //admin
    public List<CartResponseDTO> findAllCarts(){
        return cartRepository.findAll().stream().map(CartResponseDTO::new).toList();
    }

    //prioridade admin
    public CartResponseDTO findCartById(Long id){
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist Cart with this ID"));
        return new CartResponseDTO(cart);
    }

    //user
    public CartResponseDTO findCartByToken(String authHeader){
        UserResponseDTO user = userService.findUserByToken(authHeader);
        if(user.cart() == null) throw new NotFoundException("This User don't have an Cart");
        Cart cart = user.cart();
        return new CartResponseDTO(cart);
    }

    //user
    public CartResponseDTO createCart(CartRequestDTO cart, String authHeader){
        UserResponseDTO userDTO = userService.findUserByToken(authHeader);
        User user = userRepository.findUserByEmail(userDTO.email());
        if(user.getCart() != null){
            throw new AlreadyExistsException("You already have an cart");
        }
        Cart newCart = new Cart(cart);
        user.setCart(newCart);
        cartRepository.save(newCart);
        return new CartResponseDTO(newCart);
    }

    //user
    public CartResponseDTO updateCart(CartRequestDTO cart, String authHeader){
        UserResponseDTO userDTO = userService.findUserByToken(authHeader);
        User user = userRepository.findUserByEmail(userDTO.email());
        Cart updatedCart = user.getCart();
        updatedCart.setItems(cart.items());
        cartRepository.save(updatedCart);
        return new CartResponseDTO(updatedCart);
    }

    //prioridade admin
    public void deleteCartById(Long id){
        if(cartRepository.existsById(id)){
            cartRepository.deleteById(id);
        }else {
            throw new NotFoundException("Not exist Cart with this ID");
        }
    }

    //user
    public void deleteCartByToken(String authHeader){
        UserResponseDTO userDTO = userService.findUserByToken(authHeader);
        User user = userRepository.findUserByEmail(userDTO.email());
        cartRepository.delete(user.getCart());
    }
}
