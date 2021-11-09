$(document).ready(function () {
    // 서버로부터 받아온 에러 메세지 출력
    var errorMsg = $("#errorMsg").val();
    var redirect = $("#redirect").val();
    if (errorMsg) {
        alert(errorMsg);
        if (redirect) {
            window.location.replace("/" + redirect);
        }
        else {
            window.location.replace("/");
        };
    }
});