<template>
	<view>
		<view class="notice-list">
			<view class="notice-box" v-for="(notice,index) in noticeData.pageDatas" :key="index">
				<view class="notice-box-left">
					<view class="icon iconfont">&#xe630;</view>
				</view>
				<view class="notice-box-right">
					<view class="notice-box-right-row1">
						<view class="notice-box-tittle">
							{{notice.notifyType}}
						</view>
						<view class="notice-box-time">
							{{notice.createDate}}
						</view>
					</view>
					<view class="notice-box-right-row2">
						<view class="notice-box-content">
							{{notice.notifyMessage}}
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
				noticeData:{
					pageIndex: 1,
					pageDatas:[]
				},
				loadingText: "正在加载....",
			}
		},
		methods: {
			initData() {
				this.noticePageQuery();
			},
			noticePageQuery(){
				this.https({
					url: this.interfaces.initMeNotice,  //POST json传输必须未转字符串
					method: 'POST',
					data: {
						'pageIndex':this.noticeData.pageIndex
					},
					header: {
						'content-type': 'application/json'
					},
					success: ((res) => { 
						if(res.data.length <= 0){
							this.noticeData.pageIndex --;
							this.loadingText = "没有更多";
						}else{
							//获取发布内容
							this.loadingText = "上拉获取更多";
							this.noticeData.pageDatas = this.noticeData.pageDatas.concat(res.data);
						}
					})
				}); 
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
				this.noticeData.pageIndex = 1;
				this.noticeData.pageDatas = [];
				this.loadingText = "加载中...";
				this.noticePageQuery();
				uni.stopPullDownRefresh();
			},1000)
		},
		// 上拉加载
		onReachBottom(){
			uni.showLoading({title:"加载中..."})
			this.noticeData.pageIndex++;
			this.noticePageQuery();
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
