//搜索controller
app.controller("seachConreoller",function($scope,searchService,$location){
	
	//定义搜索对象结构
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''}
	
	//搜索
	$scope.search=function(){
		$scope.searchMap.pageNo=parseInt($scope.searchMap.pageNo);//转换为数字
		searchService.search($scope.searchMap).success(function(response){
			$scope.resultMap=response;
			buildPageLable();
		})
	}
	
	//添加搜索项
	$scope.addSearchItem=function(key,value){
		if(key=='category' || key=='brand' || key=='price'){//用户点击分类或品牌
			$scope.searchMap[key]=value;
		}else{
			$scope.searchMap.spec[key]=value;
		}
		$scope.search();
	}
	
	//撤销搜索变量
	$scope.removeSearchItem=function(key){
		if(key=='category' || key=='brand' || key=='price'){//用户点击分类或品牌
			$scope.searchMap[key]='';
		}else{
			delete $scope.searchMap.spec[key];
		}
		$scope.search();
	}
	
	//分页函数
	buildPageLable=function(){
		//构建分页栏
		$scope.pageLable=[];
		var firstPage=1;//显示的开始页码
		var lastPage=$scope.resultMap.totalPages;//显示的结束页码
		$scope.firstDot=true;//是否开始有点
		$scope.lastDot=true;//是否结束有点
		if (lastPage>5) {//如果页码数量大于5
			if ($scope.searchMap.pageNo<=3) {//分页条显示前五页
				lastPage=5;
				$scope.firstDot=false;//表示前面没有点
			}else if($scope.searchMap.pageNo>=lastPage-2){//分页条显示后五页
				firstPage=lastPage-4;
				$scope.lastDot=false;//表示后边没有点
			}else{//显示以当前页为中心的页码
				firstPage=$scope.searchMap.pageNo-2;
				lastPage=$scope.searchMap.pageNo+2;
			}
		}else{
			//前后无点
			$scope.firstDot=false;
			$scope.lastDot=false;
		}
		for(i=firstPage;i<=lastPage;i++){
			$scope.pageLable.push(i);
		}
	}
	
	//分页查询
	$scope.queryByPage=function(pageNo){
		if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
			return;
		}
		$scope.searchMap.pageNo=pageNo;
		$scope.search();
	}
	
	//判断当前页是否为第一页
	$scope.isTopPage=function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}
		return false;
	}
	
	//判断当前页是否为最后一页
	$scope.isEndPage=function(){
		if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
			return true;
		}
		return false;
	}
	
	//排序查询
	$scope.querySortSearch=function(sort,sortField){
		$scope.searchMap.sort=sort;
		$scope.searchMap.sortField=sortField;
		$scope.search();
	}
	
	//当搜索关键字为品牌时，屏蔽品牌
	$scope.keywordsIsBrand=function(){
		for(var i=0;i<$scope.resultMap.brandList.length;i++){
			if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
				return true;
			}
		}
		return false;
	}
	
	//接收首页参数进行查询
	$scope.indexSearch=function(){
		$scope.searchMap.keywords=$location.search()['keywords'];
		$scope.search();
	}
	
	
	
})