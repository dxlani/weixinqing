$('document').ready(function () {

    console.log($('.msg-loading'));
    console.log($('.msg-loading').is(':hidden'));

    console.log($('.msg-ul').is(':hidden'));
    // if () {

    // }

    function getMsgNumber(number) {
        if (number > 999) {
            return "999+";
        }
        return number;
    }

    $('.get-msg').click(function () {

        $('.msg-loading').show();

        console.log('get-msg   msg-loading: show');

        open = true;

        $.ajax({
            type: 'post',
            url: '/getadvice',
            success: function (data) {
                if (open) {
                    $('#atmeNumber').html(getMsgNumber(data.vo.atmeNumber));
                    $('#commentNumber').html(getMsgNumber(data.vo.commentNumber));
                    $('#assistNumber').html(getMsgNumber(data.vo.assistNumber));
                    $('#unfocusNumber').html(getMsgNumber(data.vo.unfocusNumber));
                    $('#privatemsgNumber').html(getMsgNumber(data.vo.privateMsgNumber));
                    $('.msg-ul').show();
                    $('.msg-loading').hide();
                }
                console.log('msg ul : show, msg-loading : hide');
            },
            error: function () {
                $('.msg-loading').hide();
                $('.msg-failed').show();
            }
        });

        return false;
    });


    // $('.ulnav').blur(function () {
    //     $('.msg-ul').hide();
    //     $('.msg-loading').hide();
    //     $('.msg-failed').hide();
    //     $('.unlogin-msg').hide();
    //     console.log('blur all hide');
    // });

    $('.ulnav').focusout(function () {

        open = false;

        $('.msg-ul').hide();
        $('.msg-loading').hide();
        $('.msg-failed').hide();
        $('.unlogin-msg').hide();
        console.log('focusout all hide');
    });


});