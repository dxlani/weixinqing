$('document').ready(function () {


    function insertDanger(msg) {
        $('.alert').hide();

        $('#alert-danger-span').html(msg);
        $('.alert-danger').fadeIn('slow');
    }


    function insertSuccess(msg) {
        $('.alert').hide();

        $('#alert-success-span').html(msg);
        $('.alert-success').fadeIn('slow');
    }

    function insertInfo(msg) {
        $('.alert').hide();

        $('#alert-info-span').html(msg);
        $('.alert-info').fadeIn('slow');

    }

    var result = $('#result').val();
    if (result != null && result != undefined && result != "") {
        insertDanger(result);
    }


    $('#submit').click(function () {
        var mail = $('#mail').val();
        if (mail.indexOf('@') == -1) {
            insertDanger('请输入合法的邮箱账户！');
            return false;
        }
        var name = $('#name').val();
        if (name == null || name == undefined) {
            insertDanger('请输入您的用户名昵称！');
            return false;
        }
        if (name.length <= 4) {
            insertDanger('您的用户名昵称长度过短，请您重新输入！');
            return false;
        }
        var pwd = $('#pwd').val();
        if (pwd == null || pwd == undefined) {
            insertDanger('请输入您的密码！');
            return false;
        }
        if (pwd.length < 6) {
            insertDanger('您的密码长度过短，请您重新输入！');
            return false;
        }
        var repwd = $('#repwd').val();
        if (pwd != repwd) {
            insertDanger('您两次输入的密码不一致，请您重新输入！');
            return false;
        }
        var code = $('#verifyCode').val();
        if (code == null || code == undefined || code.length < 4) {
            insertDanger('请输入验证码！');
            return false;
        }
        return true;
    });

    // $('#getVerifyCode').click(function () {
    //     var key = $('#sendTelMsgKey').val();
    //     if (key != null && key != undefined && key.length == 8) {
    //         $.ajax({
    //             type: "post",
    //             url: "/validateTelMsgKey?key=" + key,
    //             success: function (data) {
    //                 if (data == '-1') {
    //                     return;
    //                 }
    //                 var number = 120;
    //                 while (number > 0) {
    //                     console.log(number);
    //                     timeOut = setTimeout('$("#getVerifyCode").val(number);', 1000);
    //                     --number;
    //                 }
    //             },
    //             error: function () {
    //                 insertDanger('服务器繁忙，请您稍后重试！');
    //             }
    //         });
    //     } else {
    //         insertInfo('请您输入验证码之后再操作！');
    //         $('#msgVerifyCodeValidate').show();
    //     }
    // });
    //
    // $('.validateVerifyCode').click(function () {
    //     var code = $('#verifyCode').val();
    //     if (code != null && code != undefined && code.length == 4) {
    //         $.ajax({
    //             type: "post",
    //             url: "/getSendTelMsgKey?code=" + code,
    //             success: function (data) {
    //                 if (data == null || data == undefined || data == '') {
    //                     insertDanger('验证码输入错误或验证码已过期，请您刷新验证码后重新输入！');
    //                     return;
    //                 }
    //                 $('#sendTelMsgKey').val(data);
    //                 $('#getVerifyCode').trigger('click');
    //                 insertSuccess('验证码输入正确！');
    //             },
    //             error: function () {
    //                 insertDanger('服务器繁忙，请您稍后重试！');
    //             }
    //         });
    //     }
    // });
});