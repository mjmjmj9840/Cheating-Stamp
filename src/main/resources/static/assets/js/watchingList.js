$(document).ready(function () {
    // 응시자 정보 출력
    var testerInfo = JSON.parse($("#testerInfo").val());
    var examId = $("#examId").val();

    for (var i = 0; i < testerInfo.length; i++) {
        var idx = i+1
        var num = '<div class="num">' + idx + '</div>';
        var name = '<div class="name">' + testerInfo[i]["name"] + '</div>';
        var username = '<div class="username">' + testerInfo[i]["username"] + '</div>';
        if (!testerInfo[i]["watchingVideo"]) { // 응시 영상이 존재하지 않을 경우
            var video = '<div class="video"><i class="fas fa-video"></i></div>';
            var deleteBtn = '<div class="delete" onclick="location.href=\'deleteWatchingList?examId=' + examId + '&username=' + testerInfo[i]["username"] + '&videoId=-1' + '\'"><i class="fas fa-trash-alt"></i></div>';
        } else {
            var video = '<div class="video" onclick="location.href=\'watchingVideo?videoId=' + testerInfo[i]["watchingVideo"] + '\'"><i class="fas fa-video"></i></div>';
            var deleteBtn = '<div class="delete" onclick="location.href=\'deleteWatchingList?examId=' + examId + '&username=' + testerInfo[i]["username"] + '&videoId=' + testerInfo[i]["watchingVideo"] + '\'"><i class="fas fa-trash-alt"></i></div>';
        }
        var answer = '<div class="answer" onclick="location.href=\'checkAnswer?examId=' + examId + '&username=' + testerInfo[i]["username"] + '\'"><i class="fas fa-align-justify"></i></div>';
        var content = '<div class="content" id="delcon">' + num + name + username + video + answer + deleteBtn + '</div>';

        $('.exam-list').append(content);
    }
});