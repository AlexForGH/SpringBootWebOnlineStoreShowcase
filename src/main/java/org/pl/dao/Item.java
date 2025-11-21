package org.pl.dao;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", length = 255, nullable = false)
    private String title;
    @Column(name = "img_path", length = 500)
    private String imgPath;
    @Column(name = "price", length = 500, nullable = false)
    private BigDecimal price;
    @Column(name = "description", nullable = false)
    private String description;

    public Item() {
    }

    public Item(String title, String imgPath, BigDecimal price, String description) {
        this.title = title;
        this.imgPath = imgPath;
        this.price = price;
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
