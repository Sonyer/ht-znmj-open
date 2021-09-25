<template>
	<view>
		<view class="swiper">
			<view class="swiper-box" >
				<swiper circular="true" @change="visitorChange">
					<checkbox-group>
						<swiper-item v-for="(visitorData,index) in meVisitorData.visitorDatas" :key="index">
							<view class="visitor-info-box">
								<view class="visitor-info-img">
									<image :src="visitorData.faceImgRequest"></image>
								</view>
								<view class="visitor-info-content">
									<view class="visitor-info-content-row">
										<view class="visitor-info-content-row-label"> 
											真实姓名:
										</view>
										<view class="visitor-info-content-row-text">
											{{visitorData.idCardName}}
										</view>
									</view>
									<view class="visitor-info-content-row">
										<view class="visitor-info-content-row-label"> 
											身份证号:
										</view>
										<view class="visitor-info-content-row-text">
											{{visitorData.idCard}}
										</view>
									</view>
									<view class="visitor-info-content-row">
										<view class="visitor-info-content-row-label"> 
											手机号码:
										</view>
										<view class="visitor-info-content-row-text">
											{{visitorData.phoneNumber}}
										</view>
									</view>
									<view class="visitor-info-content-auth">
										<view class="visitor-info-content-auth-label">
											审核状态:
										</view>
										<view class="visitor-info-content-auth-text">
											{{visitorData.auditStatus}}
										</view>
									</view>
								</view>
							</view>
							<view class="visitor-info-check">
								<radio :value="visitorData.id" :checked="checkVisitorId===visitorData.id" v-bind:id="index" @tap="checkVisitorClick" color="#FFCC33" style="fonttransform:scale(1);" />
							</view>
						</swiper-item>
						<!--新增-->
						<swiper-item>
							<view class="visitor-info-box-none">
								新增
							</view>
							<view class="visitor-info-check">
								<radio value="0" :checked="checkVisitorId===0" @tap="checkNewVisitorClick" color="#FFCC33" style="fonttransform:scale(1);" />
							</view>
						</swiper-item>
					</checkbox-group>
				</swiper>
				<view class="indicator">
					<view :class="{'on':meVisitorData.currentIndex >= index}" class="dots" v-for="(visitorData,index) in meVisitorData.visitorDatas" :key="index"></view>
				</view>
			</view>
		</view>
		<view class="edit-content">  <!-- 常用功能-->
			<view class="edit-content-tittle">  <!-- 标题-->
				{{editTittle}}
			</view>
			<view  class="edit-content-area">
				<form @submit="visitorApplySubmitPre">
					<view class="edit-content-area-info">
						<view class="edit-content-area-info-horizontal">
							<view class="edit-content-area-info-uploadImg">
								<view class="edit-content-area-info-lable">
									人脸图片:
								</view>
								<view class="edit-content-area-info-img" v-if="newVisitorData.auditStatus == '未审核' || newVisitorData.auditStatus == '审核失败' || newVisitorData.auditStatus == '' || newVisitorData.auditStatus == null">
									<image :src="newVisitorData.faceImgRequest" mode="aspectFill" @tap="uploadImg" v-if="newVisitorData.faceImgRequest != null && newVisitorData.faceImgRequest != ''"></image>
									<image src="../../static/img/common/empty-img.png" mode="aspectFill" @tap="uploadImg" v-else=""></image>
								</view>
								<view class="edit-content-area-info-img" v-else-if="newVisitorData.faceImgRequest != null && newVisitorData.faceImgRequest != ''">
									<image :src="newVisitorData.faceImgRequest" mode="aspectFill" @tap="viewImage"></image>
								</view>
								<view class="edit-content-area-info-img" v-else>
									 <image src="../../static/img/common/empty-img.png" mode="aspectFill" @tap="uploadImg"></image>
								</view>
							</view>
							<view class="edit-content-area-info-vertical">
								<view class="edit-content-area-info">
									<view class="edit-content-area-info-lable">
										真实姓名:
									</view>
									<input type="text" name="idCardName" :value="newVisitorData.idCardName" v-if="newVisitorData.auditStatus != '已审核' && newVisitorData.auditStatus != '审核中'"/>
									<text class="edit-content-area-info-text" v-else>
										{{newVisitorData.idCardName}}
									</text>
								</view>
								<view class="edit-content-area-info">
									<view class="edit-content-area-info-lable">
										身份证号:
									</view>
									<input type="digit" name="idCard" :value="newVisitorData.idCard" v-if="newVisitorData.auditStatus != '已审核' && newVisitorData.auditStatus != '审核中'"/>
									<text class="edit-content-area-info-text" v-else>
										{{newVisitorData.idCard}}
									</text>
								</view>
							</view>
						</view>
					</view>
					<view class="edit-content-area-info-vertical">
						<view class="edit-content-area-info">
							<view class="edit-content-area-info-lable">
								公司岗位:
							</view>
							<input type="digit" name="positionName" :value="newVisitorData.positionName"  v-if="newVisitorData.auditStatus != '已审核' && newVisitorData.auditStatus != '审核中'"/>
							<text class="edit-content-area-info-text" v-else>
								{{newVisitorData.positionName}}
							</text>
						</view>
						<view class="edit-content-area-info">
							<view class="edit-content-area-info-lable">
								申请备注:
							</view>
							<input type="text" name="remark" :value="newVisitorData.remark"  v-if="newVisitorData.auditStatus != '已审核' && newVisitorData.auditStatus != '审核中'"/>
							<text class="edit-content-area-info-text" v-else>
								{{newVisitorData.remark}}
							</text>
						</view>
						<view class="edit-content-area-info">
							<view class="edit-content-area-info-lable">
								手机号码:
							</view>
							<input type="text" name="phoneNumber" v-model="newVisitorData.phoneNumber" v-if="newVisitorData.auditStatus != '已审核' && newVisitorData.auditStatus != '审核中'"/>
							<text class="edit-content-area-info-text" v-else>
								{{newVisitorData.phoneNumber}}
							</text>
						</view>
						<view class="edit-content-area-info"  v-if="newVisitorData.auditStatus != '已审核' && newVisitorData.auditStatus != '审核中'">
							<view class="edit-content-area-info-lable">
								验证码:
							</view>
							<view class="edit-content-area-info-input-horizontal">
								<input class="edit-content-area-info-input-text" type="number" name="validateCode"/>
								<button v-if="verifyShow" @click="getValidateCode">获取</button>
								<button v-else style="background-color: #C0C0C0;">{{numTime+'秒'}}</button>
							</view>
						</view>
						<view class="edit-content-area-info"  v-if="newVisitorData.auditStatus != '未审核' && newVisitorData.auditStatus != '' && newVisitorData.auditStatus != null">
							<view class="edit-content-area-info-lable">
								审核状态:
							</view>
							<text class="edit-content-area-info-text">
								{{newVisitorData.auditStatus}}
							</text>
						</view>
					</view>
					<view class="edit-content-area-option" v-if="newVisitorData.auditStatus == '未审核' || newVisitorData.auditStatus == '审核失败' || newVisitorData.auditStatus == '' || newVisitorData.auditStatus == null">
						<button formType="submit" type="primary">提交审核</button> 
					</view>
				</form>
			</view>
		</view>
	</view>
</template>

<script>
	export default{
		components:{
		},
		data() {
			return {
				wxNotifyTemplatIds:[],
				
				editTittle:"申请编辑",
				requestData:{
					orgCode:'',
					ocCode:'',
					authAreaCode:'',
					orgName:'',
					ocName:'',
					authAreaName:''
				},
				checkVisitorIndex: -1,
				checkVisitorId: 0,
				
				verifyShow: true,
				numTime: 0,
				
				newVisitorData:{
					id:0,
					idCardName:"",
					idCard:"",
					phoneNumber:"",
					faceImgRequest:"",
					positionName:"",
					auditStatus:"",
					remark:""
				},
				
				meVisitorData:{
					currentIndex: 0,
					visitorDatas:[]
				},
			}
		},
		methods:{
			initData(){
				var tittle = this.requestData.ocName+"-访客申请"
				uni.setNavigationBarTitle({
				　　title:tittle
				});
				this.editTittle = this.requestData.authAreaName+"-门禁申请";
				
				//调用查询接口
				this.queryData();
			},
			queryData(){
				this.checkLogin().then(val => {
					if(val === true){
						this.https({
							url: this.interfaces.initVisitorApply,  //POST json传输必须未转字符串
							method: 'POST',
							data: {
								orgCode:this.requestData.orgCode,
								ocCode: this.requestData.ocCode,
								authAreaCode: this.requestData.authAreaCode
							},
							header: {
								'content-type': 'application/json'
							},
							success: ((res) => { 
								this.wxNotifyTemplatIds = res.data.wxNotifyTemplatIds;
								this.meVisitorData.visitorDatas = res.data.pageDatas;
							})
						}); 
					}
				});
			},
			visitorApplySubmitPre(e){
				var evenData = e;
				var that = this;
				
				uni.showLoading({title:"加载中..."})
				if(that.wxNotifyTemplatIds == null || that.wxNotifyTemplatIds =='' || typeof(that.wxNotifyTemplatIds) == 'undefined'){
					that.visitorApplySubmit(evenData);
				}else{
					// 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
					uni.requestSubscribeMessage({
					   tmplIds: that.wxNotifyTemplatIds,
					   success (res) {
						   uni.hideLoading();
						   console.log(res)
						   that.visitorApplySubmit(evenData);
					   },
					   fail:(res) => {
						  uni.hideLoading();
						  console.log(res)
						   that.visitorApplySubmit(evenData);
					   }
					});
				 }
			},
			visitorApplySubmit(e){
				if(this.newVisitorData.faceImgRequest == '' || typeof(this.newVisitorData.faceImgRequest) == 'undefined'){
					uni.showModal({
					    content: '人脸图片未上传！',
					    showCancel: false
					});
					return;
				}
				
				if(e.detail.value.idCardName == '' || typeof(e.detail.value.idCardName) == 'undefined'
					||e.detail.value.idCard == ''  || typeof(e.detail.value.idCard) == 'undefined'
					||e.detail.value.positionName == ''  || typeof(e.detail.value.positionName) == 'undefined'){
					uni.showModal({
					    content: '真实姓名、身份证号、职位名称必须填写！',
					    showCancel: false
					});
					return;
				}
				
				if(!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(e.detail.value.idCard))){
				  uni.showModal({
				      content: '身份证号格式校验失败！',
				      showCancel: false
				  });
				  return;
				}
				
				if(e.detail.value.phoneNumber == ''|| typeof(e.detail.value.phoneNumber) == 'undefined'){
					uni.showModal({
					    content: '必须填写手机号码！',
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
					orgCode:this.requestData.orgCode,
					ocCode:this.requestData.ocCode,
					authAreaCode:this.requestData.authAreaCode,
					id: this.newVisitorData.id,
					idCardName:typeof(e.detail.value.idCardName)=='undefined'?'':e.detail.value.idCardName,
					idCard:typeof(e.detail.value.idCard)=='undefined'?'':e.detail.value.idCard,
					faceImgRequest:this.newVisitorData.faceImgRequest,
					phoneNumber: typeof(e.detail.value.phoneNumber) =='undefined'?'':e.detail.value.phoneNumber,
					positionName:typeof(e.detail.value.positionName) =='undefined'?'':e.detail.value.positionName,
					remark:typeof(e.detail.value.remark) =='undefined'?'':e.detail.value.remark,
					validateCode:typeof(e.detail.value.validateCode) =='undefined'?'':e.detail.value.validateCode,
				};
							
				uni.showModal({
					title: '审核确认',
					content: '确定要提交审核吗？',
					cancelText: '否',
					confirmText: '是',
					success: res => {
						if (res.confirm) {
							this.https({
								url: this.interfaces.submitVisitorApply,  //POST json传输必须未转字符串
								method: 'POST',
								data: submitData,
								header: {
									'content-type': 'application/json'
								},
								success: ((res) => { 
									//是否需要支付
									if(res.data == true){
										uni.showModal({
											content: '提交审核完成！',
											showCancel: false
										});
										
										this.initData();
										
										this.newVisitorData = {};
									}
								})
							}); 
						}
					},
				});
				
			},
			visitorChange(event){
				this.meVisitorData.currentIndex = event.detail.current;
			},
			checkVisitorClick(e){
				var index = e.currentTarget.id;
				const visitorData = this.meVisitorData.visitorDatas[index];
				
				this.checkVisitorIndex = index;
				this.checkVisitorId = visitorData.id; 
				
				this.checkVisitorAction(visitorData);
			},
			checkNewVisitorClick(e){
				this.checkVisitorIndex = this.meVisitorData.visitorDatas.length;
				this.checkVisitorId = 0; 
				
				this.checkVisitorAction({});
			},
			checkVisitorAction(visitorData){
				this.newVisitorData = visitorData;
			},
			getValidateCode(){
				if(this.newVisitorData.phoneNumber == null || this.newVisitorData.phoneNumber == '' || typeof (this.newVisitorData.phoneNumber) == 'undefined'){
					uni.showModal({
					    content: '必须填写手机号码！',
					    showCancel: false
					});
					return;
				}else{
					if(!(/^1[1|2|3|4|5|6|7|8|9|0][0-9]\d{4,8}$/.test(this.newVisitorData.phoneNumber))){
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
						phoneNumber:this.newVisitorData.phoneNumber
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
			uploadImg(){
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
				uni.showLoading({title:"加载中..."})
				// 上传图片
				// 做成一个上传对象
				uni.uploadFile({
				    // 需要上传的地址
				    url:_this.interfaces.uploadVisitorImg,
				    //filePath  需要上传的文件
				    filePath: file,
					name:'file',
					formData:formData,
					fileType:'image',
				    success(res1) {
						uni.hideLoading();
						const result = JSON.parse(res1.data);
						_this.newVisitorData.faceImgRequest = result.data;
				    },
					fail(res1) {
						uni.hideLoading();
						uni.showModal({
						    content: '上传图片不能超过10M！',
						    showCancel: false
						});
						return;
					}
				}); 
			},
			viewImage(){
				let imgurl = '';
			
				imgurl = this.newVisitorData.faceImgRequest;
				
				uni.previewImage({
					urls: [imgurl],
					loop: "true",
					current: imgurl
				});
			},
			async checkLogin(){
				//获取同步获取当前storeage的相关信息
				var navigateToUrl = '/pages/visitorApply/visitorApply?orgCode='+this.requestData.orgCode+'&ocCode='+this.requestData.ocCode+'&authAreaCode='+this.requestData.authAreaCode
					+'&orgName='+this.requestData.orgName+'&ocName='+this.requestData.ocName+'&authAreaName='+this.requestData.authAreaName;
				let fromUrl = navigateToUrl;
				let toUrl = ""; //无需跳转其他页面
				let refuseUrl = '/pages/tabBar/home/home';
				let authFinishedToUrl = navigateToUrl; //授权完成后跳转页面
				let authUrl = "/pages/auth/login";
				let tokenVerify = true;
				return await this.login.checkLogin(fromUrl,toUrl,refuseUrl,authUrl,tokenVerify,authFinishedToUrl);
			},
		},
		onLoad(options) {
			this.requestData.orgCode = options.orgCode;
			this.requestData.ocCode = options.ocCode;
			this.requestData.authAreaCode = options.authAreaCode;
			this.requestData.orgName = options.orgName;
			this.requestData.ocName = options.ocName;
			this.requestData.authAreaName = options.authAreaName;
			
			this.initData();
		},
		onPullDownRefresh(){
			uni.showLoading({title:"加载中..."})
			setTimeout(() => {
				this.initData();
				uni.stopPullDownRefresh();
			},1000)
			
		}
	}
	
</script>

<style lang="scss" scoped>
	.swiper {
		width: 100%;
		margin-top: 10upx;
		/*  #ifdef  MP  */
		/* padding-top: var(--status-bar-height); */
		/*  #endif  */
		display: flex;
		justify-content: center;
	
		.swiper-box {
			width: 95%;
			overflow: hidden;
			border-radius: 15upx;
			box-shadow: 0upx 8upx 25upx rgba(0, 0, 0, 0.2);
			//兼容ios，微信小程序
			position: relative;
			z-index: 1;
	
			swiper {
				width: 100%;
				height: 250upx;
	
				swiper-item {
					image {
						width: 100%;
						
					}
				}
			}
	
			.indicator {
				position: absolute;
				bottom: 20upx;
				left: 20upx;
				background-color: rgba(255, 255, 255, 0.4);
				width: 150upx;
				height: 5upx;
				border-radius: 3upx;
				overflow: hidden;
				display: flex;
	
				.dots {
					width: 0upx;
					background-color: rgba(255, 255, 255, 1);
					transition: all 0.3s ease-out;
	
					&.on {
						width: (100%/3);
					}
				}
			}
		}
		
		.visitor-info-box{
			width: 100%;
			height: 240upx;
			justify-content: space-between;
			display: flex;
			-webkit-box-orient: horizontal;
			-webkit-box-direction: normal;
			-webkit-flex-direction: row;
			flex-direction: row;
			
			.visitor-info-img{
				width: 200upx;
				display:flex;
				position: relative;
				
				image{
					width: 100%;
					height: 100%;
					display: block;
					border: none;
					border-radius: 5%;
					position: relative;
				} 
			}
			
			.visitor-info-content{
				width: 75%;
				margin-right: 10upx;
				margin-left: 10upx;
				border-radius: 5%;
				background-color: #c6d1ff;
				font-size: 35upx;
				
				.visitor-info-content-row{
					margin: 15upx;
					width: 100%;
					display: flex;
					-webkit-box-orient: horizontal;
					-webkit-box-direction: normal;
					-webkit-flex-direction: row;
					flex-direction: row;
					color: #00aa00;
					
					.visitor-info-content-row-label{
						width: 28%;
						font-size: 30upx; 
					}
					.visitor-info-content-row-text{
						align-items: flex-start;
						font-size: 30upx;
					}
				}
				
				.visitor-info-content-auth{
					margin: 15upx;
					width: 100%;
					display: flex;
					-webkit-box-orient: horizontal;
					-webkit-box-direction: normal;
					-webkit-flex-direction: row;
					flex-direction: row;
					color: #00aa00;
					
					.visitor-info-content-auth-label{
						width: 28%;
						font-size: 30upx; 
					}
					.visitor-info-content-auth-text{
						align-items: flex-start;
						font-size: 30upx;
					}
				}
			}
		}
		
		.visitor-info-box-none{
			position: absolute;
			top: 50%;
			left: 50%;
			/* translate()函数是css3的新特性.在不知道自身宽高的情况下，可以利用它来进行水平垂直居中 */
			transform: translate(-50%,-50%);
			color: #C0C0C0;
			font-size: 50upx;

		}
		
		.visitor-info-check{
			width: 40upx;
			height: 40upx;
			display: flex;
			right: 15upx;
			bottom: 15upx;
			font-size: 28upx;
			/* color: #ffff00; */
			z-index: 1;
			position: absolute;
		}
	}
	
	
	
	.edit-content{
		padding-top: 20upx;
		padding-left: 20upx;
		display: flex;
		-webkit-box-orient: vertical;
		-webkit-box-direction: normal;
		-webkit-flex-direction: column;
		flex-direction: column;
		
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
			border-left-width: 0px;
		}
		
		.edit-content-tittle{
			padding-left: 20upx;
			font-size: 35upx;
			font-weight: 800;
			color: #707070;
		}
		
		.edit-content-area{
			height: 100%;
			
			.edit-content-area-info{
				margin-top: 10upx;
				/* margin-right: 40upx; */
				margin-bottom: 10upx;
				/* width: 100%; */
				display: flex;
				-webkit-box-orient: vertical;
				-webkit-box-direction: normal;
				-webkit-flex-direction: column;
				flex-direction: column;
				justify-content: center;
				position: relative;
				display: flex;
				
				.edit-content-area-info-horizontal{
					display: flex;
					-webkit-box-orient: horizontal;
					-webkit-box-direction: normal;
					-webkit-flex-direction: row;
					flex-direction: row;
				}
				
				.edit-content-area-info-uploadImg{
					margin-top: 10upx;
					margin-right: 40upx;
					margin-bottom: 10upx;
					margin-left: 40upx;
					-webkit-box-orient: vertical;
					-webkit-box-direction: normal;
					-webkit-flex-direction: column;
					flex-direction: column;
					justify-content: center;
					position: relative;
					display: flex;
						
					.edit-content-area-info-img{
						display: flex;
						position: relative;
						
						image{
							margin-top: 10upx;
							margin-right: 10upx;
							margin-bottom: 10upx;
							margin-left: 10upx;
							height: 180upx;
							width: 180upx;
							border: 0.5upx solid #ffaa00;
							position: relative;
						}
						
						.icon {
							width: 50upx;
							height: 50upx;
							display: flex;
							top: -10upx;
							right: -25upx;
							font-size: 30upx;
							color: #ffaa00;
							z-index: 1;
							position: absolute;
						}
					}
					
				}
				
				.edit-content-area-info-lable{
					color: #ffaa00;
					font-size: 30upx;
				}
				
				input {
					font-size: 30upx;
				}
				
				.edit-content-area-info-text{
					font-size: 30upx;
					height: 50upx;
					margin-top: 0upx;
					margin-right: 20upx;
					margin-left: 20upx;
					margin-bottom: 20upx;
					border-color: #878787;
					border-style: solid;
					border-top-width: 0px;
					border-right-width: 0px;
					border-bottom-width: 1px;
					border-left-width: 0px;
				}
				
				.edit-content-area-info-input-horizontal{
					width: 75%;
					-webkit-box-orient: horizontal;
					-webkit-box-direction: normal;
					-webkit-flex-direction: row;
					flex-direction: row;
					justify-content: center;
					position: relative;
					display: flex;
					
					.edit-content-area-info-input-text{
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
			
			.edit-content-area-info-option{
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
		}
	}
</style>
