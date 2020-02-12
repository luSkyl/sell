package com.java.lcy.sell.Service.Impl;

import com.java.lcy.sell.Config.UpYunConfig;
import com.java.lcy.sell.Dto.CartDto;
import com.java.lcy.sell.Entity.ProductInfo;
import com.java.lcy.sell.Enums.ResultEnum;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Repository.ProductInfoRepository;
import com.java.lcy.sell.Service.ProductInfoService;
import com.java.lcy.sell.Enums.ProductStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
//@CacheConfig(cacheNames = "productInfo")
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private UpYunConfig upYunConfig;

    @Override
    //@Cacheable(key = "123")
    public ProductInfo findById(String productId) {
        Optional<ProductInfo> productInfo = productInfoRepository.findById(productId);
            productInfo.ifPresent(e -> e.addImageHost(upYunConfig.getImageHost()));
        return productInfo.orElse(null);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return  productInfoRepository.findAll(pageable);
    }

    @Override
   // @CachePut(key = "123")
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto : cartDtoList){
            ProductInfo productInfo = productInfoRepository.findById(cartDto.getProductId()).orElse(null);
            Integer productStock = productInfo.getProductStock();
            if(null == productInfo){
                log.error("【增加库存】 商品不存在 cartDto={}",cartDto);
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productStock + cartDto.getProductQuantity();
            if(result < 0){
                log.error("【增加库存】 商品数量不足 productStock={}",productStock);
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto : cartDtoList){
            ProductInfo productInfo = productInfoRepository.findById(cartDto.getProductId()).orElse(null);
            Integer productStock = productInfo.getProductStock();
            if(null == productInfo){
                log.error("【减少库存】 商品不存在 cartDto={}",cartDto);
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productStock - cartDto.getProductQuantity();
            if(result < 0){
                log.error("【减少库存】 商品数量不足 productStock={}",productStock);
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);
        }
    }

    @Override
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = productInfoRepository.findById(productId).orElse(null);
        if(null == productInfo){
            log.error("【商品上架】 商品不存在");
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if(productInfo.getProductStatusEnum() == ProductStatusEnum.UP){
            log.error("【商品上架】 商品状态不正确 productInfo={}",productInfo);
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return  productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = productInfoRepository.findById(productId).orElse(null);
        if(null == productInfo){
            log.error("【商品上架】 商品不存在");
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if(productInfo.getProductStatusEnum() == ProductStatusEnum.DOWN){
            log.error("【商品上架】 商品状态不正确 productInfo={}",productInfo);
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return  productInfoRepository.save(productInfo);
    }
}
