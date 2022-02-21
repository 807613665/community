function post(){
    var questionId = $("#questions_id").val();
    var content = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response){
            if (response.code==200){
                $("#comment_textarea").hide();
            }else{
                if(response.code==2003){
                    var isAccepted = confirm(response.message);
                    console.log(isAccepted);
                    if(isAccepted){
                        window.open("https://gitee.com/oauth/authorize?client_id=a8a8ab23769eb09aba2f27efdedbd52946b72ab323dcfda2a7c9118ea0c29f85&redirect_uri=http://localhost:8887/callback/gitee&response_type=code");
                        window.localStorage.setItem("closabel",true);
                    }
                }else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}