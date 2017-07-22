$('document').ready(function () {

    console.log($('.msg-loading'));
    console.log($('.msg-loading').is(':hidden'));

    console.log('msg-failed is hidden' + $('.msg-failed').is(':hidden'));

    // if () {

    // }

    function getMsgNumber(number) {
        if (number > 999) {
            return "999+";
        }
        return number;
    }

    var getMsgStatus = 0;

    $('.get-msg').click(function () {

        console.log('get-msg click');

        if (!$('.unlogin-msg').is(":hidden")) {
            $('.unlogin-msg').hide();
            return;
        }
        if (!$('.msg-loading').is(":hidden")) {
            console.log('msg-loading is not hidden');
            $('.msg-loading').hide();
            return;
        }

        if (!$('.msg-ul').is(":hidden")) {
            console.log('msg-ul is not hidden');
            $('.msg-ul').hide();
            return;
        }

        if (!$('.msg-failed').is(':hidden')) {
            console.log('msg-failed is not hidden');
            $('.msg-failed').hide();
            return;
        }

        $('.msg-loading').show();

        if (getMsgStatus > 0) {
            return;
        }

        getMsgStatus = 1;

        console.log('get-msg   msg-loading: show');

        open = true;

        $.ajax({
            type: 'post',
            url: '/getadvice',
            success: function (data) {
                if (open) {
                    if (data.vo.Status == 0 && !$('.msg-loading').is(":hidden")) {
                        $('#atmeNumber').html(getMsgNumber(data.vo.atmeNumber));
                        $('#commentNumber').html(getMsgNumber(data.vo.commentNumber));
                        $('#assistNumber').html(getMsgNumber(data.vo.assistNumber));
                        $('#unfocusNumber').html(getMsgNumber(data.vo.unfocusNumber));
                        $('#privatemsgNumber').html(getMsgNumber(data.vo.privateMsgNumber));
                        $('.msg-ul').show();
                    } else {
                        $('.unlogin-msg').show();
                    }
                    $('.msg-loading').hide();
                }
                console.log('msg ul : show, msg-loading : hide');
                getMsgStatus = 0;
            },
            error: function () {
                getMsgStatus = 0;
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

    $('.loginToViewInfo').click(function () {
        console.log('loginToViewInfo click');
    });


    $('.ulnav').blur(function () {

        open = false;

        $('.msg-ul').hide();
        $('.msg-loading').hide();
        $('.msg-failed').hide();
        $('.unlogin-msg').hide();
        console.log('blur all hide');
    });

    $('.ulnav *').hover(function () {
        $(this).toggleClass('hover');
    });

    $('.ulnav').focusout(function () {

        console.log($('.msg-failed').hasClass('hover'));
        console.log($('.msg-ul').hasClass('hover'));
        console.log($('.msg-loading').hasClass('hover'));
        console.log($('.unlogin-msg').hasClass('hover'));

        if ($('.msg-failed').hasClass('hover') == false &&
            $('.msg-ul').hasClass('hover') == false &&
            $('.msg-loading').hasClass('hover') == false &&
            $('.unlogin-msg').hasClass('hover') == false) {

            open = false;
            $('.msg-ul').hide();
            $('.msg-loading').hide();
            $('.msg-failed').hide();
            $('.unlogin-msg').hide();
            console.log('focusout all hide');
        }


    });


});