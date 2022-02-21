/**
 * 回复
 */
//一级评论
function post() {
    var questionId = $("#questions_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, content, 1);
}

//二级评论
function comment(e) {
    let commentId = e.getAttribute("data-id");
    let content = $("#input-" + commentId).val();
    comment2target(commentId, content, 2);
}

function comment2target(questionId, content, type) {
    if (!content) {
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    console.log(isAccepted);
                    if (isAccepted) {
                        window.open("https://gitee.com/oauth/authorize?client_id=a8a8ab23769eb09aba2f27efdedbd52946b72ab323dcfda2a7c9118ea0c29f85&redirect_uri=http://localhost:8887/callback/gitee&response_type=code");
                        window.localStorage.setItem("closabel", true);
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}

/**
 * 二级评论
 */
function collapseComments(e) {
    let id = e.getAttribute("data-id");
    let comments = $("#comment-" + id);
    //获取二级评论的展开状态
    let collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //删除class和状态
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        //折叠时 按钮不亮
        e.classList.remove("active");
    } else {
        var subComments = $("#comment-" + id);
        $.getJSON("/comment/" + id, function (data) {//遍历对应二级评论
            $.each(data.data, function (index, comment) {

                var mediaBody = $("<div/>", {
                    "class": "media-body"
                }).append(
                    $("<h5/>", {
                        "html": comment.user.name
                    })
                ).append(
                    $("<div/>", {
                        "html": comment.content
                    })
                ).append(
                    $("<div/>", {
                        "class": "menu"
                    }).append(
                        // $("span",{
                        //     "class":"pull-right",
                        //     "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                        // })
                    )
                );

                var mediaLeft = $("<div/>", {
                    "class": "media-left"
                }).append(
                    $("<a/>")
                ).append(
                    $("<img/>", {
                        "class": "media-object img-rounded localImg",
                        "src": comment.user.avatarUrl
                    })
                );

                var media = $("<div/>", {
                    "class": "media"
                }).append(mediaLeft).append(mediaBody);

                var comments = $("<div/>", {
                    "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                }).append(media);

                subComments.prepend(comments);
                console.log(111);
            });

            //展开
            comments.addClass("in");
            e.setAttribute("data-collapse", "in");
            //展开时 按钮亮
            e.classList.add("active");
        });

    }
}