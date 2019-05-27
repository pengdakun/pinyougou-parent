 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,goodsService,uploadService,itemCatService,typeTemplateService,$location){	
	
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
		$scope.entity.tbGoodsDesc.introduction=editor.html();
		var serviceObject;//服务层对象  				
		if($scope.entity.tbGoods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					alert(response.msg);
					location.href="goods.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	//保存 
	/*$scope.add=function(){	
		$scope.entity.tbGoodsDesc.introduction=editor.html();
		serviceObject=goodsService.add( $scope.entity  ).success(function(response){
			if(response.success){
				alert("新增成功");
				$scope.entity={};
				editor.html("");//清空富文本编辑器
			}else{
				alert(response.message);
			}
		});				
	}*/
	
	 
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
	
	
	//图片上传
	$scope.upload=function(){
		uploadService.upload().success(function(response){
			if(response.success){
				$scope.image_entity.url=response.msg;
			}else{
				alert(response.msg)
			}
		});
	}
	
	//向表格中添加上传图片
	$scope.entity={tbGoods:{},tbGoodsDesc:{itemImages:[],specificationItems:[]}}
	$scope.addTableImage=function(){
		$scope.entity.tbGoodsDesc.itemImages.push($scope.image_entity);
	}
	//移除表格中添加的图片
	$scope.deleTableTmage=function(index){
		$scope.entity.tbGoodsDesc.itemImages.splice(index,1);
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
	
	//添加规格属性
	$scope.updateSpecAttribute=function($event,text,optionName){
		var obj=$scope.searchObjectByKey($scope.entity.tbGoodsDesc.specificationItems,"attributeName",text);
		if(obj!=null){
			if($event.target.checked){
				obj.attributeValue.push(optionName);
			}else{
				obj.attributeValue.splice(obj.attributeValue.indexOf(optionName),1);
				if(obj.attributeValue.length==0){
					$scope.entity.tbGoodsDesc.specificationItems.splice($scope.entity.tbGoodsDesc.specificationItems.indexOf(obj),1);
				}
			}
		}else{
			$scope.entity.tbGoodsDesc.specificationItems.push({"attributeName":text,"attributeValue":[optionName]});
		}
	}
	
	//创建sku列表
	$scope.createItemList=function(){
		//用于深克隆
		$scope.entity.tbItems=[{spec:{},price:0,num:99999,status:0,isDefault:0}];//列表初始化
		var items=$scope.entity.tbGoodsDesc.specificationItems;
		for(var i=0;i<items.length;i++){
			$scope.entity.tbItems=addColumn($scope.entity.tbItems,items[i].attributeName,items[i].attributeValue);
		}
	}
	
	//深克隆，controller内部调用，不用加$scope
	addColumn=function(list,columnName,columnValues){
		var newList=[];
		for(var i=0;i<list.length;i++){
			var oldRow=list[i]
			for(var j=0;j<columnValues.length;j++){
				var newRow=JSON.parse(JSON.stringify(oldRow));//深克隆
				newRow.spec[columnName]=columnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
	}
	
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
