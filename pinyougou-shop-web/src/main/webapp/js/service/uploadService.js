//上传service
app.service("uploadService",function($http){
	
	this.upload=function(){
		var formdata=new FormData();
		formdata.append("file",file.files[0]);//将页面中第一个名为file的文件域添加到formdata
		return $http({
			url:"../upload.do",
			method:"post",
			data:formdata,
			headers:{"Content-Type":undefined},//设置multipart/form-data
			transformRequest:angular.identity//序列化formdata
		});
		
		
	}
	
	
})