$(document).ready(function () {
    // 서버로부터 받아온 에러 메세지 출력
    var errorMsg = $("#errorMsg").val();
    if (errorMsg) {
        alert(errorMsg);
        window.location.replace("/");
    }
});