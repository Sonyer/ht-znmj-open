<template>
	<view class="popup-layout-wrap " :class="popuplayoutClass" >
		<view class="popup-layout-content" :class="popupContentClass" :style="[{height:height}]">
			<view class="link-address-wrap">
				<view class="link-adress-content">
					<view class="head-wrap">
						<text class="cancel" @click="btn_cancel">取消</text>
						<text class="confirm" @click="btn_confirm">确认</text>
					</view>
					<view class="head-selected">
						<text class="selected-txt">修改昵称</text>
					</view>
					<view class="operation-wrap">
						<view class="operation-container">
							<view class="operation-content">
								<scroll-view
									scroll-y="true"
									class="operation-info"
									show-scrollbar="false">
									<view class='operation-info-txt'>
										<view class="operation-info-box">								
											 <view class="operation-info-box-text">
												 <input class="uni-input" cursor-spacing="20" v-model="inputInfo.nickName" placeholder-style="color:#c0c0c0" placeholder="写下你昵称...">
											 </view>
										</view>
									</view>
								</scroll-view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view v-if="maskShow" class="popup-layout-mask" @tap="close(maskClick)"></view>
	</view>
</template>

<script>

	
	export default{
		data(){
			return{
				newActive:"",
				newTransition:true,
				/*用户输入信息*/
				inputInfo:{
					nickName:''
				}
			};
		},
		props:{
			/*底部弹窗的属性*/
			active:{
				type:Boolean,
				default:false
			},
			height:{
				type:[String],
				default:"100%"
			},
			//遮盖层显示
			maskShow:{
				type:Boolean,
				default:true
			},
			//遮盖层点击
			maskClick:{
				type:Boolean,
				default:true
			}
		},
		computed:{
			popuplayoutClass:function(){
				let _class="";
				if(this.newActive){
					_class+="popup-layout-active";
				}	
				_class+=" popup-layout-bottom";
				return _class;
			},
			popupContentClass:function () {
				let _class = "";
				if (this.newTransition&&this.transition!=='none') {
					_class+="popup-layout-transition-slider"
				}
				return _class;
			}
		},
		methods: {
			//显示弹窗
			show:function () {
				this.newActive = true;
				let _this = this;
				setTimeout(function () {
					_this.newTransition = false;
				},50)
			},
			
			//关闭弹窗
			close:function (v) {
				let close = v || true;
				if (close) {
					this.newTransition = true;
					let _this=this;
					setTimeout(function () {
						_this.newActive = false;
						
					},300)
				}
			},
			//取消按钮
			btn_cancel:function(){
				this.close();
		
				//全部置为空
				this.inputInfo={
					nickName:''
				}
			},
			//确定按钮
			btn_confirm:function(){
				
				if(!this.inputInfo.nickName){
					uni.showModal({
					    content: '请填写你的昵称！',
					    showCancel: false
					});
					return;
				}
				
				var txtLength = this.strLenght(this.inputInfo.nickName);
				
				if(txtLength < 4 || txtLength > 32){
					uni.showModal({
					    content: '昵称长度必须在中文[2-16]英文[4-32]之间！',
					    showCancel: false
					});
					return;
				}
		
				this.close();
				
				//调用请求
				this.$emit('nickNameConfirmCallback',{
					inputInfo:this.inputInfo
				});
			},
			strLenght(str){
				var len = 0;
				for (var i=0; i<str.length; i++) { 
					 var c = str.charCodeAt(i); 
					//单字节加1 
					 if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f))    { 
						len++; 
					 }else { 
						len+=2; 
					 } 
				} 
				return len;
			}
		},
	}
</script>

<style lang="scss">
	popup-layout-wrap{
		position: absolute;
	}
	.popup-layout-wrap{
		position: fixed;
		z-index: 9999;
		left: 0;
		right: 0;
		top: 0;
		bottom: 0;
		//height: 100%;
		width: 100%;
		display: flex;
		flex-flow: row nowrap;
		justify-content: center;
		align-items: center;
		display: none;
		&.popup-layout-active{
			display: flex;
		}
		&.popup-layout-bottom{
			align-items: flex-end;
			.popup-layout-transition-slider{
				transform: translate3d(0,100%,0);
			}
		}
		.popup-layout-content{
			background-color: #fff;
			z-index: 2;
			//height: 100%;
			width: 100%;
			display: flex;
			flex-flow: row nowrap;
			justify-content: center;
			align-items: center;
			transform: translate3d(0,0,0) scale(1);
			opacity: 1;
			transition: transform .3s ease-in-out,opacity .3s ease-in-out;
			&.popup-layout-transition-fade{
				transform: translate3d(0,0,0) scale(0.3);
				opacity: 0;
			}
		}
		.popup-layout-mask{
			position: absolute;
			transition:all 1s ;
			z-index: 1;
			left: 0;
			right: 0;
			top: 0;
			bottom: 0;
			height: 100%;
			width: 100%;
			background-color: rgba(#000, 0.6);
		}
	}
	/***省市区选择器***/
	.link-address-wrap{
		height: 100%;
		width: 100%;
		background: #fff;
	}
	.link-address-wrap .link-adress-content{
		margin:8px 10px 10px 10px;
		
		.head-wrap{
			padding-bottom: 5px;
			display: flex;
			flex-direction: row;
			position: relative;	
			.cancel{
				color: #999;
			}
			.confirm{
				position: absolute;
				right: 0px;
			}
		}
		.head-selected{
			text-align: center;
			color: #ccc;
			font-size: 30upx;
		}
		.bottom-input{
			text-align: left;
			color: #ffaa00;
			font-size: 30upx;
		}
		.operation-wrap{
			.operation-container{
				display: flex;
				flex-direction: row;
				flex-wrap: wrap;
				.operation-content{
					display: flex;
					flex-direction: column;
					width: 100%;
					.operation-info{
						//scroll-view区域的固定高度
						height: 410rpx;
						font-size: 35upx;	
						
						.operation-info-txt{
							padding: 10px 6px;
							overflow: hidden;
							text-overflow:ellipsis;
							white-space: nowrap;
							border-bottom: 1px solid #b4b4b4;

						}
						
						.operation-info-box{
							/* display: flex; */
							-webkit-box-orient: horizontal;
							-webkit-box-direction: normal;
							-webkit-flex-direction: row;
							flex-direction: row;
							//兼容ios，微信小程序
							position: relative;
							
							.operation-info-box-text{
								font-size: 30upx;
							}
						}
					}
				}
			}
		}
	}
</style>
