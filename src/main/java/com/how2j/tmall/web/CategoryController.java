package com.how2j.tmall.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.service.CategoryService;
import com.how2j.tmall.util.ImageUtil;
import com.how2j.tmall.util.Page4Navigator;

@RestController
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
/*    @GetMapping("/categories")
    public List<Category> list() throws Exception {
        return categoryService.list();
    }*/
    @GetMapping("/categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start,
    		@RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Page4Navigator<Category> page =categoryService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page;
    }
    
    
    //将前端传来的bean上传到数据库
    //再将传来的图片保存到相应的目录上
    @PostMapping("categories")
    public Object add(Category bean,MultipartFile image,HttpServletRequest request) throws IllegalStateException, IOException{
    	categoryService.add(bean);
    	saveOrUpdateImageFile(bean, image, request);
    	return bean;
    }
    
    public void saveOrUpdateImageFile(Category bean,MultipartFile image,HttpServletRequest request) throws IllegalStateException, IOException{
    	File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
    	File file = new File(imageFolder,bean.getId()+".jpg");
    	if(!file.getParentFile().exists()){
    		file.getParentFile().mkdir();
    	}
    	image.transferTo(file);
    	BufferedImage img = ImageUtil.change2jpg(file);
    	ImageIO.write(img,"jpg",file);
    }
    
    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable("id") int id,HttpServletRequest request){
    	categoryService.delete(id);
    	File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
    	File file = new File(imageFolder,id+".jpg");
    	file.delete();
    	return null;
    }
    
    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") int id) throws Exception {
        Category bean=categoryService.get(id);
        return bean;
    }
    
    @PutMapping("/categories/{id}")
    public Object update(Category bean,MultipartFile image,HttpServletRequest request) throws IllegalStateException, IOException{
    	String name = request.getParameter("name");
    	bean.setName(name);
    	categoryService.update(bean);
    	if(image!=null){
    		saveOrUpdateImageFile(bean, image, request);
    	}
    	return bean;
    }
   
    
}
