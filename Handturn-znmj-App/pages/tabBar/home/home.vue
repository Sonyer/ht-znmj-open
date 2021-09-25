<template>
	<view>
		<view>
			<!-- <page-status></page-status> -->
			<!--自定义导航栏-->
			<searchHeader @clickSearch="handleSearch" :searchValue="searchValue" :locationShow="locationShow" :publishShow="publishShow" :locationFlag="locationFlag"/>
		</view>
		
		<view class="showList" v-if="authAreaData.pageDatas.length > 0">
			<view class="showList-box" v-for="(showData,index) in authAreaData.pageDatas" :key="index">
				<view class="showList-oc-info">
					<view class="showList-oc-info-avatar" v-bind:id="index">
						<image :src="showData.logoRequest"/>
					</view>
					<view class="showList-box-info-content">
						<view class="showList-box-info-content-row-1">
							<view>{{ showData.ocName }}</view> 
						</view>
						<view class="showList-box-info-content-row-2" v-bind:id="index">
							<view>{{ showData.authAreaName }}</view> 
						</view>
						<view class="showList-box-info-content-row-3" v-bind:id="index" @tap="showPersionHome">
							<view v-if="showData.authStr != ''">{{ showData.authStr }}</view> 
							<view v-else>无特殊权限</view> 
						</view>
					</view>
					<view class="showList-oc-info-visite" v-bind:id="index">
						<!-- <button>邀请</button> -->
						<button class="btn-share" open-type="share" v-bind:id="index">邀请</button>
					</view>
				</view>
				
			</view>
		</view>
		<view class="loading-text">{{loadingText}}</view>
	</view>
</template>

<script>
	import searchHeader from "../../components/searchHeader/searchHeader.vue"
	
	export default{
		data() {
			return {
				locationShow: false,
				publishShow: false,
				locationFlag: false,
				searchValue: '',
				
				authAreaData:{
					pageIndex: 1,
					pageDatas:[],
				},	
					
				loadingText: "正在加载....",
			}
		},
		components:{
			searchHeader,
		},
		methods: {
			showPersionHome(e){
				var index = e.currentTarget.id;
				const authAreaData = this.authAreaData.pageDatas[index];
				var navigateToUrl = '../../visitorApply/visitorApply?orgCode='+authAreaData.orgCode+'&ocCode='+authAreaData.ocCode+'&authAreaCode='+authAreaData.authAreaCode
					+'&orgName='+authAreaData.orgName+'&ocName='+authAreaData.ocName+'&authAreaName='+authAreaData.authAreaName;
				uni.navigateTo({
					url:navigateToUrl
				});	
			},
			handleSearch(data){
				this.searchValue = data.searchValue;
				this.authAreaData.pageIndex = 1;
				this.authAreaData.pageDatas = [];
				this.initData();
			},
			initData() {
				this.pageQuery();
			},
			pageQuery(){
				this.checkLogin().then(val => {
					if(val === true){
						this.https({
							url: this.interfaces.authAreaPageQuery,  //POST json传输必须未转字符串
							method: 'POST',
							data: {
								pageIndex:this.authAreaData.pageIndex,
								searchValue: this.searchValue
							},
							header: {
								'content-type': 'application/json'
							},
							success: ((res) => { 
								if(res.data.pageDatas.length <= 0){
									this.authAreaData.pageIndex --;
									this.loadingText = "没有更多";
								}else{
									//获取发布内容
									this.loadingText = "上拉获取更多";
									this.authAreaData.pageDatas = this.authAreaData.pageDatas.concat(res.data.pageDatas);
								}
							})
						}); 
					}
				});
			},
			async checkLogin(){
				//获取同步获取当前storeage的相关信息
				let fromUrl = "/pages/tabBar/home/home";
				let toUrl = ""; //无需跳转其他页面
				let refuseUrl = '/pages/tabBar/home/home';
				let authFinishedToUrl = '/pages/tabBar/home/home'; //授权完成后跳转页面
				let authUrl = "/pages/auth/login";
				let tokenVerify = true;
				return await this.login.checkLogin(fromUrl,toUrl,refuseUrl,authUrl,tokenVerify,authFinishedToUrl);
			},
		},
		onLoad() {	
			console.log('The page is be onLoad')
			uni.showLoading({title:"加载中..."})
			this.initData();
			
		},
		onPullDownRefresh(){
			uni.showLoading({title:"加载中..."})
			setTimeout(() => {
				this.authAreaData.pageIndex = 1;
				this.authAreaData.pageDatas = [];
				this.loadingText = "加载中...";
				this.pageQuery();
				uni.stopPullDownRefresh();
			},1000)
			
		},
		onPageScroll(e){ //根据距离顶部距离是否显示回到顶部按钮
			
		},
		// 上拉加载
		onReachBottom(){
			uni.showLoading({title:"加载中..."})
			this.authAreaData.pageIndex++;
			this.pageQuery();
		},
		onReady(){
			
		},
		mounted() {
			console.log('The page is be mounted')
			
		},
		onShareAppMessage: function(e){
		　　var that = this;
			
			
			var index = e.target.id;
			const clickAuthData = this.authAreaData.pageDatas[index];
			
		　　// 设置菜单中的转发按钮触发转发事件时的转发内容
			var navigateToUrl = '/pages/visitorApply/visitorApply?orgCode='+clickAuthData.orgCode+'&ocCode='+clickAuthData.ocCode+'&authAreaCode='+clickAuthData.authAreaCode
				+'&orgName='+clickAuthData.orgName+'&ocName='+clickAuthData.ocName+'&authAreaName='+clickAuthData.authAreaName;
		　　var shareObj = {
		　　　　title: "["+clickAuthData.ocName+"]->["+clickAuthData.authAreaName+"],人脸识别门禁邀请。",     // 默认是小程序的名称(可以写slogan等)
		　　　　path: navigateToUrl,        // 默认是当前页面，必须是以‘/’开头的完整路径
		　　　　imageUrl: clickAuthData.logoRequest     //自定义图片路径，可以是本地文件路径、代码包文件路径或者网络图片路径，支持PNG及JPG，不传入 imageUrl 则使用默认截图。显示图片长宽比是 5:4
		　　};
			
		　　// 来自页面内的按钮的转发
		　　if(e.from === "button" ){
				/* shareObj.imageUrl = that.publishImgs[0]; */
		　　}
		　　// 返回shareObj
		　　return shareObj;
		}
	}
</script>

<style lang="scss" scoped>
//清单展示
	.showList {
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
					width: 140upx;
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
					margin-left: 1rem;
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
					width: 120upx;
					height: 120upx;
					display:flex;
					-webkit-box-align: center;
					-webkit-align-items: center;
					align-items: center;
					
					
					button{
						height: 120upx;
						text-align: center;
						color: #fff;
						background-color: #ffaa00;
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


