<style>
    #${className?uncap_first}-edit {
        padding: 20px 25px 25px 0;
    }
    #${className?uncap_first}-edit .layui-treeSelect .ztree li a, .ztree li span {
        margin: 0 0 2px 3px !important;
    }
    .layui-form-select .layui-input {
        padding-right: 0px;
        cursor: pointer;
    }
</style>
<div class="layui-fluid" id="${className?uncap_first}-edit">
    <form class="layui-form" action="" lay-filter="${className?uncap_first}-edit-form">
        <div class="layui-form-item">
            <#if columns??>
            <#list columns as column>
                <#if column.isKey = true>
                    <div class="layui-form-item febs-hide">
                        <label class="layui-form-label">${column.remark}:</label>
                        <div class="layui-input-block">
                            <input type="text" name="${column.field?uncap_first}" data-th-value="----{${className?uncap_first}.${column.field?uncap_first}}">
                        </div>
                    </div>
                <#else>
                    <div class="layui-inline">
                        <label class="layui-form-label febs-form-item-require">${column.remark}:</label>
                        <div class="layui-input-block">
                            <input type="text" name="${column.field?uncap_first}"
                                   autocomplete="off" class="layui-input" data-th-value="----{${className?uncap_first}.${column.field?uncap_first}}">
                        </div>
                    </div>
                </#if>
            </#list>
            </#if>
        </div>
        <div class="layui-form-item febs-hide">
            <button class="layui-btn" lay-submit="" lay-filter="${className?uncap_first}-edit-form-submit" id="submit"></button>
            <button type="reset" class="layui-btn" id="reset"></button>
        </div>
    </form>
</div>

<script data-th-inline="javascript">
    layui.use(['febs', 'form', 'formSelects', 'validate', 'treeSelect'], function () {
        var $ = layui.$,
            febs = layui.febs,
            layer = layui.layer,
            treeSelect = layui.treeSelect,
            form = layui.form,
            $view = $('#${className?uncap_first}-edit'),
            ${className?uncap_first} = [[----{${className?uncap_first}}]],
            validate = layui.validate;

        form.verify(validate);
        form.render();

        form.on('submit(${className?uncap_first}-edit-form-submit)', function (data) {
            febs.post(ctx + '${className?uncap_first}/${className?uncap_first}/save', data.field, function () {
                layer.closeAll();
                $('#febs-${className?uncap_first}').find('#query').click();
            });
            return false;
        });
    });
</script>