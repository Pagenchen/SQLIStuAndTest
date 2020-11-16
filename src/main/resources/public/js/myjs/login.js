/**
 * 在js中获取user.html中的路径；
 * 在spring boot部署到tomcat后或者
 * 在application.properties文件中指定了server.context-path=/的路径；
 * 则必须从后台获取数据必须执行context-path
 * @returns 返回项目路径
 */
function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0, index + 1);
    return result;
}

/**
 * 登录
 */
var picCode;
$(function () {
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;
        form.on("submit(login)", function () {
            login();
            return false;
        });
    })
});

function login() {
    var flag = checkParams();
    var uri = getContextPath();
    if (flag != false) {
        $.post(uri + "/user/login", $("#useLogin").serialize(), function (data) {
            console.log("data:" + data);
            if (data.code == "1000") {
                layer.alert("登录成功", function () {
                    window.location.href = getContextPath() + "/home";
                });
            } else {
                $("#password").val("");
                layer.alert(data.message);
            }
        });
    }
}

function checkParams() {
    //  校验
    var username = $("#username").val();
    var password = $("#password").val();
    if ("ok" != ValidateUtils.checkUserName(username) || "ok" != ValidateUtils.checkSimplePassword(password)) {
        layer.alert("请您输入正确的用户名和密码");
        return false;
    }
    return true;
}