$('document').ready(function () {
    $('#submit').click(function () {
        var code = $('#code').val();
        if (code == null || code == undefined || code.length < 4) {
            $('.errorSpan').html('请您输入验证码！');
            $('.alert-error-msg').show();
            return false;
        }
        var account = $('#username').val();
        if (account == null || account == undefined) {
            $('.errorSpan').html('请您输入手机号或邮箱！');
            $('.alert-error-msg').show();
            return false;
        }
        var pwd = $('#pwd').val();
        if (pwd == null || pwd == undefined) {
            $('.errorSpan').html('请您输入密码！');
            $('.alert-error-msg').show();
            return false;
        }
        return true;
    });
});