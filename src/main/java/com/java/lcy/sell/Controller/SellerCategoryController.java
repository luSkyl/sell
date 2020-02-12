package com.java.lcy.sell.Controller;

import com.java.lcy.sell.Entity.ProductCategory;
import com.java.lcy.sell.Exception.SellException;
import com.java.lcy.sell.Form.CategoryForm;
import com.java.lcy.sell.Repository.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/category")
@Slf4j
public class SellerCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                             @RequestParam(value = "size",defaultValue = "10")Integer size,
                             Map<String,Object> map){
        List<ProductCategory> categoryList = productCategoryService.findAll();
        map.put("categoryList",categoryList);
        return  new ModelAndView("category/list",map);
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId",required = false)Integer categoryId,
                              Map<String,Object>map){
      if(categoryId != null){
          ProductCategory category = productCategoryService.findById(categoryId);
          map.put("category",category);
      }
        return  new ModelAndView("category/index",map);
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm categoryForm,
                             BindingResult bindingResult,
                             Map<String,Object>map){
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/category/index");
            log.error("【卖家端保存商品类目信息】 商品类目信息保存失败");
            return new ModelAndView("common/error", map);
        }
        try {
            Integer categoryId = categoryForm.getCategoryId();
            ProductCategory category = new ProductCategory(null,null);
            if(categoryId !=null){
                category = productCategoryService.findById(categoryId);
            }
            BeanUtils.copyProperties(categoryForm,category);
            productCategoryService.save(category);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/category/index");
            log.error("【卖家端保存商品类目信息】 商品类目信息保存失败");
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/sell/seller/category/list");
        return new ModelAndView("common/success",map);
    }

}
