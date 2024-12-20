package com.ecom.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProduct() {

		return productRepository.findAll();
	}

	@Override
	public Boolean deleteProduct(int id) {

		Product product = productRepository.findById(id).orElse(null);
		if (!ObjectUtils.isEmpty(product)) {
			productRepository.delete(product);
			return true;
		}

		return false;
	}

	@Override
	public Product getProductById(int id) {
		Product product = productRepository.findById(id).orElse(null);
		return product;
	}

	@Override
	public Product updateProduct(Product product, MultipartFile image) {
	    Product dbProduct = getProductById(product.getId());

	    String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();

	    dbProduct.setTitle(product.getTitle());
	    dbProduct.setDescription(product.getDescription());
	    dbProduct.setCategory(product.getCategory());
	    dbProduct.setPrice(product.getPrice());
	    dbProduct.setStock(product.getStock());
	    dbProduct.setIsActive(product.getIsActive());
	    dbProduct.setImage(imageName);
	    
	    dbProduct.setDiscount(product.getDiscount());

	    // Tính toán giá trị giảm giá với BigDecimal
	    BigDecimal price = BigDecimal.valueOf(product.getPrice());
	    BigDecimal discountRate = BigDecimal.valueOf(product.getDiscount()).divide(BigDecimal.valueOf(100));
	    BigDecimal discount = price.multiply(discountRate);
	    
	    // Tính toán giá sau giảm giá
	    BigDecimal discountPrice = price.subtract(discount).setScale(2, RoundingMode.HALF_UP);
	    dbProduct.setDiscountPrice(discountPrice.doubleValue()); // Chuyển đổi về kiểu double
	    
	    Product updateProduct = productRepository.save(dbProduct);
	    if (!ObjectUtils.isEmpty(updateProduct)) {
	        if (!image.isEmpty()) {
	            try {
	                File saveFile = new ClassPathResource("static/img").getFile();

	                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
	                        + image.getOriginalFilename());

	                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

	            } catch (Exception e) {
	                e.printStackTrace();
	            }            
	        }
	        return updateProduct; // Trả về sản phẩm đã được cập nhật
	    }

	    return null;
	}


	@Override
	public List<Product> getAllActiveProduct(String category) {
		List<Product> products = null;
		if (ObjectUtils.isEmpty(category)) {
			products = productRepository.findByIsActiveTrue();
		}
		else
			products = productRepository.findByCategoryAndIsActiveTrue(category);
		return products;
	}

}
