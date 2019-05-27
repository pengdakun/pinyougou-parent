//index页面Controller
app.controller("indexController",function($scope,loginService){
	//获得当前登录用户名
	$scope.showLoginName=function(){
		loginService.loginName().success(function(response){
			$scope.loginName=response.loginName;
		})
	}
	
})