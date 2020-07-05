
// 验证登陆信息
function check() {
let username = $("#username").val();
    if(username==null||""==username){
        alert("用户名不能为空！");
        return false;
    }
    return true;
}