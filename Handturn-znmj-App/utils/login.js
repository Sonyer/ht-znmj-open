import store from './store.js';
import interfaces from './interfaces.js';
import https from './https.js';

var checkLogin = async function(fromUrl,toUrl,refuseUrl,authUrl,tokenVerify,authFinishedToUrl) {
	var result = false;  //不强制校验的可不需要返回
	
	//获取同步获取当前storeage的相关信息
	let userInfo = store.getUserStore();

	authUrl = authUrl+'?refuseUrl='+escape(refuseUrl)+'&toUrl='+escape(authFinishedToUrl);
	
	if(typeof(userInfo) ==="undefined"  || typeof(userInfo.isAuth) ==="undefined" 
	   //|| typeof(userInfo.isAuthPhoneNumber) ==="undefined" 
	   || typeof(userInfo.token) ==="undefined"  
	   || userInfo.isAuth === '0' 
	   //|| userInfo.isAuthPhoneNumber=== '0'
	   || userInfo.token=== ''){  //未授权
	
	   uni.navigateTo({
			//url:'pages/auth/login'
			url:authUrl,
			complete(res){
				
			},
			fail(e){
				console.log(e);
			}
	   });	
	   result = false;
	   
	}else{
	
	   if(tokenVerify){
		  
		await tokenVerifyRequest(userInfo.accountCode,userInfo.token,toUrl,authUrl).then(val => {
		    result = val;
		});
		
	   }else{
		   if(toUrl === null || toUrl === ''){
				result = true;
		   }else{
			   //已授权
			   uni.navigateTo({    
					//url:'pages/tabBar/home/home.vue'
					url:toUrl,
					complete(res){
						
					},
					fail(e){
						console.log(e);
					}
			   });	
			  result = false;
		   }
	   }
	} 
	
	return result;
};

 async function  tokenVerifyRequest(accountCode,token,toUrl,authUrl){
	
	//校验token是否存在
	return await new Promise((resolve,reject) => {
		let result = false;
		 https({ 
			url: interfaces.tokenVerify,  //POST json传输必须未转字符串
			data: {'accountCode':accountCode,'token':token},
			method: 'POST',
			header: {
				'content-type': 'application/json' 
			},
			success: ((res) => { 			
				if(res.data == true){
					if(toUrl === null || toUrl === ''){
						result = true;
						resolve(result);						
					}else{						
						//已授权
						uni.navigateTo({    
							url:toUrl,
							complete(res){
								
							},
							fail(e){
								console.log(e);
							}
						});	
						result = false;
						resolve(result);
					}
				}else{			
					uni.navigateTo({
						//url:'pages/auth/login'
						url:authUrl,
						complete(res){
							
						},
						fail(e){
							console.log(e);
						}
					});	
					result = false;
					resolve(result);
				}
			})
		 });
	});
}
 
exports.checkLogin = checkLogin;
