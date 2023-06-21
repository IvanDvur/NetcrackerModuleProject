package com.netcracker.dataservice.controllers.api;

import com.netcracker.dataservice.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BillingPlanController {

    private final SubscriptionPlanService paymentPlanService;

    @PostMapping
    public void changePaymentPlan(String token, String plan) {
        this.paymentPlanService.changeCustomerPaymentPlan(token, plan);
    }


}
