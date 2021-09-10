$(document).ready(function () {
    // 응시자 정보 출력
    var testerInfo = JSON.parse($("#testerInfo").val());

    for (var i = 0; i < testerInfo.length; i++) {
        var idx = i+1
        var num = '<div class="num">' + idx + '</div>';
        var name = '<div class="name">' + testerInfo[i]["name"] + '</div>';
        var username = '<div class="username">' + testerInfo[i]["username"] + '</div>';
        var video = '<div class="video" onclick="location.href=\'watchingVideo?videoId=' + testerInfo[i]["watchingVideo"] + '\'"><i class="fas fa-video"></i></div>';
        console.log(video);
        var answer = '<div class="answer"><i class="fas fa-align-justify"></i></div>';

        var content = '<div class="content" id="delcon">' + num + name + username + video + answer + '</div>';

        $('.exam-list').append(content);
    }
});