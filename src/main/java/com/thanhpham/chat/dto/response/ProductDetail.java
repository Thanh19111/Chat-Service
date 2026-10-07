package com.thanhpham.chat.dto.response;

import lombok.Data;

@Data
public class ProductDetail{
    private String id;
    private String productName;
    private String calculationUnit;
    private Integer quantity;
    private Long price;
}
