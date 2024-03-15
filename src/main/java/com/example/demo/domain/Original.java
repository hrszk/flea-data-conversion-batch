package com.example.demo.domain;

import javax.validation.constraints.NotNull;

public class Original {

    private Integer id;
    private String name;
    private Integer conditionId;
    
    // @NotNull
    private String categoryName;
    private String categoryName1;
    private String categoryName2;
    private String categoryName3;
    private String brand;
    private double price;
    private Integer shipping;
    private String description;

    // カテゴリー名を分割
    public void divideCategoryName(){

        String[] parts=categoryName.split("/");
        categoryName1=parts[0];
        categoryName2=parts[1];
        categoryName3=parts[2];
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

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName1() {
        return categoryName1;
    }

    public void setCategoryName1(String categoryName1) {
        this.categoryName1 = categoryName1;
    }

    public String getCategoryName2() {
        return categoryName2;
    }

    public void setCategoryName2(String categoryName2) {
        this.categoryName2 = categoryName2;
    }

    public String getCategoryName3() {
        return categoryName3;
    }

    public void setCategoryName3(String categoryName3) {
        this.categoryName3 = categoryName3;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getShipping() {
        return shipping;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Original [id=" + id + ", name=" + name + ", conditionId=" + conditionId + ", categoryName="
                + categoryName + ", categoryName1=" + categoryName1 + ", categoryName2=" + categoryName2
                + ", categoryName3=" + categoryName3 + ", brand=" + brand + ", price=" + price + ", shipping="
                + shipping + ", description=" + description + "]";
    }

    
}
