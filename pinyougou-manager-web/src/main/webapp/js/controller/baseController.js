//基础controller
app.controller("baseController",function($scope){
	//分页控件配置 
	$scope.paginationConf = {
			currentPage: 1,//当前页数
			totalItems: 10,//总条数
			itemsPerPage: 10,//每页显示条数
			perPageOptions: [10, 20, 30, 40, 50],
			onChange: function(){//页码改变时调用
				$scope.reloadList();//重新加载
			}
	};
	
	//重新加载
	$scope.reloadList=function(){
		$scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
	}
	
	//复选按钮事件
	$scope.selectIds=[];
	$scope.updateSelection=function(id,$event){
		if($event.target.checked){
			$scope.selectIds.push(id);
		}else{
			var index=$scope.selectIds.indexOf(id);
			$scope.selectIds.splice(index,1);
		}
	}
	
	
	//从json对象获得某个key的value
	$scope.jsonToString=function(jsonString,key){
		var json=JSON.parse(jsonString);
		var value="";
		for(var i=0;i<json.length;i++){
			if(i>0){
				value+=",";
			}
			value+=json[i][key];
		}
		return value;
	}
	
	//从集合中按照key查询对象
	$scope.searchObjectByKey=function(list,key,keyValue){
		for(var i=0;i<list.length;i++){
			if(list[i][key]==keyValue){
				return list[i];
			}
		}
		return null;
	}
	
	
})