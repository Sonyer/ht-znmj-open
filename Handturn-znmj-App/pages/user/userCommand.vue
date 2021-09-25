<template>
	<view>
		<form @submit="commandSubmit">
			<view class="command-content">
				<view class="command-box">
					<view class="command-box-command">
						<view class="command-box-command-lable">
							邀请口令:
						</view>
						<input type="text" name="commandCode" :value="commandData.commandCode" />
					</view>
					<view class="command-box-command">
						<view class="command-box-command-lable">
							账号名称:
						</view>
						<input type="text" name="userName" :value="commandData.userName" />
					</view>
					<view class="command-box-command">
						<view class="command-box-command-lable">
							手机号码:
						</view>
						<input type="text" name="phoneNumber" v-model="commandData.phoneNumber"/>
					</view>
					<view class="command-box-command">
						<view class="command-box-command-lable">
							验证码:
						</view>
						<view class="command-box-command-input-horizontal">
							<input class="command-box-command-input-text" type="number" name="validateCode"/>
							<button v-if="verifyShow" @click="getValidateCode">获取</button>
							<button v-else style="background-color: #C0C0C0;">{{numTime+'秒'}}</button>
						</view>
					</view>
				</view>
				
				<view class="command-option">
					<button formType="submit" type="primary">提交验证</button> 
				</view>
			</view>
		</form>
	</view>
</template>

<script>
	export default{
		components:{
			
		},
		data() {
			return {
				verifyShow: true,
				numTime: 0,
				
				commandData:{
					commandCode:"",
					userName:"",
					phoneNumber:"",
					validateCode:""
				}
			}
		},
		methods:{
			
			commandSubmit(e){
				if(e.detail.value.commandCode == '' || typeof(e.detail.value.commandCode) == 'undefined'){
					uni.showModal({
					    content: '管理口令必须填写！',
					    showCancel: false
					});
					return;
				}
				
				if(e.detail.value.userName == '' || typeof(e.detail.value.userName) == 'undefined'){
					uni.showModal({
					    content: '管理账号必须填写！',
					    showCancel: false
					});
					return;
				}
				
				if(e.detail.value.phoneNumber == ''|| typeof(e.detail.value.phoneNumber) == 'undefined'){
					uni.showModal({
					    content: '必须填写联系手机号码！',
					    showCancel: false
					});
					return;
				}else{
					if(!(/^1[1|2|3|4|5|6|7|8|9|0][0-9]\d{4,8}$/.test(e.detail.value.phoneNumber))){
					  uni.showModal({
					      content: '手机号码格式错误！',
					      showCancel: false
					  });
					  return;
					}
				}
				
				if(e.detail.value.validateCode == '' || typeof(e.detail.value.validateCode) == 'undefined'){
					uni.showModal({
					    content: '验证码必须填写！',
					    showCancel: false
					});
					return;
				}
				
				
				const submitData = {
					commandCode:typeof(e.detail.value.commandCode)=='undefined'?'':e.detail.value.commandCode,
					userName:typeof(e.detail.value.userName)=='undefined'?'':e.detail.value.userName,
					phoneNumber:typeof(e.detail.value.phoneNumber)=='undefined'?'':e.detail.value.phoneNumber,
					validateCode:typeof(e.detail.value.validateCode)=='undefined'?'':e.detail.value.validateCode,
				};
			
				uni.showModal({
					title: '提交验证',
					content: '确定要提交验证吗？',
					cancelText: '否',
					confirmText: '是',
					success: res => {
						if (res.confirm) {
							this.https({
								url: this.interfaces.subimitCommand,  //POST json传输必须未转字符串
								method: 'POST',
								data: submitData,
								header: {
									'content-type': 'application/json'
								},
								success: ((res) => { 
									uni.showModal({
										content: '提交验证完成！',
										showCancel: false
									});
									//关闭当前窗口
									uni.navigateBack({
										
									})
								})
							}); 
						}
					},
				});
			},
			getValidateCode(){
				console.log(this.commandData);
				if(this.commandData.phoneNumber == null || this.commandData.phoneNumber == '' || typeof (this.commandData.phoneNumber) == 'undefined'){
					uni.showModal({
					    content: '必须填写手机号码！',
					    showCancel: false
					});
					return;
				}else{
					if(!(/^1[1|2|3|4|5|6|7|8|9|0][0-9]\d{4,8}$/.test(this.commandData.phoneNumber))){
					  uni.showModal({
					      content: '手机号码格式错误！',
					      showCancel: false
					  });
					  return;
					}
				}
				
				
				this.https({
					url: this.interfaces.validateCode,  //POST json传输必须未转字符串
					method: 'POST',
					data: {
						phoneNumber:this.commandData.phoneNumber
					},
					header: {
						'content-type': 'application/json'
					},
					success: ((res) => { 
						this.numTime = 60;
						this.timedown(this.numTime);
					})
				}); 
			},
			timedown:function(num){
				let that = this;
				if(num == 0){
					that.verifyShow = true;				// 是否显示获取验证码
					return clearTimeout();
				}else{
					that.verifyShow = false;			// 是否显示获取验证码
					setTimeout(function() {  
						that.numTime = num-1
						that.timedown(num-1);  
					}, 1000);//定时每秒减一  
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.command-content{
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
	}
	
	input{
		margin-top: 0upx;
		margin-right: 20upx;
		margin-left: 20upx;
		margin-bottom: 20upx;
		border-color: #878787;
		border-style: solid;
		border-top-width: 0px;
		border-right-width: 0px;
		border-bottom-width: 1px;
		border-left-width: 0px
	}
	
	.command-box{
		width: 100%;
		padding-bottom: 100upx;
		/*  #ifdef  MP  */
		/* padding-top: var(--status-bar-height); */
		/*  #endif  */
		display: flex;
		-webkit-box-orient: vertical;
		-webkit-box-direction: normal;
		-webkit-flex-direction: column;
		flex-direction: column;
		justify-content: center;
		position: relative;
		
		.command-box-command{
			margin-top: 10upx;
			/* margin-right: 40upx; */
			margin-bottom: 10upx;
			/* width: 100%; */
			-webkit-box-orient: horizontal;
			-webkit-box-direction: normal;
			-webkit-flex-direction: row;
			flex-direction: row;
			justify-content: center;
			position: relative;
			display: flex;
			
			.command-box-command-lable{
				color: #ffaa00;
				text-align: left;
				justify-content	: flex-end;
			}
			
			input {
				font-size: 38upx;
			}
			
			.command-box-command-input-horizontal{
				width: 75%;
				-webkit-box-orient: horizontal;
				-webkit-box-direction: normal;
				-webkit-flex-direction: row;
				flex-direction: row;
				justify-content: center;
				position: relative;
				display: flex;
				
				.command-box-command-input-text{
					border-color: #878787;
					border-style: solid;
					border-top-width: 0px;
					border-right-width: 0px;
					border-bottom-width: 1px;
					border-left-width: 0px;
					font-size: 30upx;
					margin: 10upx;
					border-top-width: 0px;
					border-right-width: 0px;
					border-left-width: 0px;	
				}
					
				button{
					font-size: 30upx;
					text-align: center;
					vertical-align:middle ;
					line-height:28px;
					color: #fff;
					background-color: #ffaa00;
				}
			}
			
		}
	}
	
	.command-option{
		bottom:0upx;
		width: 100%;
		height: 100upx;
		position:fixed;
		align-items: center;
		font-size: 35upx;
		background-color: #ffffff;
		
		button{
			font-size: 35upx;
		}
	}
</style>
