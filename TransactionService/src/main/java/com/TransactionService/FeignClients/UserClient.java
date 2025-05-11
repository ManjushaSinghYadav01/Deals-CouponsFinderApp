package com.TransactionService.FeignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.TransactionService.DTO.UserDTO;

@FeignClient(name = "USER-SERVICE", path = "/user")
public interface UserClient {

	
	  @GetMapping("/wallet/{userId}")
	    public Double getUserWalletBalance(@PathVariable Long userId);

	   //void updateWalletAfterTransaction(Long userId, double walletUsed, double cashback);

	  @PutMapping("/{userId}/{walletUsed}/{cashback}")
	  void updateWalletAfterTransaction(@PathVariable("userId") Long userId,@PathVariable("walletUsed") double walletUsed,@PathVariable("cashback") double cashback
	  );

}
