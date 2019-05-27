 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
	
	//注册
	$scope.register=function(){
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入!");
			return;
		}
		userService.add($scope.entity,$scope.smsCode).success(function(response){
			alert(response.msg);
		})
	}
	
	//发送短信验证码
	$scope.sendSmsCode=function(){
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请输入手机号码!");
			return;
		}
		userService.sendSmsCode($scope.entity.phone).success(function(response){
			alert(response.msg);
		})
	}
	
});	
