package org.example.model;


public class Produit {

    private Long id;
    private static Long count = 1L;
    private String name;

    private Double price;

    public Produit(String name, Double price) {
        this.id = count++;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static Long getCount() {
        return count;
    }

    public static void setCount(Long count) {
        Produit.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}