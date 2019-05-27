//控制层 
app.controller('seckillGoodsController' ,function($scope,seckillGoodsService,$location,$interval){	
	 //读取列表数据绑定到表单中  
	$scope.findList=function(){
		seckillGoodsService.findList().success(
			function(response){
				$scope.list=response;
			}			
		);
	} 
	
	//查询详情
	$scope.findOneFromRedis=function(){
		seckillGoodsService.finsOneFromRedis($location.search()["id"]).success(function(response){
			$scope.entity=response;
			
			//倒计时开始
			//获得结束时间到当前日期的秒速
			allSecond=Math.floor((new Date($scope.entity.endTime).getTime()-new Date().getTime())/1000);
			time=$interval(function(){
				allSecond=allSecond-1;
				if(allSecond<=0){
					$interval.cancel(time);//取消定时
				}
				$scope.timeStr=converTimeString(allSecond);
			},1000);
			
		})
	}
	
	//装换秒为天：小时：分钟：秒
	converTimeString=function(allSecond){
		var days=Math.floor(allSecond/(60*60*24));//天数
		var hours=Math.floor((allSecond-days*60*60*24)/(60*60));//小时数
		var minutes=Math.floor((allSecond-days*60*60*24-hours*60*60)/60);//分钟数
		var second=allSecond-days*60*60*24-hours*60*60-minutes*60;//秒数
		var timeString="";
		if(days>0){
			timeString+=days+"天:";
		}
		return timeString+=hours+"小时:"+minutes+"分钟:"+second+"秒";
	}
	
	//提交订单
	$scope.submitOrder=function(){
		seckillGoodsService.submitOrder($scope.entity.id).success(function(response){
			if(response.success){
				alert("抢购成功，请在五分钟内支付完成！");
				location.href="pay.html"
			}else{
				alert(response.msg);
			}
		})
	}
	
});	
