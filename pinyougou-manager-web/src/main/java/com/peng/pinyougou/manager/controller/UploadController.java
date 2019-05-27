package com.peng.pinyougou.manager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.peng.pinyougou.util.FastDFSClient;

import entity.Result;

/**   
* 项目名称：pinyougou-shop-web   
* 类名称：UploadController   
* 类描述：  上传controller 
* 创建人：彭坤   
* 创建时间：2018年8月29日 下午8:50:38      
* @version     
*/
@RestController
public class UploadController {
	
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;
	
	@RequestMapping(value="/upload")
	public Result upload(MultipartFile file){
		try {
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
			String originalFilename = file.getOriginalFilename();
			String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			String uploadFile = fastDFSClient.uploadFile(file.getBytes(), extName);
			uploadFile=FILE_SERVER_URL+uploadFile;
			return new  Result(true, uploadFile);
		} catch (Exception e) {
			e.printStackTrace();
			return new  Result(false, "上传失败");
		}
		
	}
	

}
