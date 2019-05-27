//广告service
app.service("contentService",function($http){
	
	//通过广告类目id查询广告
	this.findByCategoryId=function(id){
		return $http.get("content/findByCategoryId.do?id="+id);
	}
	
})