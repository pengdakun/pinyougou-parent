//购物车服务层
app.service('cartService',function($http){
	//购物车列表
	this.findCartList=function(){
		return $http.get('/cart/findCartList.do');		
	}
	
	//添加商品到购物车
	this.addGoodsToCartList=function(itemId,num){
		return $http.get('/cart/addGoodsToCartList.do?itemId='+itemId+'&num='+num);
	}
	
	//求合计计数
	this.sum=function(cartList){
		var totalValue={totalNum:0,totalFee:0};
		for(var i=0;i<cartList.length;i++){
			var cart=cartList[i];
			for(var j=0;j<cart.orderItemList.length;j++){
				totalValue.totalNum+=cart.orderItemList[j].num;
				totalValue.totalFee+=cart.orderItemList[j].totalFee;
			}
		}
		return totalValue;
	}
	
	//获得用户收货地址
	this.findAddress=function(){
		return $http.get("/address/findListByUserId.do");
	}
	
	//提交订单
	this.submitOrder=function(order){
		return $http.post("/order/add.do",order);
	}
	
});