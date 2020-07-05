$(function () {
    getLists();
    showContent();
    window.setInterval("checkScrollScreen(),showContent(),getLists()", 1000);
});

function checkScrollScreen() {
    $("#content_messages").scrollTop($("#content_messages")[0].scrollHeight);
}

function getLists() {
    $.get("/com/list.jsp", function (data) {
        $(".online_ul").html(data);
    })
}

function set(username) {
    $("#receive").val(username);
}

function showContent() {
    $.get("/com/message?action=getMessage&nocache=" + new Date().getTime(), function (data) {
        $("#content_messages").html(data);
    })
}

function send(username) {
    if ($("#receive").val() == "") {
        alert("聊天对象不能为空");
        return false;
    } else if ($("#content").val == "") {
        alert("聊天内容不能为空");
        return false;
    } else {
        $.post("/com/message", {
                action: "sendMessage",
                from: username,
                to: $("#receive").val(),
                face: $("#face").val(),
                color: $("#color").val(),
                content: $("#content").val(),
            }, function (data) {
                $("#content_messages").html(data);
            }
        )
        $("#receive").val("");
        $("#content").val("");
    }

}

function exit() {
location.href="/com/message?action=exit";
alert("欢迎下次光临");
}

