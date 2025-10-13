package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.*;
import com.merx_commerce.demo.entities.DTOS.*;
import com.merx_commerce.demo.exceptions.OutOfStockException;
import com.merx_commerce.demo.repositories.CartItemRepository;
import com.merx_commerce.demo.repositories.CartRepository;
import com.merx_commerce.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemService cartItemService;

    private Product p1;

    private User u1;

    private Cart c1;

    @BeforeEach
        void setup(){
        p1 = Product.builder()
                .name("keyboard")
                .description("a magic keyboard")
                .price(new BigDecimal(250))
                .quantityInStock(10)
                .images(new ArrayList<>())
                .category(new Category(1L,"test", null))
                .build();

        u1 = User.builder()
                .name("leandro")
                .cpf("13270080921")
                .email("leandro@email.com")
                .build();

        c1 = Cart.builder()
                .user(u1)
                .items(new ArrayList<>())
                .build();

        u1.setCart(c1);
        }

    @Test
    void testCreateCartItem(){
        UserResponseDTO userDTO = new UserResponseDTO(u1);
        when(userService.findUserByToken(anyString())).thenReturn(userDTO);
        when(userRepository.findUserByEmail(userDTO.email())).thenReturn(u1);
        when(cartRepository.save(any(Cart.class))).thenReturn(u1.getCart());
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(1L,p1,u1.getCart(),1,new BigDecimal(250));
        CartItemResponseDTO cartItemResponseDTO = cartItemService.createCartItem(cartItemRequestDTO,"fake-token");

        Assertions.assertNotNull(cartItemResponseDTO);
        Assertions.assertEquals(p1.getName(),cartItemResponseDTO.product().getName());
        Assertions.assertEquals(1,u1.getCart().getItems().size());
        Assertions.assertEquals(1,cartItemResponseDTO.cart().getItems().size());
    }

    @Test
    void testCreateCartItemFailed(){
        UserResponseDTO userDTO = new UserResponseDTO(u1);
        when(userService.findUserByToken(anyString())).thenReturn(userDTO);
        when(userRepository.findUserByEmail(userDTO.email())).thenReturn(u1);
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(1L,p1,u1.getCart(),1,new BigDecimal(250));
        OutOfStockException exception = Assertions.assertThrows(OutOfStockException.class, () -> cartItemService.createCartItem(cartItemRequestDTO,"fake-token")); //trocar quantidade no estoque dentro do BeforeEach

        Assertions.assertEquals("This product is out of stock",exception.getMessage());
    }

    @Test
    void testUpdateCartItem(){
        UserResponseDTO userDTO = new UserResponseDTO(u1);
        when(userService.findUserByToken(anyString())).thenReturn(userDTO);
        when(userRepository.findUserByEmail(userDTO.email())).thenReturn(u1);
        CartItem cartItem = new CartItem(1L,p1,u1.getCart(),1,new BigDecimal(250));
        CartItemRequestDTO cartItemRequestDTO = new CartItemRequestDTO(2L,p1,u1.getCart(),2,new BigDecimal(500));
        u1.getCart().setItems(List.of(cartItem));
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        CartItemResponseDTO cartItemResponseDTO = cartItemService.updateCartItem(cartItemRequestDTO,1L,"fake-token");

        Assertions.assertEquals(2, cartItemResponseDTO.quantity());
        Assertions.assertEquals(u1.getCart(),cartItemResponseDTO.cart());
    }
}
