layui.extend({
    febs: 'lay/modules/febs',
    validate: 'lay/modules/validate'
}).config({
    version: '1.0.1'
}).define(['febs', 'conf'], function (exports) {
    layui.febs.initPage();
    exports('index', {});
});