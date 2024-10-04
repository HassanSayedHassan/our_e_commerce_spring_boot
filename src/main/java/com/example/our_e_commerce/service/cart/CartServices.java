package com.example.our_e_commerce.service.cart;

import com.example.our_e_commerce.dto.CartDto;
import com.example.our_e_commerce.exceptions.ResourceNotFoundException;
import com.example.our_e_commerce.model.Cart;
import com.example.our_e_commerce.model.User;
import com.example.our_e_commerce.repository.CartItemRepository;
import com.example.our_e_commerce.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartServices  implements ICartServices {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);


    @Override
    public Cart getCartById(Long id) {
        Cart cart=cartRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with id: " + id)
        );
        cart.setTotalAmount(cart.getTotalAmount());
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long cartId) {
        Cart cart=getCartById(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cart.getItems().clear();
        cartRepository.deleteById(cartId);
    }


    @Override
    public BigDecimal getTotalAmount(Long cartId) {
        Cart cart=getCartById(cartId);
        return cart.getTotalAmount();
    }



    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    @Override
    public CartDto convertToDto(Cart cart){
        return modelMapper.map(cart, CartDto.class);
    }
}
