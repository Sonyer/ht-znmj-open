layui.define(function(exports) {
  exports('conf', {
    container: 'bole',
    containerBody: 'febs-body',
    v: '2.0',
    base: layui.cache.base,
    css: layui.cache.base + 'css/',
    views: layui.cache.base + 'views/',
    viewLoadBar: true,
    debug: layui.cache.debug,
    name: 'bole',
    entry: '/index',
    engine: '',
    eventName: 'febs-event',
    tableName: 'febs',
    requestUrl: './'
  })
});
