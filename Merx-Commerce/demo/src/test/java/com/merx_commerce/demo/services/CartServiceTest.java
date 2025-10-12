package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.Cart;
import com.merx_commerce.demo.entities.CartItem;
import com.merx_commerce.demo.entities.DTOS.CartRequestDTO;
import com.merx_commerce.demo.entities.DTOS.CartResponseDTO;
import com.merx_commerce.demo.entities.DTOS.UserResponseDTO;
import com.merx_commerce.demo.entities.User;
import com.merx_commerce.demo.exceptions.AlreadyExistsException;
import com.merx_commerce.demo.exceptions.NotFoundException;
import com.merx_commerce.demo.repositories.CartRepository;
import com.merx_commerce.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartService cartService;

    private Cart c1,c2;
    private CartItem ci1,ci2;
    private User u1,u2;

    @BeforeEach
    void setup(){
        u1 = User.builder().name("leandro").cpf("20506380009").email("leandro@email.com").password("leandro").build();
        u2 = User.builder().name("leandro2").cpf("34598922001").email("leandro2@email.com").password("leandro2").build();
        c1 = Cart.builder().user(u1).build();
        c2 = Cart.builder().user(u2).build();
        ci1 = CartItem.builder().cart(c1).build();
        ci2 = CartItem.builder().cart(c2).build();
        c1.setItems(List.of(ci1));
        c2.setItems(List.of(ci2));
    }

    @Test
    void testFindAllCarts(){
        when(cartRepository.findAll()).thenReturn(List.of(c1,c2));
        List<CartResponseDTO> allCarts = cartService.findAllCarts();

        Assertions.assertNotNull(allCarts);
        Assertions.assertEquals(2,allCarts.size());
    }

    @Test
    void testFindCartById(){
        when(cartRepository.findById(anyLong())).thenReturn(Optional.ofNullable(c1));
        CartResponseDTO cart = cartService.findCartById(1L);

        Assertions.assertNotNull(cart);
        Assertions.assertEquals("leandro",cart.user().getName());
    }

    @Test
    void testFindCartByIdFailed(){
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> cartService.findCartById(1L));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Not exist Cart with this ID",exception.getMessage());
    }

    @Test
    void testFindCartByToken(){
        u1.setCart(c1);
        UserResponseDTO user = new UserResponseDTO(u1);
        when(userService.findUserByToken(anyString())).thenReturn(user);
        CartResponseDTO cart = cartService.findCartByToken("fake-token");
        Assertions.assertNotNull(cart);
        Assertions.assertEquals("leandro",cart.user().getName());
    }

    @Test
    void testFindCartByTokenFailed(){
        UserResponseDTO user = new UserResponseDTO(u2);
        when(userService.findUserByToken(anyString())).thenReturn(user);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> cartService.findCartByToken("fake-token"));

        Assertions.assertEquals("This User don't have an Cart",exception.getMessage());
    }

    @Test
    void testCreateCart(){
        UserResponseDTO user = new UserResponseDTO(u1);
        when(userService.findUserByToken(anyString())).thenReturn(user);
        when(userRepository.findUserByEmail(user.email())).thenReturn(u1);
        CartRequestDTO cartRequest = new CartRequestDTO(c2.getId(),c2.getUser(),c2.getItems());
        when(cartRepository.save(any(Cart.class))).thenReturn(u1.getCart());

        CartResponseDTO newCart = cartService.createCart(cartRequest, "fake-token");

        Assertions.assertNotNull(newCart);
        Assertions.assertEquals("leandro2", newCart.user().getName());
    }

    @Test
    void testCreateCartFailed(){
        UserResponseDTO user = new UserResponseDTO(u1);
        CartRequestDTO cartRequest = new CartRequestDTO(c2.getId(),c2.getUser(),c2.getItems());
        when(userService.findUserByToken(anyString())).thenReturn(user);
        when(userRepository.findUserByEmail(user.email())).thenReturn(u1);
        u1.setCart(c1);
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> cartService.createCart(cartRequest,"fake-token"));

        Assertions.assertEquals("You already have an cart", exception.getMessage());
    }
}