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


    $('#getVerifyCode').click(function () {
        var key = $('#sendTelMsgKey').val();
        if (key != null && key != undefined && key.length == 8) {
            $.ajax({
                type: "post",
                url: "/validateTelMsgKey?key=" + key,
                success: function (data) {
                    if (data == '-1') {
                        return;
                    }
                    $.ajax({
                        type: "post",
                        url: "",
                        success: function () {

                        },
                        error: function () {
                            insertDanger('服务器繁忙，请您稍后重试！');
                        }
                    });
                },
                error: function () {
                    insertDanger('服务器繁忙，请您稍后重试！');
                }
            });
        } else {
            insertInfo('请您输入验证码之后再操作！');
            $('#msgVerifyCodeValidate').show();
        }
    });

    $('.validateVerifyCode').click(function () {
        var code = $('#verifyCode').val();
        if (code != null && code != undefined && code.length == 4) {
            $.ajax({
                type: "post",
                url: "/getSendTelMsgKey?code=" + code,
                success: function (data) {
                    if (data == null || data == undefined || data == '') {
                        insertDanger('验证码输入错误或验证码已过期，请您刷新验证码后重新输入！');
                        return;
                    }
                    $('#sendTelMsgKey').val(data);
                    insertSuccess('验证码输入正确！');
                },
                error: function () {
                    insertDanger('服务器繁忙，请您稍后重试！');
                }
            });
        }
    });
});