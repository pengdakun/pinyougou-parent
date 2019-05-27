//购物车控制层 
app.controller('cartController',function($scope,cartService){
	//查询购物车列表
	$scope.findCartList=function(){
		cartService.findCartList().success(
			function(response){
				$scope.cartList=response;
				$scope.totalValue=cartService.sum($scope.cartList);
			}
		);		
	}
	
	
	//添加商品到购物车
	$scope.addGoodsToCartList=function(itemId,num){
		cartService.addGoodsToCartList(itemId,num).success(
			function(response){
				if(response.success){
					$scope.findCartList();//刷新列表
					$scope.totalValue=cartService.sum($scope.cartList);
				}else{
					alert(response.msg);//弹出错误提示
				}				
			}
		);
	}
	
	//获得当前用户收货地址
	$scope.findAddress=function(){
		cartService.findAddress().success(function(response){
			$scope.addressList=response;
			for(var i=0;i<$scope.addressList.length;i++){
				if($scope.addressList[i].isDefault=="1"){
					$scope.address=$scope.addressList[i];
					break;
				}
				
			}
			
		})
	}
	
	//选中某个地址
	$scope.selectAddress=function(address){
		$scope.address=address;
	}
	//判断某地址对象是不是当前选择的对象
	$scope.isSelectedAddess=function(address){
		if(address==$scope.address){
			return true;
		}
		return false;
	}
	
	//订单对象
	$scope.order={"paymentType":"1"};
	
	//选择支付类型
	$scope.selectPayType=function(type){
		$scope.order.paymentType=type;
	}
	
	//提交订单
	$scope.submitOrder=function(){
		//赋值收货信息
		$scope.order.receiverAreaName=$scope.address.address;
		$scope.order.receiverMobile=$scope.address.mobile;
		$scope.order.receiver=$scope.address.contact;
		cartService.submitOrder($scope.order).success(function(response){
			if(response.success){
				if($scope.order.paymentType=='1'){//微信支付跳转到支付页面
					location.href="/pay.html";
				}else{//货到付款跳转到提示页面
					location.href="paysuccess.html";
				}
			}else{
				alert(response.msg);//弹出错误提示
			}	
		})
	}
	
	
});