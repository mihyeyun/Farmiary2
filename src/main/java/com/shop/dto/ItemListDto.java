package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ItemListDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private ItemSellStatus itemSellStatus;

    private String createdBy;

    private LocalDateTime updateTime;

    private String imgUrl;

    private Integer price;

    @QueryProjection
    public ItemListDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price, ItemSellStatus itemSellStatus,
                       String createdBy, LocalDateTime updateTime){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.itemSellStatus = itemSellStatus;
        this.createdBy = createdBy;
        this.updateTime = updateTime;
    }

}