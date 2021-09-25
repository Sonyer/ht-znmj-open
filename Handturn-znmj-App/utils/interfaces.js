 /*const domain = "http://127.0.0.1:8088/sitapi/";   //开发*/
 /* const domain = "https://znmj.handturn.com/sitapi/"; */    //测试 
	/*const domain = "https://linlingo.handturn.com/sitapi/"; */  //生产
	
var domain_even = function () {
	  let version = __wxConfig.envVersion;
	  switch (version)
	  {
		case 'develop':
		  return 'https://znmj.handturn.com/sitapi/'; 
		  break;
		case 'trial':
		  return 'https://znmj.handturn.com/sitapi/';
		  break;
		case 'release':
		  return 'https://znmj.handturn.com/sitapi/';
		  break;
		default:
		  return 'https://znmj.handturn.com/sitapi/';
	}
};

const domain = domain_even();
	
const interfaces = {
	//获取验证码
	validateCode: domain + "bas/validateCode",  
	//获取微信页面二维码
	miniqrQrImg: domain + "common/miniqrQrImg",   
	 
	//微信登陆
	wechatLogin: domain + "wechat/login",   
	 //微信手机号授权
	wechatGetPhoneNumber: domain + "wechat/getPhoneNumber", 
	//微信手机号授权
	tokenVerify: domain + "wechat/tokenVerify", 
	//获取频道栏目
	homeInitData: domain + "home/homeInitData",

	//图片上传
	uploadImgs: domain + "publish/uploadImgs",
	uploadImg: domain + "publish/uploadImg",
	deleteImg: domain + "publish/deleteImg",
	

	//我的
	initMe: domain + "me/initMe", 
	updateNickName: domain + "me/updateNickName", 
	uploadMeAvatar: domain + "me/uploadMeAvatar", 
	

	initMeNotice: domain + "me/initMeNotice",
	initMeNoticeSet: domain+ "me/initMeNoticeSet",
	
	subimitCommand: domain + "me/subimitCommand",
	//首页列表
	authAreaPageQuery: domain + "home/authAreaPageQuery",
	//访客申请
	initVisitorApply: domain + "visitorApply/initVisitorApply",
	uploadVisitorImg: domain + "visitorApply/uploadVisitorImg",
	submitVisitorApply: domain + "visitorApply/submitVisitorApply",
	//访客审核
	initVisitorAudit: domain + "visitorAudit/initVisitorAudit",
	auditVisitor: domain + "visitorAudit/auditVisitor",
	cancelVisitor: domain + "visitorAudit/cancelVisitor",
	
}
module.exports = interfaces;