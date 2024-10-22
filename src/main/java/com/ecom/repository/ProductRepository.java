package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import com.ecom.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	List<Product> findByIsActiveTrue();

	List<Product> findByCategoryAndIsActiveTrue(String category);

}
