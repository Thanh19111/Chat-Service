package com.thanhpham.chat.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class BillDetail{
    private String customerName;
    private String address;
    private String phoneNumber;
    private List<ProductDetail> list;
    private long totalAmount;
}
