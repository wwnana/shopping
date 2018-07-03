package shopping.admin.product.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import cn.itcast.commons.CommonUtils;
import shopping.category.domain.Category;
import shopping.category.service.CategoryService;
import shopping.product.domain.Product;
import shopping.product.service.ProductService;

@WebServlet("/admin/AdminAddProductServlet")
public class AdminAddProductServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
    //用于上传图书的功能实现，上传必须用post
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//commons-fileupload的上传三步
		//1.创建工具
		FileItemFactory factory = new DiskFileItemFactory();
		//2.创建解析器对象
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(80*1024);//设置单个上传的文件上限为80KB
		//3.解析request得到List<FileItem>
		List<FileItem> fileItemList = null;
		try {
			fileItemList = sfu.parseRequest(request);
			System.out.println("fileItemList="+fileItemList);
		} catch (FileUploadException e) {
			// 如果出现异步，说明单个文件超出了80KB
			error("上传的文件超出了80KB",request,response);
			return;
		}
		//4.把List<FileItem>封装到Product对象中
		//4.1首先把“普通表单字段”放到一个Map中，再把Map转换成Product和Category对象
		Map<String,Object> map = new HashMap<String,Object>();
		for(FileItem fileItem : fileItemList){
			if(fileItem.isFormField()){//如果是普通表单字段
				map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}
		}
		Product product = CommonUtils.toBean(map, Product.class);
		Category category = CommonUtils.toBean(map, Category.class);
		product.setCategory(category);
		//4.2把上传的图片保存起来
		/*
		 * 获取小图
		 */
		//获取文件名，并截取之，因为部分浏览器需要绝对路径
		FileItem fileItem = fileItemList.get(2);//获取小图
		//System.out.println("fileItem="+fileItem);
		String filename = fileItem.getName();
		//System.out.println("filename="+filename);
		int index = filename.lastIndexOf("\\");
		if(index != -1){
			filename = filename.substring(index + 1);
		}
		//System.out.println("filename1="+filename);
		//给文件名加前缀，避免文件同名现象
		filename = CommonUtils.uuid()+"_"+filename;
		//System.out.println("filename2="+filename);
		//校验文件名称的扩展名
		if(!filename.toLowerCase().endsWith(".jpg")){
			error("上传的图片扩展名必须是JPG",request,response);
			return;
		}
		//校验图片尺寸
		//保存上传的图片：获取真实路径
		String savepath = this.getServletContext().getRealPath("/product_img");
		//System.out.println("savepath="+savepath);
		//创建目标文件
		File destFile = new File(savepath,filename);//父路径+子路径
		//System.out.println("destFile="+destFile);
		//保存文件
		try {
			fileItem.write(destFile);//它会把临时文件重定向到制动的路径，再删除临时文件
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//使用文件路径创建ImageIcon
		ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
		//通过ImageIcon得到Image对象
		Image image = icon.getImage();
		//获取宽高来进行校验
		if(image.getWidth(null)>350 || image.getHeight(null)>350){
			error("您上传的图片尺寸超出了350*350！",request,response);
			destFile.delete();//删除图片
			return;
		}
		
		//把图片的路径设置给product对象
		product.setImage_b("product_img/"+filename);
		
		/*
		 * 获取大图
		 */
		//获取文件名，并截取之，因为部分浏览器需要绝对路径
		fileItem = fileItemList.get(1);//获取大图
		filename = fileItem.getName();
		index = filename.lastIndexOf("\\");
		if(index != -1){
			filename = filename.substring(index + 1);
		}
		//给文件名加前缀，避免文件同名现象
		filename = CommonUtils.uuid()+"_"+filename;
		//校验文件名称的扩展名
		if(!filename.toLowerCase().endsWith(".jpg")){
			error("上传的图片扩展名必须是JPG",request,response);
			return;
		}
		//校验图片尺寸
		//保存上传的图片：获取真实路径
		savepath = this.getServletContext().getRealPath("/product_img");
		//创建目标文件
		destFile = new File(savepath,filename);//父路径+子路径
		//保存文件
		try {
			fileItem.write(destFile);//它会把临时文件重定向到制动的路径，再删除临时文件
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//使用文件路径创建ImageIcon
		icon = new ImageIcon(destFile.getAbsolutePath());
		//通过ImageIcon得到Image对象
		image = icon.getImage();
		//获取宽高来进行校验
		if(image.getWidth(null)>350 || image.getHeight(null)>350){
			error("您上传的图片尺寸超出了350*350！",request,response);
			destFile.delete();//删除图片
			return;
		}
		
		//把图片的路径设置给product对象
		 product.setImage_w("product_img/"+filename);
		
		
		//通过service完成保存
		product.setPid(CommonUtils.uuid());
		ProductService productService = new ProductService();
		productService.add(product);
		//保存成功信息转发到msg.jsp
		request.setAttribute("msg", "添加产品成功！");
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
		
	}
	
	private void error(String msg,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setAttribute("msg", msg);
		request.setAttribute("parents", new CategoryService().findParents());
		request.getRequestDispatcher("/adminjsps/admin/product/add.jsp").forward(request, response);
	}
}
