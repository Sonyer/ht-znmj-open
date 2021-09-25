<template>
	<view>
		<view class="content">
			<view class="icon iconfont" :class="['icon-'+iconStr]" style="color:#ffaa00">
				
			</view>
			<view class="content-detail">
				{{detail}}
			</view>
			<button v-if="toUrl !=null && toUrl != ''" v-bind:id="toUrl" @tap="switchPage">{{buttonText}}</button>
		</view>
	</view>
</template>

<script>
	export default{
		props:{
			/*底部弹窗的属性*/
			detail:{
				type:String,
				default:'购物车满满，幸福又美满。'
			},
			toUrl:{
				type:String,
				default:""
			},
			iconStr:{
				type:String,
				default:"shouye10"
			},
			buttonText:{
				type:String,
				default:"去首页看看"
			}
		},
		data() { 
			return {}
		},
		methods:{
			
			switchPage(e){
				var toUrl = e.currentTarget.id;
				
				uni.switchTab({
					url:toUrl
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.content{
		display: flex;
		align-items: center; 
		text-align: center;
		width: 100%;
		height: 100%;
		position: fixed;
		-webkit-box-orient: vertical;
		-webkit-box-direction: normal;
		-webkit-flex-direction: column;
		flex-direction: column;
		justify-content: center;
		
		.icon {
			padding-top: 0upx;
			padding-right: 20upx;
			padding-bottom: 0upx;
			padding-left: 20upx;
			display: flex;
			justify-content: flex-end;
			align-items: center;
			font-size: 150upx;
			color: #ffaa00;
		}
		
		.content-detail{
			margin: 20upx;
			font-size: 28upx;
			color: #999999;
		}
		
		button{
			color: #ffffff;
			background-color: #ff5500;
			font-size: 28upx;
		}
	}
</style>
