 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					$scope.finByParentId($scope.entity.parentId,"");
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.finByParentId($scope.entity.parentId,"");
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	$scope.entity={"parentId":0};
	//动态添加导航
	$scope.mbxDaohang=[{"id":0,"name":"顶级分类列表"}];
	$scope.finByParentId=function(parentId,name){
		$scope.entity={"parentId":parentId};
		if(parentId!="0" && name!=""){
			$scope.mbxDaohang.push({"id":parentId,"name":name});
		}
		itemCatService.findByParentId(parentId).success(function(response){
			$scope.list=response;
		})
	}
	//删除导航
	$scope.deleMbx=function(index){
		for(var i=index+1;i<$scope.mbxDaohang.length;i++){
			$scope.mbxDaohang.splice(index+1,1);
		}
		$scope.mbxDaohang.splice(index+1,1);
	}
	
	//查询模板下拉列表
	$scope.selectTempleOptionList=function(){
		typeTemplateService.selectOptionList().success(function(response){
			$scope.templeOption={data:response};
		})
	}
    
});	
