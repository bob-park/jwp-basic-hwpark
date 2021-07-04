String.prototype.format = function () {
    const args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};


$(document).ready(function () {/* jQuery toggle layout */
    $('#btnToggle').click(function () {
        if ($(this).hasClass('on')) {
            $('#main .col-md-6').addClass('col-md-4').removeClass('col-md-6');
            $(this).removeClass('on');
        } else {
            $('#main .col-md-4').addClass('col-md-6').removeClass('col-md-4');
            $(this).addClass('on');
        }
    });


});


$(function () {
    /**
     * handle
     */
// add answer handle
    $(".answerWrite input[type=submit]").click(addAnswer);

    function addAnswer(e) {
        e.preventDefault();
        const queryString = $("form[name=answer]").serialize();

        $.ajax({
            type: 'post',
            url: '/api/qna/addAnswer',
            data: queryString,
            dataType: 'json',
            error: (err) => console.error(err),
            success: onSuccess,
        });
    }

    function onSuccess(json, status) {
        const answerTemplate = $("#answerTemplate").html();

        const data = json.answer;

        const template = answerTemplate.format(data.writer, new Date(data.createdDate), data.contents, data.answerId, data.answerId);

        $(".qna-comment-slipp-articles").prepend(template);
    }

    // remove handle
    $(".qna-comment-slipp-articles").on("click", ".form-delete", deleteAnswer);

    function deleteAnswer(e) {
        e.preventDefault();

        const queryString = $(this).serialize();

        $.ajax({
            type: 'post',
            url: '/api/qna/removeAnswer',
            data: queryString,
            dataType: 'json',
            error: (err) => console.error(err),
            success: (res) => {

                console.log(res);

                if (res.result.status === true) {
                    $(this).parents(".article").remove();
                }
            },
        });
    }

});



