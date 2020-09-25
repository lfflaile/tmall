package com.how2j.tmall.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.ProductImage;
import com.how2j.tmall.service.CategoryService;
import com.how2j.tmall.service.ProductImageService;
import com.how2j.tmall.service.ProductService;
import com.how2j.tmall.util.ImageUtil;

@RestController
public class ProductImageController {
	//产品
	@Autowired
	ProductService productService;
	//产品图片
	@Autowired
	ProductImageService productImageService;
	//分类
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/products/{pid}/productImages")
	public List<ProductImage> list(@RequestParam("type") String type,@PathVariable("pid") int pid){
		Product product = productService.get(pid);
		//type = singles是单个图片
		//type = details是详情图片
		if(ProductImageService.type_single.equals(type)){
			List<ProductImage> singles = productImageService.listSingleProductImages(product);
			return singles;
		}else if(ProductImageService.type_detail.equals(type)){
			List<ProductImage> detail = productImageService.listDetailProductImages(product);
			return detail;
		}else{
			return new ArrayList<>();
		}
	}
	
	@PostMapping("/productImages")
	public Object add(@RequestParam("pid") int pid,@RequestParam("type") String type,MultipartFile image,HttpServletRequest request){
		ProductImage bean = new ProductImage();
		Product product = productService.get(pid);
		bean.setProduct(product);
		bean.setType(type);
		
		productImageService.add(bean);
		String folder = "img/";
        if(ProductImageService.type_single.equals(bean.getType())){
            folder +="productSingle";
        }else{
            folder +="productDetail";
        }
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,bean.getId()+".jpg");
        String fileName = file.getName();
        if(!file.getParentFile().exists()){
        	file.getParentFile().mkdir();
        }        try {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);           
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(ProductImageService.type_single.equals(bean.getType())){
        	String imageFoler_small = request.getServletContext().getRealPath("img/productSingle_small");
        	String imageFolder_middler = request.getServletContext().getRealPath("img/productSingle_middle");
        	File f_small = new File(imageFoler_small,fileName);
        	File f_middle = new File(imageFolder_middler,fileName);
        	f_small.getParentFile().mkdirs();
        	f_middle.getParentFile().mkdirs();
        	ImageUtil.resizeImage(file,56,56,f_small);
        	ImageUtil.resizeImage(file,217,190,f_middle);
        }
        return bean;
	}
	
	@DeleteMapping("/productImages/{id}")
	public String delete(@PathVariable("id") int id,HttpServletRequest request){
		ProductImage bean = productImageService.get(id);
		productImageService.delete(id);
		String folder = "img/";
        if(ProductImageService.type_single.equals(bean.getType()))
            folder +="productSingle";
        else
            folder +="productDetail";
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,bean.getId()+".jpg");
        String fileName = file.getName();
        file.delete();
        if(ProductImageService.type_single.equals(bean.getType())){
        	String imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
        	String imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");
        	File f_small = new File(imageFolder_small,fileName);
        	File f_middle = new File(imageFolder_middle,fileName);
        }
		return null;
	}
	
}
