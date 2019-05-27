//登录服务
app.service("loginService",function($http){
	//获得登录名称
	this.loginName=function(){
		 return $http.get("../login/name.do");
	}
	
})