<template>
	<view>
		<view class="user-info">   <!-- 用户信息 -->
			<view class="user-info-left">
				<view class="user-info-left-avatar">
					<image :src="meData.avatarRequest" @click="uploadImgAvatar"/>
				</view>
			</view>
			<view class="user-info-right">
				<view class="user-info-right-left">
					<view class="user-info-right-left-row1" @click="nickNameOption">
						{{meData.nickName}}
					</view>
					<view class="user-info-right-left-row2">
						
					</view>
				</view>
			</view>
		</view>
		
		<view class="often-function">  <!-- 常用功能-->
			<view class="often-function-tittle">  <!-- 标题-->
				常用
			</view>
			
			<view  class="often-function-area">  <!-- 功能区 -->
				<view class="often-function-area-single" @tap="handUserNotice"> 
					<view class="icon iconfont">
						&#xe629;
					</view>
					<view class="often-function-area-single-dot" v-if="userNotify == true">
					 
					</view>
					<view class="sigle-text">消息通知</view>
				</view>
				<view class="often-function-area-single" @tap="handUserCommand">
					<view class="icon iconfont">
						&#xe691;
					</view>
					<view class="sigle-text">管理口令</view>
				</view>
				<view class="often-function-area-single" @tap="handUserVisitorAudit">
					<view class="icon iconfont">
						&#xe632;
					</view>
					<view class="often-function-area-single-dot" v-if="userVisitorAudit == true">
					 
					</view>
					<view class="sigle-text">访客审核</view>
				</view>
				<!-- <view class="often-function-area-single" @tap="handPlatRule"> 
					<view class="icon iconfont">
						&#xe654;
					</view>
					<view class="sigle-text">说明</view>
				</view> -->
				<view class="often-function-area-single" @tap="handUserNoticeSet">
					<view class="icon iconfont">
						&#xe652;
					</view>
				
					<view class="sigle-text">微信订阅</view> 
				</view>
				<view class="often-function-area-single" @tap="authSystemSet">
					<view class="icon iconfont">
						&#xe61c;
					</view>
				
					<view class="sigle-text">授权设置</view>
				</view>
			</view>
		</view>
		
		<meNickNameSubmit
			ref="meNickNameSubmit"
			:height="meNickNameSubmitHeight"
			@nickNameConfirmCallback="nickNameConfirmCallback()"
		>
		</meNickNameSubmit>
	</view>
</template>

<script>
	import meNickNameSubmit from '../../components/meNickNameSubmit/meNickNameSubmit.vue'
	
	export default{
		components:{
			meNickNameSubmit
		},
		data() {
			return {
				meData:{
					accountCode:"",
					nickName:"",
					gender:"",
					avatarRequest:"",
					phoneNumber:"",
					vipLevel:"",
					certificationType:"",
					
					persionUserPayBusNo:"",
					clientUserPayBusNo:"",
					persionUserCerStatus:"",
					clientUserCerStatus:"",
					
					persionUserCerPayStatus:"",
					clientUserCerPayStatus:"",
						
					viewCount:"",
					admireCount:"",
					commentCount:"",
					shareCount:"",
					followCount:"",
					
					copyright:"",
					aboutUs:"",
					instDocRequest:"",
					
					wxNotifyTemplatIds:[]
				},
				nickNameTxt:'',
				meNickNameSubmitHeight:'250rpx',
				
				userNotify: false,
				userVisitorAudit: false,
				
				//是否允许虚拟支付
				isAllowVpayment : true,
			}
		},
		methods:{
			//重置系统端位
			resetSystemInfo: function(){
			    var that = this;
			    wx.getSystemInfo({
			      success:function(res){
					  //res.platform == "devtools" --电脑端  res.platform == "ios" -- IOS端    res.platform == "android" --Andorid端
					 that.systemInfo = res;
					 if(res.platform == "ios"){
						 that.isAllowVpayment = false;
					 }
			      },
				  fail:function(res){
					  that.isAllowVpayment = true;
				  }
			    })
			},
			initData(){
				this.checkLogin().then(val => {
					if(val === true){
						this.https({
							url: this.interfaces.initMe,  //POST json传输必须未转字符串
							method: 'POST',
							data: {
							},
							header: {
								'content-type': 'application/json'
							},
							success: ((res) => { 
								this.meData = res.data;
							})
						}); 
					}
				});
			},
			async checkLogin(){
				//获取同步获取当前storeage的相关信息
				let fromUrl = "/pages/tabBar/user/user";
				let toUrl = ""; 
				let authFinishedToUrl = '/pages/tabBar/user/user'; //授权完成后跳转页面
				let refuseUrl = '/pages/tabBar/user/user';
				let authUrl = "/pages/auth/login";
				let tokenVerify = true;
				return await this.login.checkLogin(fromUrl,toUrl,refuseUrl,authUrl,tokenVerify,authFinishedToUrl);
			},
			nickNameOption(){
				this.$refs.meNickNameSubmit.show();
			},
			nickNameConfirmCallback(data){
				let ads=data.inputInfo;
				this.nickNameTxt = ads.nickName;
				
				if(this.nickNameTxt === ''){
					uni.showModal({
					    content: '请填写你的昵称！',
					    showCancel: false
					});
					return;
				}
							
				this.https({
					url: this.interfaces.updateNickName,  //POST json传输必须未转字符串
					method: 'POST',
					data: {
						'nickName':this.nickNameTxt
					},
					header: {
						'content-type': 'application/json'
					},
					success: ((res) => { 
						this.meData.nickName = this.nickNameTxt;
					})
				}); 
			},
			uploadImgAvatar(){
				//获取用户信息
				let userInfo = this.store.getUserStore();
				
				const formData = {'accountCode':userInfo.accountCode,'token':userInfo.token};
				
				const _this = this;
				uni.chooseImage({
					sourceType:['camera','album'],
			        count: 1,
			        sizeType:['copressed'],
			        success(res) {
			
			            var imgFiles = res.tempFilePaths;
			           
					    let arr=[]
						for (let k in imgFiles) {
							arr[k] =_this.uploadFile(imgFiles[k],formData);
						} 
						
			        }
			     })
			},
			uploadFile(file,formData){
				const _this = this;
				// 上传图片
				// 做成一个上传对象
				uni.uploadFile({
				    // 需要上传的地址
				    url:_this.interfaces.uploadMeAvatar,
				    //filePath  需要上传的文件
				    filePath: file,
					name:'file',
					formData:formData,
					fileType:'image',
				    success(res1) {
						const result = JSON.parse(res1.data);
						if(result.code && result.code != 200){
							uni.showModal({
								content:result.message
							})
							return;
						}
						_this.meData.avatarRequest =  result.data;
				    }
				}); 
			},
			viewImage(e){
				uni.previewImage({
					urls: this.imageList,
					loop: "true",
					current: this.imageListTemp[e.currentTarget.id]
				});
			},
		
			handUserNotice(){
				//更新通知标记
				this.store.notifyData.VISITOR_APPLY_NOTIFY = '';
				this.store.notifyData.VISITOR_AUDIT_NOTIFY = '';
				
				const notifyData = this.store.notifyData;
				this.store.setNotifyStore(notifyData);
				this.userNotify = false;
				
				uni.navigateTo({
					url:'../../user/userNotice',
					success: res => {
						
					},
					fail: () => {},
					complete: () => {
						
					}
				});	
			},
			handUserNoticeSet(){
				uni.navigateTo({
					url:'../../user/userNoticeSet',
					success: res => {
						
					},
					fail: () => {},
					complete: () => {
						
					}
				});	
			},
			handUserCommand(){
				uni.navigateTo({
					url:'../../user/userCommand',
					success: res => {
						
					},
					fail: () => {},
					complete: () => {
						
					}
				});	
			},
			handUserVisitorAudit(){
				//更新通知标记
				this.store.notifyData.VISITOR_APPLY_NOTIFY = '';
				
				const notifyData = this.store.notifyData;
				this.store.setNotifyStore(notifyData);
				this.userVisitorAudit = false;
				
				uni.navigateTo({
					url:'../../user/userVisitorAudit',
					success: res => {
						
					},
					fail: () => {},
					complete: () => {
						
					}
				});	
			},
			authSystemSet(){
				//这里只做消息权限是否开启控制
				uni.getSetting({
					withSubscriptions: true, //是否同时获取用户订阅消息的订阅状态，默认不获取
					success: (res)=> {						
						uni.openSetting({ // 打开设置页
						   success(res) {
							 console.log(res.authSetting)
						   }
						});
					}
				})
			},
			
			handPlatRule(){
				uni.downloadFile({
				  url: this.meData.instDocRequest, 
				  success: function (res) {
				    var filePath = res.tempFilePath;
				    uni.openDocument({
				      filePath: filePath,
				      success: function (res2) {
				        console.log('打开文档成功');
				      },
					  fail: function (res2) {
						console.log(res2)
					  }
				    });
				  },
				  fail: function (res) {
					console.log(res)
				  }
				}); 
				
			},
			notifyInit(){
				if(typeof(this.store) == 'undefined' || typeof(this.store.notifyData) == 'undefined'){
					this.userNotify = false;
					this.userVisitorAudit = false;
				}else{
					
					if((this.store.notifyData.VISITOR_APPLY_NOTIFY != null && typeof(this.store.notifyData.VISITOR_APPLY_NOTIFY) !="undefined" && this.store.notifyData.VISITOR_APPLY_NOTIFY !="")
						|| (this.store.notifyData.VISITOR_AUDIT_NOTIFY != null && typeof(this.store.notifyData.VISITOR_AUDIT_NOTIFY) !="undefined" && this.store.notifyData.VISITOR_AUDIT_NOTIFY !="")
						){
						this.userNotify = true;	
					}
					
					this.userVisitorAudit = this.store.notifyData.VISITOR_APPLY_NOTIFY == '1';
				}
			}
		},
		computed:{
			
		},
		onTabItemTap(){
			this.notifyInit();
		},
		onLoad() {	
			uni.showLoading({title:"加载中..."})
			this.notifyInit();
			this.initData();
			
			//系统类型校验
			this.resetSystemInfo();
		},
		onPullDownRefresh(){
			uni.showLoading({title:"加载中..."})
			
			this.notifyInit();
			
			setTimeout(() => {
				this.initData();
				uni.stopPullDownRefresh();
			},1000)
		},
	}
	
</script>

<style lang="scss" scoped>
	.user-info{
		/*  #ifdef  MP  */
		/* padding-top: var(--status-bar-height); */
		/*  #endif  */
		display: flex;
		justify-content: center;
		-webkit-box-orient: horizontal;
		-webkit-box-direction: normal;
		-webkit-flex-direction: row;
		flex-direction: row;
		//兼容ios，微信小程序
		position: relative;
		
		.user-info-left{
			display: flex;
			-webkit-box-align: center;
			-webkit-align-items: center;
			align-items: center;
			padding: 20upx;
			//兼容ios，微信小程序
			position: relative;
			
			.user-info-left-avatar{
				width: 140upx;
				height: 140upx;
				display:flex;
				margin-right: 0.5rem;
				position: relative;
				
				.icon {
					width: 40upx;
					height: 40upx;
					display: flex;
					right: -1upx;
					bottom: -1upx;
					font-size: 40upx;
					/* color: #ffff00; */
					z-index: 1;
					position: absolute;
				}
				
				image{
					width: 100%;
					height: 100%;
					display: block;
					border: none;
					border-radius: 15%;
					position: relative;
				}	
			}
		}
		
		.user-info-right{
			padding-top: 20upx;
			width: 100%;
			height: 150upx;
			display: flex;
			justify-content: space-between;
			-webkit-box-orient: horizontal;
			-webkit-box-direction: normal;
			-webkit-flex-direction: row;
			flex-direction: row;
			
			.user-info-right-left{
				padding-top: 15upx;
				padding-bottom: 20upx;
				padding-left: 0upx;
				height: 100upx;
				font-size: 45upx;
				font-weight: 800;
				display: flex;		
				-webkit-box-orient: vertical;
				-webkit-box-direction: normal;
				-webkit-flex-direction: column;
				flex-direction: column;
				position: relative;
				
				.user-info-right-left-row1{
					justify-content: space-between;
					width: 100%;
					height: 100upx;
					font-weight: 800;
					display: flex;
					-webkit-box-orient: horizontal;
					-webkit-box-direction: normal;
					-webkit-flex-direction: row;
					flex-direction: row;
					font-size: 45upx;
				}
				/* .user-info-right-left-row2{
					margin-top: 20upx;
					width: 100%;
					height: 75upx;
					display: flex;
					font-size: 28upx;
					-webkit-box-orient: horizontal;
					-webkit-box-direction: normal;
					-webkit-flex-direction: row;
					flex-direction: row;
					align-items: center;
					text-align: center;
					color: #999999;
					
					.column1{
						padding-right: 20upx;
					}
				} */
			}
			
			/* .user-info-right-right{
				width: 38%;
				padding: 20upx;
				height: 75upx;
				font-size: 30upx;
				display: flex;
				-webkit-box-orient: vertical;
				-webkit-box-direction: normal;
				-webkit-flex-direction: column;
				flex-direction: column;
				color: #ffaa00;
				
				.user-info-right-right-row1{
					padding-top: 10upx;
					justify-content: space-between;
					width: 100%;
					height: 75upx;
					font-size: 28upx;
					align-items: center;
					text-align: center;
				}
				
				.user-info-right-right-red{
					padding-top: 10upx;
					justify-content: space-between;
					width: 100%;
					height: 75upx;
					font-size: 28upx;
					align-items: center;
					text-align: center;
					color: #ff0000;
				}
				
				.user-info-right-right-none{
					padding-top: 10upx;
					justify-content: space-between;
					width: 100%;
					height: 75upx;
					font-size: 28upx;
					align-items: center;
					text-align: center;
					color: #adadad;
				}
				.user-info-right-right-greed{
					padding-top: 10upx;
					justify-content: space-between;
					width: 100%;
					height: 75upx;
					font-size: 28upx;
					align-items: center;
					text-align: center;
					color: #55aa00;
				}
			} */
		}
	}
	
	.cer-function{
		padding-top: 20upx;
		padding-left: 20upx;
		display: flex;
		-webkit-box-orient: vertical;
		-webkit-box-direction: normal;
		-webkit-flex-direction: column;
		flex-direction: column;
		
		.cer-function-tittle{
			padding-left: 20upx;
			font-size: 30upx;
			font-weight: 800;
			color: #707070;
		}
		
		.cer-function-area{
			padding: 10upx;
			justify-content: space-between;
			-webkit-box-orient: horizontal;
			-webkit-box-direction: normal;
			-webkit-flex-direction: row;
			flex-direction: row;
			flex-wrap: wrap;
			width: auto;
			height: 80upx;
			display: flex;
			cursor: pointer;
			position: relative;
			
			.cer-function-box{
				margin-left: 40upx;
				margin-right: 40upx;
				display: flex;
				-webkit-box-orient: horizontal;
				-webkit-box-direction: normal;
				-webkit-flex-direction: row;
				flex-direction: row;
				font-size: 40upx;
				font-weight: 400;
				justify-content: flex-end;
				align-items: center;
				text-align: center;
				
			
				.icon {
					padding-top: 0upx;
					padding-bottom: 0upx;
					padding-left: 0upx;
					width: 40upx;
					height: 40upx;
					display: flex;
					justify-content: flex-end;
					align-items: center;
					font-size: 40upx;
					color: #C0C0C0;
				}
				
				
				.user-info-right-right-row1{
					padding-left: 10upx;
					justify-content: space-between;
					font-size: 30upx;
					align-items: center;
					text-align: center;
				}
				
				.user-info-right-right-red{
					padding-left: 10upx;
					justify-content: space-between;
					font-size: 30upx;
					align-items: center;
					text-align: center;
					color: #ff0000;
				}
				
				.user-info-right-right-none{
					padding-left: 10upx;
					justify-content: space-between;
					font-size: 30upx;
					align-items: center;
					text-align: center;
					color: #adadad;
				}
				.user-info-right-right-greed{
					padding-left: 10upx;
					justify-content: space-between;
					font-size: 30upx;
					align-items: center;
					text-align: center;
					color: #55aa00;
				}
			}
		}
	}
	
	.often-function{
		padding-top: 20upx;
		padding-left: 20upx;
		display: flex;
		-webkit-box-orient: vertical;
		-webkit-box-direction: normal;
		-webkit-flex-direction: column;
		flex-direction: column;
		
		.often-function-tittle{
			padding-left: 20upx;
			font-size: 30upx;
			font-weight: 800;
			color: #707070;
		}
		
		.often-function-area{
			height: 100%;
			padding: 10upx;
			-webkit-box-orient: horizontal;
			-webkit-box-direction: normal;
			-webkit-flex-direction: row;
			flex-direction: row;
			flex-wrap: wrap;
			width: auto;
			height: 120upx;
			display: flex;
			cursor: pointer;
			position: relative;
			
			.often-function-area-single{
				padding: 10upx;
				width: 150upx;
				height: 150upx;
				display: flex;
				-webkit-box-orient: vertical;
				-webkit-box-direction: normal;
				-webkit-flex-direction: column;
				flex-direction: column;
				align-items: center;
				text-align: center;
				position: relative; 
				
				.often-function-area-single-dot{
					position: absolute;
					display: flex;
					background-color: #ff5500;
					color: #ff5500;
					font-size: 20upx;
					/* background-color: red; */
					/*height: 24px;改前*/
					min-height: 10px;/*改后新增的代码*/
					min-width:10px;/*改后新增的代码*/
					line-height: 24px;
					right:12px;
					top: 0px;
					text-align: center;
					 -webkit-border-radius: 24px;
					border-radius: 24px;
				}
				
				.icon {
					width: 80upx;
					height: 80upx;
					display: flex;
					justify-content: flex-end;
					align-items: center;
					font-size: 80upx;
					color: #707070;
				}
				
				/* image{
					width: 150upx;
					height: 150upx;
					display: block;
					border: none;
					border-radius: 100%;
					position: relative;
				} */
				
				.sigle-text{
					text-align: center;
					align-items: center;
					font-size: 25upx;
					color: #707070;
				}
				
			}
		}
	}
</style>
