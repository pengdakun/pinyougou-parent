 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,typeTemplateService,goodsService,itemCatService,$location){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		//获得页面传递的参数
		var id=$location.search()['id'];
		if(id==null){
			return;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				editor.html($scope.entity.tbGoodsDesc.introduction);
				//将String转换为JSON对象
				$scope.entity.tbGoodsDesc.itemImages=JSON.parse($scope.entity.tbGoodsDesc.itemImages);
				$scope.entity.tbGoodsDesc.customAttributeItems=JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
				$scope.entity.tbGoodsDesc.specificationItems=JSON.parse($scope.entity.tbGoodsDesc.specificationItems);
				for(var i=0;i<$scope.entity.tbItems.length;i++){
					$scope.entity.tbItems[i].spec=JSON.parse($scope.entity.tbItems[i].spec);
				}
			}
		);			
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//获得一级分类列表
	$scope.selectItemCat1List=function(){
		itemCatService.findByParentId(0).success(function(response){
			$scope.itemCat1List=response;
		})
	}
	
	//监听一级分类值得变化，得到二级分类
	$scope.$watch("entity.tbGoods.category1Id",function(newValue,oldValue){
		itemCatService.findByParentId(newValue).success(function(response){
			$scope.itemCat2List=response;
		})
	})
	//监听二级分类值得变化，得到三级分类
	$scope.$watch("entity.tbGoods.category2Id",function(newValue,oldValue){
		itemCatService.findByParentId(newValue).success(function(response){
			$scope.itemCat3List=response;
		})
		
	})
	//通过三级分类得到模板id
	$scope.$watch("entity.tbGoods.category3Id",function(newValue,oldValue){
		itemCatService.findOne(newValue).success(function(response){
			$scope.entity.tbGoods.typeTemplateId=response.typeId;
		})
	})
	//监听模板id
	$scope.$watch("entity.tbGoods.typeTemplateId",function(newValue,oldValue){
		typeTemplateService.findOne(newValue).success(function(response){
			$scope.typeTemplate=response;
			//获得品牌
			$scope.typeTemplate.brandIds=JSON.parse($scope.typeTemplate.brandIds);
			//获得扩展属性
			if($location.search()['id']==null){//增加商品
				$scope.entity.tbGoodsDesc.customAttributeItems=JSON.parse($scope.typeTemplate.customAttributeItems);
			}
		})
		typeTemplateService.findSpecList(newValue).success(function(response){
			$scope.specList=response;
		})
	})
	
	
	//状态数组
	$scope.status=['未审核','已审核','审核未通过','已关闭'];
	
	//查询商品分类列表
	$scope.itemCatList=[];//商品分类列表
	$scope.findItemCatList=function(){
		itemCatService.findAll().success(function(response){
			for(var i=0;i<response.length;i++){
				$scope.itemCatList[response[i].id]=response[i].name;
			}
		})
	}
	
	//审核
	$scope.updateStatus=function(status){
		goodsService.updateStatus($scope.selectIds,status).success(function(response){
			if(response.success){
				$scope.reloadList();//刷新列表
				$scope.selectIds=[];
			}else{
				alert(response.msg);
			}
		})
	}
	
	//判断某规格及规格选项是否被选中
	$scope.checkAttributeValue=function(text,optionName){
		var items=$scope.entity.tbGoodsDesc.specificationItems;
		var obj=$scope.searchObjectByKey(items,"attributeName",text);
		if(obj!=null){
			if(obj.attributeValue.indexOf(optionName)>=0){
				return true;
			};
		}
		return false;
	}
    
});	
