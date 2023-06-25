package com.netcracker.dataservice.controllers.api;



import com.netcracker.dataservice.service.SubscriptionPlanService;
import dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tariff")
@CrossOrigin
public class BillingPlanController {

    private final SubscriptionPlanService paymentPlanService;

    @PostMapping(value = "/change",produces = "application/json")
    public ResponseEntity<TokenDto> changePaymentPlan(@RequestHeader("Authorization") String token, @RequestParam("plan") String plan) {
       return this.paymentPlanService.changeCustomerPaymentPlan(token, plan);
    }


}
