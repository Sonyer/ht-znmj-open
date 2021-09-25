import store from './store.js';

module.exports = (param) => {
	
				
	var url = param.url;
	var method = param.method;
	var header = param.header || {};
	var data = param.data || {};
	
	const userInfo = store.getUserStore();
	var token = "";
	var accountCode = "";
	
	//用户验证的请求
	if(typeof(userInfo) !== "undefined" && typeof(userInfo.token) !== "undefined" 
		&& typeof(userInfo.accountCode) !== "undefined"){
		token = userInfo.token;
		accountCode = userInfo.accountCode;
	}
	
	// 请求方式: GET POST 
	if(method){
		method = method.toUpperCase(); // 小写转成大写
		if(method == "POST"){
			header = {"content-type":"application/x-www-form-urlencoded"}
		}
	}
	
	header.token = token;
	header.accountCode = accountCode; 
	
	// 发起请求 加载动画
	if(!param.hideLoading){
		uni.showLoading({title:"加载中..."})
	}
	
	/* console.log("加载:"+url) */
	// 发起网络请求
	uni.request({
		url: url,
		method:method || "GET",
		header:header,
		data: data,
		success: res => {
			//获取token
			if(res.header.token){
				userInfo.token = res.header.token;
				store.userStore(userInfo);
			}
			
			//---------------获取通知消息------------------begin
			if(res.header.INNER_NOTIFY_TYPES_KEY != null &&  res.header.INNER_NOTIFY_TYPES_KEY != '' && typeof ( res.header.INNER_NOTIFY_TYPES_KEY) != 'undefined'){
				const notifyStore = store.getNotifyStore();
				
				var innerNotifyTypesSet = JSON.parse(res.header.INNER_NOTIFY_TYPES_KEY);
				for(let innerNotifyType of innerNotifyTypesSet) {
				  if(res.header[innerNotifyType] != null && res.header[innerNotifyType] != '' && typeof (res.header[innerNotifyType]) != 'undefined'){
				  	notifyStore[innerNotifyType] = res.header[innerNotifyType];
				  }
				}
				
				store.setNotifyStore(notifyStore);
			}
			//---------------获取通知消息------------------end
			
			if(res.data.code && res.data.code != 200){ // api错误
				uni.showModal({
					content:res.data.message
				})
				return;
			}
			
			typeof param.success == "function" && param.success(res.data);
		},
		fail: (e) => {
			uni.showModal({
				content: e.message
			})
			typeof param.fail == "function" && param.fail(e.data);
		},
		complete: () => {
			// console.log("网络请求complete");
			uni.hideLoading();
			typeof param.complete == "function" && param.complete(e.data);
			return;
		}
	})
}