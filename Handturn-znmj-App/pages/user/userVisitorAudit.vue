<template>
	<view>
		<view>
			<!--栏目导航 --> 
			<scroll-view 
			class="column-scroll" 
			scroll-x 
			scroll-with-animation 
			v-if="tabData.tabs.length > 0" 
			:scroll-into-view="'s' + tabData.tabs.currentIndex">
				<view :class="['column-scroll-item', { active: index == tabData.currentId}]" 
				:id="'s' + index" 
				v-for="(item, index) in tabData.tabs"
				:key="index" 
				@tap="handleTabsScroll(index)">
				{{ item.tabName }}
					<view class="column-bottom-border" v-if="index == tabData.currentId">
						<!-- 底部红色短border -->
					</view>
				</view>
			</scroll-view>
		</view>
		
		<view class="showList" v-if="pageDatas.length > 0">
			<view class="showList-box" v-for="(showData,index) in pageDatas" :key="index">
				<view class="showList-oc-info">
					<view class="showList-oc-info-avatar" v-bind:id="index">
						<image :src="showData.faceImgRequest"/>
					</view>
					<view class="showList-box-info-content">
						<view class="showList-box-info-content-row-1">
							<view>{{ showData.ocName }}</view> 
						</view>
						<view class="showList-box-info-content-row-2" v-bind:id="index">
							<view>{{ showData.authAreaName }}</view> 
						</view>
						<view class="showList-box-info-content-row-3" v-bind:id="index">
							<view>{{ showData.idCardName }}-({{ showData.positionName }})</view> 
						</view>
					</view>
					<view class="showList-oc-info-visite" v-bind:id="index" v-if="showData.auditStatus === '未审核' || showData.auditStatus === '审核中'">
						<button v-bind:id="index" @tap="visitorAuditPre">审核</button>
						<button v-bind:id="index" @tap="visitorCancelPre">驳回</button>
					</view>
					<view class="showList-oc-info-visite" v-bind:id="index" v-else>
						<button class="noneButton">{{showData.auditStatus}}</button>
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
				pageIndex: 1,
				pageDatas:[],
				
				wxNotifyTemplatIds:[],
				
				loadingText: "正在加载....",
				
				locationShow: false,
				publishShow: false,
				locationFlag: false,
				
				tabData:{
					isFixed: true,
					currentId: 0,
					currentIndex: 0, 
					currentStatusType: '0',
					tabs:[
							{
								tabName:"全部",
								statusType:'0'
							},
							{
								tabName:"待审核",
								statusType:'1'
							},
							{
								tabName:"已审核",
								statusType:'2'
							},
							{
								tabName:"审核失败",
								statusType:'3'
							}
						]
				},	
			}
		},
		components:{
			
		},
		methods: {
			handleTabsScroll(index){
				this.tabData.currentId = index;
				this.tabData.currentIndex = Math.max(0, index - 1); 
				
				this.tabData.currentStatusType = this.tabData.tabs[index].statusType;
				this.pageIndex = 1;
				this.pageDatas = [];
				this.queryPage();
			},
			queryPage(){
				this.checkLogin().then(val => {
					if(val === true){
						this.https({
							url: this.interfaces.initVisitorAudit,  //POST json传输必须未转字符串
							method: 'POST',
							data: {
								pageIndex:this.pageIndex,
								currentStatusType:this.tabData.currentStatusType
							},
							header: {
								'content-type': 'application/json'
							},
							success: ((res) => { 
								this.wxNotifyTemplatIds = res.data.wxNotifyTemplatIds;
								
								if(res.data.pageDatas.length <= 0){
									if(this.pageIndex == 1){
										this.pageDatas = res.data.pageDatas;
									}
									this.pageIndex --;
									this.loadingText = "没有更多";
								}else{
									if(this.pageIndex == 1){
										this.pageDatas = res.data.pageDatas;
									}else{
										this.pageDatas = this.pageDatas.concat(res.data.pageDatas);	
									}
									//获取发布内容
									this.loadingText = "上拉获取更多";
								}
								
								//消息订阅
								//this.handUserNoticeSet();
							})
						}); 
					}
				});
			},
			visitorAuditPre(e){
				var evenData = e;
				var that = this;
				
				uni.showLoading({title:"加载中..."})
				if(that.wxNotifyTemplatIds == null || that.wxNotifyTemplatIds =='' || typeof(that.wxNotifyTemplatIds) == 'undefined'){
					that.visitorAudit(evenData);
				}else{
					// 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
					uni.requestSubscribeMessage({
					   tmplIds: that.wxNotifyTemplatIds,
					   success (res) {
						   uni.hideLoading();
						   console.log(res)
						   that.visitorAudit(evenData);
					   },
					   fail:(res) => {
						  uni.hideLoading();
						  console.log(res)
						   that.visitorAudit(evenData);
					   }
					});
				 }
			},
			visitorAudit(e){
				var index = e.currentTarget.id;
				
				const visitorApplyData = this.pageDatas[index];
				
				uni.showModal({
					title: '审核确认',
					content: '确定审核确认吗？(该操作后将无法撤回)',
					cancelText: '否',
					confirmText: '是',
					success: res => {
						if (res.confirm) {
							this.https({
								url: this.interfaces.auditVisitor,  //POST json传输必须未转字符串
								method: 'POST',
								data: {
									visitorApplyId:visitorApplyData.id
								},
								header: {
									'content-type': 'application/json'
								},
								success: ((res) => { 
									visitorApplyData.auditStatus = res.data;
									
								})
							}); 
						}
					}
				})            
			},
			visitorCancelPre(e){
				var evenData = e;
				var that = this;
				
				uni.showLoading({title:"加载中..."})
				if(that.wxNotifyTemplatIds == null || that.wxNotifyTemplatIds =='' || typeof(that.wxNotifyTemplatIds) == 'undefined'){
					that.visitorCancel(evenData);
				}else{
					// 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
					uni.requestSubscribeMessage({
					   tmplIds: that.wxNotifyTemplatIds,
					   success (res) {
						   uni.hideLoading();
						   console.log(res)
						   that.visitorCancel(evenData);
					   },
					   fail:(res) => {
						  uni.hideLoading();
						  console.log(res)
						   that.visitorCancel(evenData);
					   }
					});
				 }
			},
			visitorCancel(e){
				var index = e.currentTarget.id;
				
				const visitorApplyData = this.pageDatas[index];
				
				uni.showModal({
					title: '驳回确认',
					content: '确定驳回确认吗？(该操作后将无法撤回)',
					cancelText: '否',
					confirmText: '是',
					success: res => {
						if (res.confirm) {
							this.https({
								url: this.interfaces.cancelVisitor,  //POST json传输必须未转字符串
								method: 'POST',
								data: {
									visitorApplyId:visitorApplyData.id
								},
								header: {
									'content-type': 'application/json'
								},
								success: ((res) => { 
									visitorApplyData.auditStatus = res.data;
									
								})
							}); 
						}
					}
				})            
			},
			async checkLogin(){
				//获取同步获取当前storeage的相关信息
				let fromUrl = "/pages/user/userVisitorAudit";
				let toUrl = ""; //无需跳转其他页面
				let refuseUrl = '/pages/tabBar/home/home';
				let authFinishedToUrl = '/pages/tabBar/home/home'; //授权完成后跳转页面
				let authUrl = "/pages/auth/login";
				let tokenVerify = true;
				return await this.login.checkLogin(fromUrl,toUrl,refuseUrl,authUrl,tokenVerify,authFinishedToUrl);
			},
		},
		onLoad() {	
			uni.showLoading({title:"加载中..."})
			
			this.pageIndex = 1;
			this.pageDatas = [];
			this.queryPage();
		},
		onPullDownRefresh(){
			uni.showLoading({title:"加载中..."})
			setTimeout(() => {
				this.pageIndex = 1;
				this.pageDatas = [];
				this.loadingText = "加载中...";
				this.queryPage();
				uni.stopPullDownRefresh();
			},1000)
		},
		onPageScroll(e){ //根据距离顶部距离是否显示回到顶部按钮
			
		},
		// 上拉加载
		onReachBottom(){
			this.pageIndex++;
			this.queryPage();
		},
		onReady(){
			
		},
		mounted() {
			console.log('The page is be mounted')
			
		},	
	}
</script>

<style lang="scss" scoped>
	.column-scroll {
		position: fixed;/*固定位置*/
		top:0;
		z-index:9998;/*设置优先级显示，保证不会被覆盖*/
		/*设置优先级显示，保证不会被覆盖*/
		box-sizing: border-box;
		padding-left: 20upx;
		white-space: nowrap;
		height: 80upx;
		background-color: #ffffff;
		border-bottom: 1upx solid #ccc;
		
		&.fixed{
			position: fixed;
			top: 0;
			left: 0;
			right: 0;
			z-index:9998;/*设置优先级显示，保证不会被覆盖*/
		}
		
		.column-scroll-item {
		  text-align: center;
		  display: inline-block;
		  padding: 0 25upx;
		  line-height: 70upx;
		  font-size: 30upx;
		  font-weight: 400;
		  color: #1e1e1e;
	
		  &.active {
			display: inline-block;
			font-weight: bold;
			color: #ffaa00;
			font-size: 30upx;
			// border-bottom: 2px solid #FF4F52;
		  }
		}
		.column-bottom-border {
		  margin: 0 auto;
		  width: 80upx;
		  height: 10upx;
		  background: rgba(255, 79, 82, 1);
		  border-radius: 5upx;
		}
	}
	
//清单展示
	.showList {
		margin-top: 80upx; 
		width: 100%;
		display: flex;
		-webkit-box-orient: vertical;
		-webkit-box-direction: normal;
		-webkit-flex-direction: column;
		flex-direction: column;
		justify-content: center;
		
		.showList-box {
			width: 97%;
			justify-content: space-between;
			display: flex;
			-webkit-box-orient: horizontal;
			-webkit-box-direction: normal;
			-webkit-flex-direction: row;
			flex-direction: row;
			//兼容ios，微信小程序
			position: relative;
			border-style: solid; 
			border-width: 5px 5px 5px 5px;
			border-color:#e8e5e5;
			
			.showList-oc-info {
				width: 95%;
				display: flex;
				-webkit-box-align: center;
				-webkit-align-items: center;
				align-items: center;
				padding: 15upx;
				//兼容ios，微信小程序
				position: relative;
				
				.showList-oc-info-avatar{
					width: 120upx;
					height: 120upx;
					display:flex;
					position: relative;
					
					image{
						width: 100%;
						height: 100%;
						display: block;
						border: none;
						border-radius: 10%;
						position: relative;
					} 
				}
				.showList-box-info-content{
					width: 100%;
					height: 120upx;
					margin-left: 0.5rem;
					-webkit-box-orient: vertical;
					-webkit-box-direction: normal;
					-webkit-flex-direction: column;
					flex-direction: column;
						
					
					.showList-box-info-content-row-1{
						justify-content: space-between;
						width: 100%;
						height: 40upx;
						font-size: 30upx;
						font-weight: 800;
						display: flex;
						-webkit-box-orient: horizontal;
						-webkit-box-direction: normal;
						-webkit-flex-direction: row;
						flex-direction: row;	
						
					}
					
					.showList-box-info-content-row-2{
						width: 100%;
						height: 40upx;
						display: flex;
						align-items: center;
						font-size: 28upx;
						-webkit-box-orient: horizontal;
						-webkit-box-direction: normal;
						-webkit-flex-direction: row;
						flex-direction: row;
						
					}
					
					.showList-box-info-content-row-3{
						width: 100%;
						height: 40upx;
						display: flex;
						align-items: center;
						font-size: 28upx;
						-webkit-box-orient: horizontal;
						-webkit-box-direction: normal;
						-webkit-flex-direction: row;
						flex-direction: row;
						
					}	
				}
			
				.showList-oc-info-visite{
					width: 30%;
					height: 120upx;
					display:flex;
					-webkit-box-orient: vertical;
					-webkit-box-direction: normal;
					-webkit-flex-direction: column;
					flex-direction: column;
					-webkit-box-align: center;
					-webkit-align-items: center;
					align-items: center;
					
					
					button{
						margin: 5upx;
						height: 50upx;
						text-align: center;
						font-size: 20upx;
						color: #fff;
						background-color: #ffaa00;
					}
				
					.noneButton{
						margin: 5upx;
						height: 50upx;
						text-align: center;
						font-size: 20upx;
						color: #fff;
						background-color: #bfbfbf;
						vertical-align:middle ;
						
						/* color: #fff;
						background-color: #bfbfbf;
						height: 40upx;
						font-size: 20upx;
						text-align: center;
						width: 100%;
						margin-bottom: 10upx;
						vertical-align:middle ; */
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


