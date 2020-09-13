package com.raja.practice.coupon.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raja.practice.coupon.modal.Coupon;

public interface CouponRepo extends JpaRepository<Coupon, Long>{
	public Coupon findByCode(String couponCode);
}
