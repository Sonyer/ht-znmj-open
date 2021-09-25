// 定义常用的校验，常用的正则 https://www.open-open.com/code/view/1430625516632
layui.define(['jquery'], function (exports) {
    var $ = layui.jquery;
    exports('validate', {
        username: function (value, item) {
            if (!isEmpty(value)) {
                var result = '';
                $.ajax({
                    url: ctx + 'user/check/' + value,
                    data: {
                        "userId": item.getAttribute('id')
                    },
                    async: false,
                    type: 'get',
                    success: function (d) {
                        (!d) && (result = '该用户名已存在')
                    }
                });
                if (!isEmpty(result)) {
                    return result;
                }
            }
        },
        cron: function (value, item) {
            if (!isEmpty(value)) {
                var result = '';
                $.ajax({
                    url: ctx + 'job/cron/check',
                    data: {
                        "cron": value
                    },
                    async: false,
                    type: 'get',
                    success: function (d) {
                        (!d) && (result = 'cron表达式不合法')
                    }
                });
                if (!isEmpty(result)) {
                    return result;
                }
            }
        },
        integer: function(value, item) {
            if (!isEmpty(value)) {
                if (!/^\+?([1-9]\d*)?(([1-9]\d*)(\.0*))?$/.test(value)) {
                    return '请输入正确的正整数'
                }else{
                    if (value <= 0) {
                        return '请输入正确的正整数'
                    }
                }
            }
        },
        positiveNumber: function(value, item) {
            if (!isEmpty(value)) {
                if (!/^[+-]?(0|([1-9]\d*))(\.\d+)?$/.test(value)) {
                    return '请输入≥0的数字'
                } else {
                    if (value < 0) {
                        return '请输入≥0的数字'
                    }
                }
            }
        },
        email: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$").test(value)) {
                    return '请填写正确的邮箱';
                }
            }
        },
        phone: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^1\\d{10}$").test(value)) {
                    return '请填写正确的手机号码';
                }
            }
        },
        number: function (value) {
            if (!isEmpty(value)) {
                if(!/^[+-]?(0|([1-9]\d*))(\.\d+)?$/.test(value)){
                    return '请输入正确的数字'
                }
            }
        },
        checkDate: function (value) {
            if (!isEmpty(value)) {
                if(!/^(\d{4})[-\/](\d{1}|0\d{1}|1[0-2])([-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/.test(value)){
                    return '请输入日期!'
                }
            }
        },
        range: function (value, item) {
            var minlength = item.getAttribute('minlength') ? item.getAttribute('minlength') : -1;
            var maxlength = item.getAttribute('maxlength') ? item.getAttribute('maxlength') : -1;
            var length = value.length;
            if (minlength === -1) {
                if (length > maxlength) {
                    return '长度不能超过 ' + maxlength + ' 个字符';
                }
            } else if (maxlength === -1) {
                if (length < minlength) {
                    return '长度不能少于 ' + minlength + ' 个字符';
                }
            } else {
                if (length > maxlength || length < minlength) {
                    return '长度范围 ' + minlength + ' ~ ' + maxlength + ' 个字符';
                }
            }
        }
    });

    function isEmpty(obj) {
        return typeof obj == 'undefined' || obj == null || obj === '';
    }
});