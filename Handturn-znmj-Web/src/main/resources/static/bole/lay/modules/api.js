layui.define([],function(exports){
    exports('api',{
        getMenus: 'portal/userResource/' + currentUser.userCode + '?invalid_ie_cache=' + new Date().getTime()
    });
});