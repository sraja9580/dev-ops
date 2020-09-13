package com.raja.practice.coupon.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raja.practice.coupon.modal.Coupon;
import com.raja.practice.coupon.repo.CouponRepo;

@RestController
@RequestMapping("/coupons")
public class CouponResource {
	
	@Autowired
	CouponRepo couponRepo;
	
	@PostMapping
	public Coupon save(@RequestBody Coupon coupon) {		 
		return   couponRepo.save(coupon);
	}
	
	@GetMapping("/coupon/{couponCode}")
	public Coupon getCoupon(@PathVariable String couponCode) {
		return  couponRepo.findByCode(couponCode);
	}

}
