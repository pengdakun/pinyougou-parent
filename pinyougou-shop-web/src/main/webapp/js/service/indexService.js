//首页service
app.service("indexService",function($http){
	
	//获得用户名
	this.loginName=function(){
		return $http.get("../login/name.do");
	}
	
})