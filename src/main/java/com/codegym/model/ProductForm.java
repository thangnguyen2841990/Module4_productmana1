package com.codegym.model;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;

public class ProductForm {
    private Long id;
    @NotEmpty(message = "Khong duoc de trong!")
    @NotBlank(message = "Khong duoc nhap khoang trang!")
    @Size(min = 6, max = 20, message = "Ten san pham tu 6 - 20 ky tu!")
    private String name;
    @NotNull(message = "Hay nhap gia tien!")
    @Min(value = 1000, message = "Gia thap nhat la 1000(VND)")
    private double price;
    @NotNull(message = "Hay nhap so luong san pham!")
    @Min(value = 1,message = "So luong nho nhat la 1!")
    private int quantity;
    @NotEmpty(message = "Khong duoc de trong!")
    @NotBlank(message = "Khong duoc nhap khoang trang!")
    @Size(min = 5, message = "it nhat 5 ky tu!")
    private String description;
    private MultipartFile image;
    private Category category;

    public ProductForm() {
    }

    public ProductForm(String name, double price, int quantity, String description, MultipartFile image, Category category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    public ProductForm(Long id, String name, double price, int quantity, String description, MultipartFile image, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
