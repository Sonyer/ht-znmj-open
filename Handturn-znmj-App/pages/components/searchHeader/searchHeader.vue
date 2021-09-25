<template>
	<view>
		<view class="header">
			<!-- 搜索框 -->
			<view class="input-box">
				<input type="text" @input="onInputSearchValue" :value="searchValue" @blur="onblurInputSearchValue()" placeholder="请输入关键词..." placeholder-style="color:#c0c0c0" />
				<view class="icon iconfont">&#xe63d;</view>
			</view>
		</view>
		<!-- 占位 -->
		<view class="place"></view>
	</view>
</template>

<script>
	
	export default{
		props:{
			searchValue:String,
		},
		data () {
			return {
				searchValueInput:'',
			}
		},
		methods:{
			onInputSearchValue(e){
				this.searchValueInput = e.detail.value;
			},
			onblurInputSearchValue(){ 
				this.$emit('clickSearch',{searchValue:this.searchValueInput});
			},
		}
	}
</script>

<style lang="scss">
	.header {
		width: 92%;
		padding: 0 4%;
		height: 80upx;
		display: flex;
		align-items: center;
		position: fixed;
		z-index: 10;
		background-color: #ff9900;
		top: 0;
		// 条件编译 设置离顶部的高度
		/*  #ifdef  MP  */
		/* top: var(--status-bar-height); */
		/*  #endif  */
		
		/*  #ifdef  APP-PLUS  */
		top: var(--status-bar-height);
		/*  #endif  */

		.input-box {
			margin-left: 20upx;
			margin-right: 20upx;
			width: 100%;
			height: 60upx;
			background-color: #f5f5f5;
			border-radius: 30upx;
			position: relative;
			display: flex;
			align-items: center;

			.icon {
				display: flex;
				align-items: center;
				position: absolute;
				top: 0;
				right: 0;
				width: 60upx;
				height: 60upx;
				font-size: 52upx;
				color: #ffaa00;
			}

			input {
				padding-left: 28upx;
				height: 28upx;
				font-size: 28upx;
			}
		}
		
		.icon-btn {
			width: 60upx;
			height: 60upx;
			flex-shrink: 0;
			display: flex;

			.icon {
				width: 60upx;
				height: 60upx;
				margin-left: 5upx;
				display: flex;
				justify-content: flex-end;
				align-items: center;
				font-size: 60upx;
				color: #ffffff;
			}
		}
	}

	.place {
		background-color: #ffffff;
		height: 80upx;
		/*  #ifdef  APP-PLUS  */
		margin-top: var(--status-bar-height);
		/*  #endif  */
	}
</style>




