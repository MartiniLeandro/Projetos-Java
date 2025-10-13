package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.Cart;
import com.merx_commerce.demo.entities.Category;
import com.merx_commerce.demo.entities.DTOS.*;
import com.merx_commerce.demo.entities.Product;
import com.merx_commerce.demo.entities.User;
import com.merx_commerce.demo.exceptions.OutOfStockException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

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
                .quantityInStock(0)
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
        OutOfStockException exception = Assertions.assertThrows(OutOfStockException.class, () -> cartItemService.createCartItem(cartItemRequestDTO,"fake-token"));

        Assertions.assertEquals("This product is out of stock",exception.getMessage());
    }
}
