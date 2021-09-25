<div class="layui-fluid layui-anim febs-anim" id="febs-${className?uncap_first}" lay-title="${className}管理">
    <div class="layui-row febs-container">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body febs-table-full">
                    <form class="layui-form layui-table-form" lay-filter="${className?uncap_first}-table-form" id="${className?uncap_first}-table-form">
                        <div class="layui-row">
                            <div class="layui-col-md10">
                                <div class="layui-form-item">
                                    <#if columns??>
                                    <#list columns as column>
                                        <div class="layui-inline">
                                            <label class="layui-form-label">${column.remark}</label>
                                            <div class="layui-input-inline">
                                                <input type="text" name="${column.field?uncap_first}" autocomplete="off" class="layui-input">
                                            </div>
                                        </div>
                                    </#list>
                                    </#if>
                                </div>
                            </div>
                            <div class="layui-col-md2 layui-col-sm12 layui-col-xs12 table-action-area">
                                <div class="layui-btn layui-btn-sm layui-btn-primary table-action" id="query">
                                    <i class="layui-icon">&#xe848;</i>
                                </div>
                                <div class="layui-btn layui-btn-sm layui-btn-primary table-action" id="reset">
                                    <i class="layui-icon">&#xe79b;</i>
                                </div>
                            </div>
                        </div>
                    </form>
                    <table id="${className?uncap_first}Table" lay-filter="${className?uncap_first}Table" lay-data="{id: '${className?uncap_first}Table'}"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="${className?uncap_first}Table-toolbar">
    <button lay-event="add" class="layui-btn  layui-btn-sm layui-btn-primary"
            shiro:hasPermission="${className?uncap_first}:add">
        <i class="layui-icon">&#xe7aa;</i> 新增
    </button>
    <button lay-event="enable" class="layui-btn  layui-btn-sm layui-btn-primary"
            shiro:hasPermission="${className?uncap_first}:enable">
        <i class="layui-icon">&#xe77d;</i> 启用
    </button>
    <button lay-event="disable" class="layui-btn  layui-btn-sm layui-btn-primary"
            shiro:hasPermission="${className?uncap_first}:disable">
        <i class="layui-icon">&#xe781;</i> 禁用
    </button>
</script>
<script type="text/html" id="${className?uncap_first}-option">
    <span shiro:lacksPermission="${className?uncap_first}:view,${className?uncap_first}:update">
        <span class="layui-badge-dot febs-bg-orange"></span> 无权限
    </span>
    <a lay-event="update" shiro:hasPermission="${className?uncap_first}:update" lay-tips="修改"><i
                class="layui-icon febs-edit-area febs-blue">&#xe7a4;</i></a>
    <a lay-event="view" shiro:hasPermission="${className?uncap_first}:view" lay-tips="查看"><i
                class="layui-icon febs-edit-area febs-green">&#xe7a5;</i></a>
</script>
<script data-th-inline="javascript" type="text/javascript">
    layui.use(['dropdown', 'jquery', 'laydate', 'form', 'table', 'febs', 'treeSelect'], function () {
        var $ = layui.jquery,
            laydate = layui.laydate,
            febs = layui.febs,
            form = layui.form,
            table = layui.table,
            treeSelect = layui.treeSelect,
            dropdown = layui.dropdown,
            $view = $('#febs-${className?uncap_first}'),
            $query = $view.find('#query'),
            $reset = $view.find('#reset'),
            sortObject = {field: 'createDate', type: null},
            $searchForm = $view.find('#${className?uncap_first}-table-form'),
            baseStatusOption = [[----{baseStatusOption}]],
            baseStatusOptionMap = {},
            tableIns;

        form.render();

        initDictData();

        initTable();

        //监听事件
        table.on('toolbar(${className?uncap_first}Table)', function(obj) {
            var data = obj.data,
                layEvent = obj.event;
            var checkStatus = table.checkStatus('${className?uncap_first}Table');
            if (layEvent === 'add') {
                febs.modal.open('新增${className}', '${className?uncap_first}/${className?uncap_first}/add', {
                    btn: ['提交', '重置'],
                    area: $(window).width() <= 750 ? '95%' : '50%',
                    offset: '100px',
                    yes: function (index, layero) {
                        $('#${className?uncap_first}-edit').find('#submit').trigger('click');
                    },
                    btn2: function () {
                        $('#${className?uncap_first}-edit').find('#reset').trigger('click');
                        return false;
                    }
                });
            }
            if (layEvent === 'enable') {
                if (!checkStatus.data.length) {
                    febs.alert.warn('请选择需要启用的${className}');
                } else {
                    var ${className?uncap_first}Ids = [];
                    layui.each(checkStatus.data, function (key, item) {
                        ${className?uncap_first}Ids.push(item.id)
                    });
                    febs.post(ctx + '${className?uncap_first}/${className?uncap_first}/enable/' + ${className?uncap_first}Ids.join(','), null, function () {
                        tableIns.reload({where: getQueryParams(), page: {curr: 1}});
                        febs.alert.success('操作成功!');
                    });
                }
            }
            if (layEvent === 'disable') {
                if (!checkStatus.data.length) {
                    febs.alert.warn('请选择需要禁用的${className}');
                } else {
                    var ${className?uncap_first}Ids = [];
                    layui.each(checkStatus.data, function (key, item) {
                        ${className?uncap_first}Ids.push(item.id)
                    });
                    febs.post(ctx + '${className?uncap_first}/${className?uncap_first}/disable/' + ${className?uncap_first}Ids.join(','), null, function () {
                        tableIns.reload({where: getQueryParams(), page: {curr: 1}});
                        febs.alert.success('操作成功!');
                    });
                }
            }
            if (layEvent === 'DEFAULT_EXPORT') {
                exportTable();
            }
        });


        table.on('tool(${className?uncap_first}Table)', function (obj) {
            var data = obj.data,
                layEvent = obj.event;
            if (layEvent === 'view') {
                febs.modal.open('${className}信息', '${className?uncap_first}/${className?uncap_first}/update/' + data.id, {
                    area: $(window).width() <= 750 ? '95%' : '50%',
                    offset: '100px',
                });
            }
            if (layEvent === 'update') {
                febs.modal.open('修改${className}', '${className?uncap_first}/${className?uncap_first}/update/' + data.id, {
                    area: $(window).width() <= 750 ? '90%' : '50%',
                    offset: '100px',
                    btn: ['提交', '取消'],
                    yes: function (index, layero) {
                        $('#${className?uncap_first}-edit').find('#submit').trigger('click');
                    },
                    btn2: function () {
                        layer.closeAll();
                    }
                });
            }
        });

        table.on('sort(${className?uncap_first}Table)', function (obj) {
            sortObject = obj;
            tableIns.reload({
                initSort: obj,
                where: $.extend(getQueryParams(), {
                    field: obj.field,
                    order: obj.type
                })
            });
        });

        $query.on('click', function () {
            var params = $.extend(getQueryParams(), {field: sortObject.field, order: sortObject.type});
            tableIns.reload({where: params, page: {curr: 1}});
        });

        $reset.on('click', function () {
            $searchForm[0].reset();
            tableIns.reload({where: getQueryParams(), page: {curr: 1}, initSort: sortObject});
        });

        function initDictData(){
            baseStatusOptionMap = {};
            for(var index in baseStatusOption){
                baseStatusOptionMap[baseStatusOption[index].value] = baseStatusOption[index].text;
            }
        }

        function initTable() {
            tableIns = febs.table.init({
                elem: $view.find('table'),
                id: '${className?uncap_first}Table',
                toolbar: '#${className?uncap_first}Table-toolbar',
                defaultToolbar: ['filter', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                    title: '导出'
                    ,layEvent: 'DEFAULT_EXPORT'
                    ,icon: 'layui-icon-export'
                }],
                url: ctx + '${className?uncap_first}/${className?uncap_first}/list',
                cols: [
                        [
                        {fixed: 'left',type: 'checkbox', width: 50},
                        <#if columns??>
                        <#list columns as column>
                        <#if column.field?uncap_first = 'status'>
                            {field: '${column.field?uncap_first}', title: '${column.remark}',sort: true,dataMap:baseStatusOptionMap,templet: function(d){
                                    return baseStatusOptionMap[d.status] == null? d.status:baseStatusOptionMap[d.status];
                                }, minWidth: 100},
                        <#else>
                            {field: '${column.field?uncap_first}', title: '${column.remark}',sort: true, minWidth: 100},
                        </#if>
                        </#list>
                        </#if>
                        {fixed: 'right',title: '操作', toolbar: '#${className?uncap_first}-option', width: 170}
                    ]
                ],
                height: $(document).height() - $('#${className?uncap_first}Table').offset().top - 30,
            });
        }

        //导出函数
        function exportTable(){
            var queryData = getQueryParams();
            var sortObj = {field: sortObject.field, order: sortObject.type};
            var cols = tableIns.config.cols[0];

            var colsTemp = [];
            for(var i = 0; i < cols.length; i++){
                var colTemp = {};

                var flag = false;
                for(var key in cols[i]){
                    if(key == 'field'){
                        flag = true;
                    }
                    if(key == 'field' || key == 'title' || key == 'dataMap'){
                        colTemp[key] = cols[i][key];
                    }
                }
                if(flag){
                    colsTemp.push(colTemp);
                }
            }
            var exportData = {
                queryData:JSON.stringify(queryData),
                sortObj:JSON.stringify(sortObj),
                cols:JSON.stringify(colsTemp)
            }
            febs.download(ctx + '${className?uncap_first}/${className?uncap_first}/export/', exportData, '导出数据.xlsx');
            return false;
        }

        function getQueryParams() {
            return {
                <#if columns??>
                <#list columns as column>
                    ${column.field?uncap_first}: $searchForm.find('input[name="${column.field?uncap_first}"]').val().trim(),
                </#list>
                </#if>
            };
        }
    })
</script>