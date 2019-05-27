app.controller('payController' ,function($scope ,payService,$location){
	
	//创建二维码
	$scope.createNative=function(){
		payService.createNative().success(function(response){
			//显示点定单号及金额
			$scope.totalFee=(response.totalFee/100).toFixed(2);
			$scope.outTradeNo=response.outTradeNo;
			//生成二维码
			var qr = new QRious({
	 		   element:document.getElementById('qrious'),
	 		   size:250,
	 		   level:'H',
	 		   value:response.code_url
	 		});
			
			queryPayStatus();//查询订单状态
		})
	}
	
	//订单状态查询
	queryPayStatus=function(){
		payService.queryPayStatus($scope.outTradeNo).success(function(response){
			if(response.success){
				location.href="paysuccess.html#?money="+$scope.totalFee;
			}else{
				if(response.msg="二维码超时"){
					$scope.createNative();
				}else{
					location.href="payfail.html";
				}
			}
		})
	}
	
	//获得支付金额
	$scope.getMoney=function(){
		return $location.search()["money"];
	}
	
})