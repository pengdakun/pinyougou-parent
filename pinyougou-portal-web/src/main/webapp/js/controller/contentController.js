//广告controller
app.controller("contentController",function($scope,contentService){
	
	//通过广告类目查询广告
	$scope.contentList=[];
	$scope.findByCategoryId=function(id){
		contentService.findByCategoryId(id).success(function(response){
			$scope.contentList[id]=response;
		})
	}
	
	//搜索
	$scope.search=function(){
		location.href="http://localhost:9104/search.html#?keywords="+$scope.keywords;
	}
	
})