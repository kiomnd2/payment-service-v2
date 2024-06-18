package com.subprj.paymentv2.interfaces.payment.view;

import com.subprj.paymentv2.common.utils.IdempotencyCreator;
import com.subprj.paymentv2.domain.payment.checkout.CheckoutCommand;
import com.subprj.paymentv2.domain.payment.checkout.CheckoutResult;
import com.subprj.paymentv2.domain.payment.checkout.CheckoutUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class CheckoutController {
    private final CheckoutUseCase checkoutUseCase;

    @GetMapping("/")
    public String checkoutPage(Model model, CheckoutDto.CheckoutRequest request) {
        CheckoutCommand command = CheckoutCommand.builder()
                .cartId(request.getCartId())
                .buyerId(request.getBuyerId())
                .productIds(request.getProductIds())
                .idempotencyKey(IdempotencyCreator.create(request.getSeed()))
                .build();
        CheckoutResult checkout = checkoutUseCase.checkout(command);
        model.addAttribute("orderId", checkout.getOrderId());
        model.addAttribute("orderName", checkout.getOrderName());
        model.addAttribute("amount", checkout.getAmount());
        return "checkout";
    }
}
