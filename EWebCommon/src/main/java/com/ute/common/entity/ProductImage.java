package com.ute.common.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_images")
public class ProductImage {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="extra_image")
	private String extraImage;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	public ProductImage() {
	}

	public ProductImage(Integer id,  Product product) {
		this.id = id;
		this.product = product;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExtraImage() {
		return extraImage;
	}

	public void setExtraImage(String extraImage) {
		this.extraImage = extraImage;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}