var app=angular.module("pinyougouAPP",[]);

//定义过滤器
app.filter("trustHtml",['$sce',function($sce){
	//传入需要过滤内容
	return function(data){
		return $sce.trustAsHtml(data);//返回过滤后内容，进行信任html转换
	}
}])