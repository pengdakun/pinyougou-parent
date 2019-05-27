//品牌controller
app.controller("brandController",function($scope,$controller,brandService){
	//继承baseController，将baseController的$scope与当前的controller的$scope关联
	$controller("baseController",{$scope:$scope});
	//条件分页查询
	$scope.searchEntity={};
	$scope.search=function(page,rows){
		brandService.search(page,rows,$scope.searchEntity).success(function(response){
			$scope.list=response.rows;//获得每页显示数据
			$scope.paginationConf.totalItems=response.total;//获得总条数
		})
	}
	
	//添加
	$scope.save=function(){
		var object=null;
		if($scope.entity.id!=null){
			object=brandService.update($scope.entity);
		}else{
			object=brandService.save($scope.entity);
		}
		object.success(function(response){
			if(response.success){
				$scope.reloadList();//重新加载
			}else{
				alert(response.msg);
			}
		})
	}
	
	//通过id查询-修改
	$scope.update=function(id){
		brandService.findOne(id).success(function(response){
			$scope.entity=response;//获得查询数据
		})
	}
	
	//删除
	$scope.del=function(){
		brandService.del($scope.selectIds).success(function(response){
			if(response.success){
				$scope.reloadList();//重新加载
			}else{
				alert(response.msg);
			}
		})
	}
	
	
});