package com.maverick;

public class Product {

    private String objectId;
    private String name;
    private Integer cost;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Integer getCost() {
//        return cost;
//    }
//
//    public void setCost(Integer cost) {
//        this.cost = cost;
//    }
//


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("objectId='").append(objectId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", cost=").append(cost);
        sb.append('}');
        return sb.toString();
    }
}