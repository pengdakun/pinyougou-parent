//首页controller
app.controller("indexController",function($scope,indexService){
	
	//获得登录用户名
	$scope.loginName=function(){
		indexService.loginName().success(function(response){
			$scope.loginName=response.loginName;
		})
		
	}
	
})