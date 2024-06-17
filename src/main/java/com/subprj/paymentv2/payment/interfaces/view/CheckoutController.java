package com.subprj.paymentv2.payment.interfaces.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class CheckoutController {

    @GetMapping("/")
    public String checkoutPage(Model model, CheckoutDto.CheckoutRequest request) {
        return"checkout";
    }
}
