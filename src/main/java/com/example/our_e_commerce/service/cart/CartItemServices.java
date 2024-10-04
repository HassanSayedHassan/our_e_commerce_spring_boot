package com.example.our_e_commerce.service.cart;

import com.example.our_e_commerce.exceptions.ResourceNotFoundException;
import com.example.our_e_commerce.model.Cart;
import com.example.our_e_commerce.model.CartItem;
import com.example.our_e_commerce.model.Product;
import com.example.our_e_commerce.repository.CartItemRepository;
import com.example.our_e_commerce.repository.CartRepository;
import com.example.our_e_commerce.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemServices implements ICartItemServices {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartServices cartServices;

    @Override
    public void addCartItem(Long cartId, Long productId, int quantity) {
        //1- Get the cart
        //2- Get the product
        //3- Check if the cartItem already exists
        //4- IF yes, update the quantity
        //5- IF no, create a new cartItem

        Cart cart=cartServices.getCartById(cartId);
        Product product=productService.getProductById(productId);
        CartItem cartItem=cart.getItems()
                .stream().filter(cartItem1 -> cartItem1.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());

        if(cartItem.getId()==null){
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItem.setUnitPrice(product.getPrice());
        }else{
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(Long cartId, Long productId) {
        Cart cart=cartServices.getCartById(cartId);
        CartItem itemToRemove=getCartItem(cartId, productId);

        cart.removeItem(itemToRemove);
//        cartItemRepository.delete(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItem(Long cartId, Long productId, int quantity) {
        Cart cart=cartServices.getCartById(cartId);
        cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst().ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

        BigDecimal totalAmount=cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }


    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartServices.getCartById(cartId);
        return  cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}
