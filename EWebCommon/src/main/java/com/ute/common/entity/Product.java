package com.ute.common.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, length = 255, nullable = false)
	private String name;

	@Column(length = 2048)
	private String specifications;

	@Column(length = 2048)
	private String description;

	@Column(name = "created_time", nullable = false, updatable = false)
	private Date createdTime;

	@Column(name = "updated_time")
	private Date updatedTime;

	private boolean enabled;

	@Column(name = "in_stock")
	private boolean inStock;

	private float cost;

	private float price;

	@Column(name = "discount_percent")
	private float discountPercent;

	@Column(name = "main_image", nullable = false)
	private String mainImage;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany
	@JoinColumn(name = "product_id")
	private Set<ProductImage> productImages;
	
	private int reviewCount;
	private float averageRating;

	@Transient
	private boolean customerCanReview;
	@Transient
	private boolean reviewedByCustomer;

	public Product() {
	}

	public Product(Integer id) {
		this.id = id;
	}

	public Product(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + "]";
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	public Set<ProductImage> getProductImages() {
		return productImages;
	}
	public void setProductImages(Set<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public void addExtraImage(ProductImage productImage){
		this.productImages.add(productImage);
	}
	@Transient
	public float getDiscountPrice() {
		if (discountPercent > 0) {
			return price * ((100 - discountPercent) / 100);
		}
		return this.price;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public boolean isCustomerCanReview() {
		return customerCanReview;
	}

	public void setCustomerCanReview(boolean customerCanReview) {
		this.customerCanReview = customerCanReview;
	}

	public boolean isReviewedByCustomer() {
		return reviewedByCustomer;
	}

	public void setReviewedByCustomer(boolean reviewedByCustomer) {
		this.reviewedByCustomer = reviewedByCustomer;
	}

}