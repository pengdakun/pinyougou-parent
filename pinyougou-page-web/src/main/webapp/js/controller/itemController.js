 //控制层 
app.controller('itemController' ,function($scope,$http){	
	
	$scope.specItem={};//存储用户选择规格
	
	//数量加减
	$scope.addNum=function(x){
		$scope.num+=x;
		if($scope.num<1){
			$scope.num=1;
		}
	}
	
	//选择规格
	$scope.selectSpec=function(key,value){
		$scope.specItem[key]=value;
		searchSku();
	}
	
	//判断某规格是否被选中
	$scope.isSelect=function(key,value){
		if($scope.specItem[key]==value){
			return true;
		}
		return false;
	}
	
	$scope.sku={};//当前选择的sku
	//加载默认sku
	$scope.loadSku=function(){
		$scope.sku=skuList[0];
		$scope.specItem=JSON.parse(JSON.stringify($scope.sku.spec));
	}
	
	//匹配两个JSON对象是否相等
	matchObject=function(map1,map2){
		//双向匹配
		for(var k in map1){
			if(map1[k]!=map2[k]){
				return false;
			}
		}
		for(var k in map2){
			if(map2[k]!=map1[k]){
				return false;
			}
		}
		return true;
	}
	
	//根据规格查询sku
	searchSku=function(){
		for(var i=0;i<skuList.length;i++){
			if(matchObject($scope.specItem,skuList[i].spec)){
				$scope.sku=skuList[i];
				return;
			}
		}
	}
		
	//添加购物车,{"withCredentials":true}跨域请求时是否携带cookie
	$scope.addToCat=function(){
		$http.get("http://192.168.25.1:9107/cart/addGoodsToCartList.do?itemId="+$scope.sku.id+"&num="+$scope.num,{"withCredentials":true}).success(function(response){
			if(response.success){
				location.href="http://192.168.25.1:9107/cart.html";
			}else{
				alert(response.msg);
			}
		})
	}
	
    
});	
