<template>
	<view>
		<view class="content">
			<view class="logo-area">
				<view class="logo">
					<image src="../../static/logo.png"></image>
				</view>
			</view>
			
			<view class="auth-buttons">
				<!-- #ifdef MP-WEIXIN -->
				<view>
					<button v-if="showGetWxUser" type="primary" open-type="getUserInfo" @getuserinfo="getWxUserInfo">微信授权登陆</button>
				</view>
				<!-- <view>
					<button v-if="showGetWxPhoneNumber" open-type="getPhoneNumber" @getphonenumber="getWxPhoneNumber">授权手机号码</button>
				</view> -->
				<!-- #endif -->
			</view>
		</view>
	</view>
</template>

<script>

    export default {
        data() {
            return {
				showGetWxUser:true,
				//showGetWxPhoneNumber:false,
				wxAuthRequest:{
					wxCode: '',
					iv: '',
					encryptedData: '',
					longitude: '',
					latitude: '',
					userInfo: '',
				},
				wxGetPhoneNumberRequest:{
					wxCode: '',
					iv: '',
					encryptedData: ''
				},
				refuseUrl: '../tabBar/home/home',
				toUrl: '../tabBar/home/home'
			};
        },
        methods: {
			getWxUserInfo(e) {
				//确认是否已授权
				this.locationPermission(e);
			},
			getWxPhoneNumber(e) { 
				let _this = this;
				uni.showLoading({
					title: '加载中...' 
				});
				
				// 1.wx获取登录用户code
				uni.login({
					provider: 'weixin',
					success: function(loginRes) {
						_this.wxGetPhoneNumberRequest.wxCode = loginRes.code;
						console.log("微信获取Code:"+loginRes.code);		
							
					_this.wxGetPhoneNumberRequest.encryptedData = e.detail.encryptedData;
					_this.wxGetPhoneNumberRequest.iv = e.detail.iv;
					
						_this.https({
							url: _this.interfaces.wechatGetPhoneNumber,
							data: _this.wxGetPhoneNumberRequest,  
							method: 'POST',
							header: {
								'content-type': 'application/json'
							},
							success: ((res) => { 
								//全局存储用户信息
								_this.store.userStore(res.data);
							
								uni.hideLoading();
								
								if(res.data.isAuthPhoneNumber == '1'){
									//不需要授权直接跳转首页
									uni.reLaunch({
										url: _this.toUrl
									});
								}
							})
						});
					},
				});
			},
			//执行登陆操作
			getWxUserInfoAction(e){
				let _this = this;
				uni.showLoading({
					title: '加载中...' 
				});
				_this.wxAuthRequest.encryptedData = e.detail.encryptedData;
				_this.wxAuthRequest.iv = e.detail.iv;
				_this.wxAuthRequest.userInfo = e.detail.rawData;
				console.log("encryptedData:"+e.detail.encryptedData)
				console.log("iv:"+e.detail.iv)
				console.log("userInfo:"+e.detail.rawData)
				
				// 1.wx获取登录用户code
				uni.login({
					provider: 'weixin',
					success: function(loginRes) {
						_this.wxAuthRequest.wxCode = loginRes.code;
						console.log("微信获取Code:"+loginRes.code);		
						
						uni.getLocation({
							type: 'gcj02',
							success: function (res) {
								console.log('当前位置的经度：' + res.longitude);
								console.log('当前位置的纬度：' + res.latitude);
								_this.wxAuthRequest.longitude = res.longitude;
								_this.wxAuthRequest.latitude = res.latitude;
								
								console.log("存储用户:"+_this.wxAuthRequest);
								
								//请求登陆
								_this.requestLogin();
							},
							fail() {
								// 已授权 ..(获取位置信息)
								uni.showModal({
									title: '温馨提示:是否已开启GPS',
									content: '您的GPS定位未打开,将会影响您的体验!',
									confirmText: '已开启',
									success(res){
										if (res.confirm) {
											//获取位置
											_this.locationPermission(e)
										}else{
											_this.wxAuthRequest.longitude = "0";
											_this.wxAuthRequest.latitude = "0";
											//请求登陆
											_this.requestLogin();
										}
									}
								})
							},
							complete: function(){
								uni.hideLoading();
							}
						});
					},
					fail() {
						_this.locationPermission(even);
						uni.hideLoading();
					}
				});
			},
			requestLogin(){
				//获取用户信息
				this.https({
					url: this.interfaces.wechatLogin,  //POST json传输必须未转字符串
					data: this.wxAuthRequest,
					method: 'POST',
					header: {
						'content-type': 'application/json'
					},
					success: ((res) => { 
						console.log('保存后获取:'+res.data);
						//全局存储用户信息
						this.store.userStore(res.data);
						
						uni.hideLoading();
						
						uni.reLaunch({
							url: this.toUrl
						});
						
						/* if(res.data.isAuthPhoneNumber == '1'){
							//不需要授权直接跳转首页
							uni.reLaunch({
								url: this.toUrl
							});
						}else{
							this.showGetWxUser = false;
							this.showGetWxPhoneNumber = true;
						} */
					})
				}); 
			},
			 //初始化加载页面时处理授权-------------
			 locationPermission(even){
			 	const _this = this;
			 	uni.getSetting({
			 		success(res) {   
			 			if (!res.authSetting['scope.userLocation']) {
			 				// 未授权
			 				uni.authorize({
			 					scope: 'scope.userLocation',
			 					success() { //1.1 允许授权
			 						//uni.getLocation()
			 						console.log("已授权")
			 						_this.getWxUserInfoAction(even);
			 					},
			 					fail(e){    //1.2 拒绝授权
			 						console.log(e);
			 						console.log("你拒绝了授权，无法获得周边信息")
			 						
			 						// 定位权限未开启，引导设置
			 						uni.showModal({
			 							title: '温馨提示',
			 							content: '您已拒绝定位,请开启',
			 							confirmText: '去设置',
			 							success(res){
			 								if (res.confirm) {
			 									//打开授权设置
			 									uni.openSetting()
			 								}else{
												_this.wxAuthRequest.longitude = "0";
												_this.wxAuthRequest.latitude = "0";
												//请求登陆
												_this.requestLogin();
											}
			 							}
			 						})
			 						
			 						uni.hideLoading();
			 					}
			 				})
			 			}else{
			 				// 已授权 ..(获取位置信息)
			 				_this.getWxUserInfoAction(even);
			 			}
			 		}
			 	});
			 },
        },
		onLoad(options) {//默认加载
		console.log(options)
			if(options.refuseUrl != null && options.refuseUrl != '' && typeof(options.refuseUrl) != 'undefined'){
				this.refuseUrl = unescape(options.refuseUrl);
			}
			if(options.toUrl != null && options.toUrl != '' && typeof(options.toUrl) != 'undefined'){
				this.toUrl = unescape(options.toUrl);
			}
			
			//#ifdef  MP-WEIXIN
			/* if(typeof(userInfo) == "undefined"  || typeof(userInfo.isAuth) =="undefined" || userInfo.isAuth == '0'){  //用户未登陆未授权
				this.showGetWxUser = true;
				this.showGetWxPhoneNumber = false;
			}else if(typeof(userInfo.isAuthPhoneNumber) =="undefined" || userInfo.isAuthPhoneNumber== '0'){
				this.showGetWxUser = false;
				this.showGetWxPhoneNumber = true;
			} */
			//#endif
		},
		mounted(options) {
			if(options.refuseUrl != null && options.refuseUrl != '' && typeof(options.refuseUrl) != 'undefined'){
				this.refuseUrl = options.refuseUrl;
			}
			if(options.toUrl != null && options.toUrl != '' && typeof(options.toUrl) != 'undefined'){
				this.toUrl = options.toUrl;
			}
		}
    }
</script>

<style lang="scss" scoped>
	.content{
		width: 100%;
		height: 100%;
		position: fixed;
		
		.logo-area{
			align-items: center;
			justify-content: center;
			display: flex;
			height: 50%;
			position: relative;
			
			.logo{
				image{
					width: 150upx;
					height: 150upx;
				}
			}
		}
		
		.auth-buttons{
			button{
				margin: 20upx;
			}
		}
	}
</style>
