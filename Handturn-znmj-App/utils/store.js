var userStore = function(user) {
	try{
        uni.setStorageSync('userInfo', JSON.stringify(user));
    }catch(e){
	//TODO handle the exception
	};
};

var getUserStore = function() {
	const userInfo = uni.getStorageSync('userInfo'); 

	if(typeof(userInfo) !== "undefined" && userInfo != '' && userInfo != null){
		return JSON.parse(userInfo);
	}else{
		userInfo;
	}
};

var setSearchStore = function(search) {
	try{
		const searchTemp = {'searchValue':''};
		if(typeof(search.searchValue) !="undefined"){
			searchTemp.searchValue = search.searchValue;
		}
	    uni.setStorageSync('searchInfo', JSON.stringify(searchTemp));
	}catch(e){
	//TODO handle the exception
	};
};

var getSearchStore = function() {
	const searchInfo = uni.getStorageSync('searchInfo');
	if(searchInfo){
		return JSON.parse(searchInfo);
	}else{
		const searchTemp = {'searchValue':''};
		return searchTemp;
	}
};

//消息通知全局变量
notifyData : {};

var setNotifyStore = function(notify) {
	try{

		let showTabBar = false;
		let showCartBar = false;
		if((notify.VISITOR_APPLY_NOTIFY != null && typeof(notify.VISITOR_APPLY_NOTIFY) !="undefined" && notify.VISITOR_APPLY_NOTIFY !="")
			|| (notify.VISITOR_AUDIT_NOTIFY != null && typeof(notify.VISITOR_AUDIT_NOTIFY) !="undefined" && notify.VISITOR_AUDIT_NOTIFY !="")
			){
			showTabBar = true;
		}
		
		//前端自定义通知 只让前端处理	
		/* if(notify.BUYER_BUY_ORDER_ADD_CART_NOTIFY == null || typeof(notify.BUYER_BUY_ORDER_ADD_CART_NOTIFY) =="undefined"){
			notify.BUYER_BUY_ORDER_ADD_CART_NOTIFY = '';
		}else{
			if(notify.BUYER_BUY_ORDER_ADD_CART_NOTIFY == '1'){
				showCartBar = true;
			}
		} */
		
		uni.setStorageSync('notifyStore', JSON.stringify(notify));
	
		this.notifyData = notify;
	
		//显示提醒图标
		if(showTabBar){
			uni.showTabBarRedDot({
				index: 1,
				fail(res) {
					console.log(res)
				}
			});
		}else{
			uni.hideTabBarRedDot({
				index: 1,
				success() {
					
				},
				fail(res) {
				
				}
			});
		}
		
		/* if(showCartBar){
			uni.showTabBarRedDot({
				index: 2,
				fail(res) {
					console.log(res)
				}
			});
		}else{
			uni.hideTabBarRedDot({
				index: 2,
				success() {
					
				},
				fail(res) {
				
				}
			});
		} */
		
		//强制刷新
		this.$forceUpdate();
	}catch(e){
		//TODO handle the exception
	};
};

var getNotifyStore = function() {
	const notifyStore = uni.getStorageSync('notifyStore');
	if(typeof(notifyStore) !== "undefined" && notifyStore != '' && notifyStore != null){
		return JSON.parse(notifyStore);
	}else{
		const notifyStoreTemp = {};
		return notifyStoreTemp;
	}
};
 
exports.userStore = userStore;
exports.getUserStore = getUserStore;
exports.setSearchStore = setSearchStore;
exports.getSearchStore = getSearchStore;
exports.setNotifyStore = setNotifyStore;
exports.getNotifyStore = getNotifyStore;