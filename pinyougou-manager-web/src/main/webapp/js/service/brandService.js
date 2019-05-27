//品牌service
app.service("brandService",function($http){
	this.search=function(page,rows,searchEntity){
		return $http.post("../brand/search.do?page="+page+"&rows="+rows,searchEntity);
	}
	this.save=function(entity){
		return $http.post("../brand/add.do",entity);
	}
	this.update=function(entity){
		return $http.post("../brand/update.do",entity);
	}
	this.findOne=function(id){
		return $http.get("../brand/findOne.do?id="+id);
	}
	this.del=function(ids){
		return $http.get("../brand/delete.do?ids="+ids);
	}
	this.selectOoptionList=function(){
		return $http.get("../brand/selectOoptionList.do");
	}
});