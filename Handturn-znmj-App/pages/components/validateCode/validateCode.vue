<template>
		<view class="validate-container">
			<view class="code-input-main">
				<view class="inputLine">
	 
					<input class="input-item" maxlength="1" :value="code[0]"></input>
					<input class="input-item" maxlength="1" :value="code[1]"></input>
					<input class="input-item" maxlength="1" :value="code[2]"></input>
				</view>
				<view class="inputLine">
	 
					<input class="input-item" maxlength="1" :value="code[3]"></input>
					<input class="input-item" maxlength="1" :value="code[4]"></input>
					<input class="input-item" maxlength="1" :value="code[5]"></input>
				</view>
	 
	 
				<input @input="inputEvent" class="code-input-input" v-model="code" maxlength="6" type="number" />
			</view>
			
			<button>提交验证</button>
		</view>

</template>

<script>
	export default {
			data() {
				return {
					code: '',
				};
			},
			
			methods: {
				inputEvent(res){
					console.log("input 1 input code  components : ", res.detail.value);	   
				}
			}
		}
</script>
<style>
	.validate-container{
		width: 100%;
		height: 100%;
		background: #FFFFFF;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		z-index: 10;
	}
	
	.validate-tittle{
		width: 100%;
		font-size: 35upx;
		text-align: center;
		align-items: center;
		justify-content: center;
		color: #FFFFFF;
	}
	.code-input-input {
			height: 400upx;
			position: absolute;
			width: 100%;
			outline: none;
			color: transparent;
			text-shadow: 0 0 0 transparent;
			width: 300%;
			margin-left: -100%;
			background: #00000000;
		}
	 
		.code-input-main {
			padding: 20upx;
			display: flex;
			flex-direction: column;
			width: 100%;
			height: 400upx;
			background-color: #F8F8F8;
		}
	 
		.input-item {
			width: 110upx;
			height: 110upx;
			font-size: 40upx;
			background: #ffffff;
			text-align: center;
			border-radius: 5%;
			margin-left: 40upx;
			margin-right: 40upx;
			color: #000000;
			border:1px dashed #ffaa00; 
		}
	 
		.inputLine {
			display: flex;
			justify-content: center;
			width: 100%;
			margin-top: 80upx;
		}
</style>
