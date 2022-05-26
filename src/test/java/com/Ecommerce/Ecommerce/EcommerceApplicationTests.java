package com.Ecommerce.Ecommerce;

import com.Ecommerce.Ecommerce.Entity.Seller;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class EcommerceApplicationTests {

	@Autowired
	SellerRepository sellerRepository;
	@Test
	void contextLoads() {
	}


}
