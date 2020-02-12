package com.java.lcy.sell.Controller;

import com.java.lcy.sell.Entity.ProductCategory;
import com.java.lcy.sell.Entity.ProductInfo;
import com.java.lcy.sell.Repository.ProductCategoryService;
import com.java.lcy.sell.Service.ProductInfoService;
import com.java.lcy.sell.Utils.ResultVoUtil;
import com.java.lcy.sell.Vo.ProductInfoVo;
import com.java.lcy.sell.Vo.ProductVo;
import com.java.lcy.sell.Vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    //@CacheEvict(cacheNames = "product",key = "123")
    //@Cacheable(cacheNames = "product",key = "#sellerId",condition = "#sellerId.length()>3",unless = "#result.getCode() != 0")
    public ResultVo list(@RequestParam(value = "sellerId",required = false)String sellerId ) {
        List<ProductInfo> productinfos = productInfoService.findUpAll();
        List<Integer> categoryTypeList = productinfos.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());
        List<ProductCategory> productCategorys = productCategoryService.findByCategoryTypeIn(categoryTypeList);
        List<ProductVo> productVoList = new ArrayList<>();
        for (ProductCategory productCategory : productCategorys) {
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVo> productInfoVoList = new ArrayList<>();
            for (ProductInfo productInfo : productinfos) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo, productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
            }
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);
        }
        return ResultVoUtil.success(productVoList);
    }
}
