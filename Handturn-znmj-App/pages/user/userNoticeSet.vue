<template>
	<view>
		<view class="notice-list">
			<view class="notice-box" v-for="(notice,index) in noticeSetData.pageDatas" :key="index" v-bind:id="index" @tap="noticeSetAuth">
				<view class="notice-box-left">
					<view class="icon iconfont">&#xe630;</view>
				</view>
				<view class="notice-box-right">
					<view class="notice-box-right-row1">
						<view class="notice-box-tittle">
							{{notice.notifyType}}
						</view>
						<view class="notice-box-time">
							{{notice.ownerType}}
						</view>
					</view>
					<view class="notice-box-right-row2">
						<view class="notice-box-content">
							{{notice.notifyDec}}
						</view>
						<view class="notice-box-option">
							<button v-if="notice.status == true">已开启</button>
							<button v-else style="background-color: #C0C0C0;">未开启</button>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="loading-text">{{loadingText}}</view>
	</view>
</template>

<script>
	
	export default{
		data() {
			return {
				noticeSetData:{
					pageDatas:[]
				},
				loadingText: "正在加载....",
			}
		},
		methods: {
			initData() {
				this.noticeSetPageQuery();
			},
			noticeSetPageQuery(){
				this.https({
					url: this.interfaces.initMeNoticeSet,  //POST json传输必须未转字符串
					method: 'POST',
					data: {},
					header: {
						'content-type': 'application/json'
					},
					success: ((res) => { 
						this.getWxNotifyStatus(res.data);	
					})
				}); 
			},
			noticeSetAuth(e){
				let _this = this;
				
				var index = e.currentTarget.id;
				
				const noticeSetData = _this.noticeSetData.pageDatas[index];
				if(noticeSetData.wxNotifyTemplateId == null || noticeSetData.wxNotifyTemplateId == '' || typeof (noticeSetData.wxNotifyTemplateId) == 'undefined'){
					uni.showModal({
					    content: '微信消息订阅无需授权！',
					    showCancel: false
					});
					return;
				}
				
				uni.showLoading({title:"加载中..."})
				uni.requestSubscribeMessage({
				   tmplIds: [noticeSetData.wxNotifyTemplateId],
				   success (res) {
					   uni.hideLoading();
					   console.log(res)
					   
					   var can_send = 0;
				
						if (res[noticeSetData.wxNotifyTemplateId] == "accept"){
							can_send = 1;
						}  

					   if(can_send == 1){
						  uni.showModal({
						      content: '微信消息订阅已授权！',
						      showCancel: false
						  }); 
						  
						  _this.getWxNotifyStatus(_this.noticeSetData.pageDatas);
					   }else{
						   _this.authSystemSet();
					   }
				   },
				   fail:(res) => {
					  uni.hideLoading();
					  console.log(res)
					  _this.authSystemSet();
				   }
				});
			},
			authSystemSet(){			
				var that = this;
				//这里只做消息权限是否开启控制
				uni.getSetting({
					withSubscriptions: true, //是否同时获取用户订阅消息的订阅状态，默认不获取
					success: (res)=> {	
						// 定位权限未开启，引导设置
						uni.showModal({
							title: '温馨提示',
							content: '您已拒绝了微信消息授权,请开启',
							confirmText: '去设置',
							success(res){
								if (res.confirm) {
									uni.openSetting({ // 打开设置页
									   success(res) {
											console.log(res)
											that.getWxNotifyStatus(that.noticeSetData.pageDatas);
									   },
									   fail(res) {
											console.log(res)
									   }
									});
								}
							}
						});
					},
					fail: (res) => {
						 console.log(res.authSetting)
					}
				})
			},
			getWxNotifyStatus(noticeSetData){
				//这里只做消息权限是否开启控制
				uni.getSetting({
					withSubscriptions: true, //是否同时获取用户订阅消息的订阅状态，默认不获取
					success: (res)=> {	
						if (res.subscriptionsSetting) {
					
							var itemSettings = res.subscriptionsSetting.itemSettings;
							
							if(res.subscriptionsSetting.mainSwitch == true){
								for (var i = 0; i < noticeSetData.length; i++) { 
									
									if(typeof(itemSettings) =='undefined' 
									 || typeof(itemSettings[noticeSetData[i].wxNotifyTemplateId]) =='undefined' 
									 || itemSettings[noticeSetData[i].wxNotifyTemplateId] == ''){
										noticeSetData[i].status = false;
									}else if (typeof(itemSettings[noticeSetData[i].wxNotifyTemplateId]) !='undefined' 
										&& itemSettings[noticeSetData[i].wxNotifyTemplateId] != '' 
										&& itemSettings[noticeSetData[i].wxNotifyTemplateId] != 'accept') {
										noticeSetData[i].status = false
									}else{
										noticeSetData[i].status = true
									}
								}
							}
						}
						
						this.noticeSetData.pageDatas = noticeSetData;
						this.loadingText = "加载完成...";
					
					},
					fail: (res) => {
						 this.noticeSetData.pageDatas = noticeSetData;
						 this.loadingText = "加载完成...";
					}
				})
			}
		},
		onLoad() {
			console.log('The page is be onLoad')
			uni.showLoading({title:"加载中..."})
			this.initData();
		},
		onPullDownRefresh(){
			uni.showLoading({title:"加载中..."})
			
			setTimeout(() => {
				this.noticeSetData.pageDatas = [];
				this.loadingText = "加载中...";
				this.noticeSetPageQuery();
				uni.stopPullDownRefresh();
			},1000)
		},
		// 上拉加载
		onReachBottom(){
			uni.showLoading({title:"加载中..."})
			this.noticeSetPageQuery();
		},
	}	
</script>

<style lang="scss" scoped>
	.notice-list{
		.notice-box{
			padding: 20upx;
			display: flex;
			justify-content: space-between;
			-webkit-box-orient: horizontal;
			-webkit-box-direction: normal;
			-webkit-flex-direction: row;
			flex-direction: row;
			border-bottom: 1upx solid #ccc;
			
			.notice-box-left{
				width: 15%;
				
				.icon {
					padding-top: 0upx;
					padding-right: 20upx;
					padding-bottom: 0upx;
					padding-left: 0upx;
					width: 80upx;
					height: 80upx;
					display: flex;
					justify-content: flex-end;
					align-items: center;
					font-size: 80upx;
					color: #ff9900;
				}
			}	
			
			.notice-box-right{
				width: 85%;
				
				.notice-box-right-row1{
					margin-top: 10upx;
					display: flex;
					justify-content: space-between;
					-webkit-box-orient: horizontal;
					-webkit-box-direction: normal;
					-webkit-flex-direction: row;
					flex-direction: row;
					font-size: 30upx;
					
					.notice-box-tittle{
						font-size: 35upx;
						font-weight: 800upx;
					}
					.notice-box-time{
						font-size: 28upx;
						font-weight: 500upx;
					}
				}
				.notice-box-right-row2{
					margin-top: 10upx;
					font-size: 30upx;
					font-weight: 500upx;
					display: flex;
					justify-content: space-between;
					-webkit-box-orient: horizontal;
					-webkit-box-direction: normal;
					-webkit-flex-direction: row;
					flex-direction: row;
					
					.notice-box-content{
						width: 75%;
						font-size: 28upx;
					}
					.notice-box-option{
						font-size: 28upx;
						
						button{
							font-size: 25upx;
							text-align: center;
							vertical-align:middle ;
							line-height:22px;
							background-color: #ffaa00;
							color: #ffffff;
							text-align: center;
						}
					}
					
				}
			}
		}
	}
	.loading-text {
		width: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 60upx;
		color: #979797;
		font-size: 24upx;
	}
</style>
