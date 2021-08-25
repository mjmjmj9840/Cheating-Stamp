timestamp = new Array

setInterval(function()
{
    const alert_ = document.querySelector('.alert-true')
    if (alert_ !== null) {
        let now = new Date();

        let hours = ('0' + now.getHours()).slice(-2);
        let minutes = ('0' + now.getMinutes()).slice(-2);
        let seconds = ('0' + now.getSeconds()).slice(-2);

        let nowString = hours + ':' + minutes  + ':' + seconds;

        timestamp.push(nowString)
        console.log(timestamp)
    }
}, 1000 );


$(document).ready(function () {
    // 시험 문제 및 답안란 출력
    var questionsString = $("#questions").val();
    var questions = JSON.parse(questionsString);
    for (var i = 0; i < questions.length; i++) {
        // console.log(questions[i]);   // -> {1: "문제"}
        // console.log(questions[i][i+1]);  // -> 문제
        var question = '<p class="question">' + questions[i][i+1] + '</p>';
        var answer = '<textarea rows="8" cols="30" class="answer"></textarea>'; // 임시 답안란
        $('#exam').append(question);
        $('#exam').append(answer);
    }
});

// 사용자 작성 답안을 JSON 배열로 리턴
function saveAnswer() {

    let jsonArray = new Array();
    var answerNum = $("#exam").children(".answer").length;  // answer 개수
    for (let i = 0; i < answerNum; i++) {
        let key = i+1;
        let jsonObj = new Object();
        jsonObj[key] = $("#exam").children(".answer").eq(i).val();
        jsonObj = JSON.stringify(jsonObj);  // -> {"1":"답안1"}
        jsonArray.push(JSON.parse(jsonObj));
    }

    return jsonArray
}

// 시간 문자열(yyyyMMddHHmm) 파싱
function parseTime(time) {
    var year = time.substring(0,4);
    var month = time.substring(4,6);
    var date = time.substring(6,8);
    var hour = time.substring(8,10);
    var min = time.substring(10,12);

    var date = new Date(year, month, date, hour, min);
    console.log(date);
    var timeSec = date.getTime();
    return timeSec;
}

/*타이머*/
var startTime = parseTime($("#examStartTime").val());
var endTime = parseTime($("#examEndTime").val());
var examTime = $("#examTime").val(); // 시험 제한 시간 (x분)

function remainTime() {
    var now = new Date().getTime();

//    console.log("now:" + now);
//    console.log("now:" + new Date().toString());
//    console.log("start:" + parseTime($("#examEndTime").val()));

    if(now<=startTime){ //현재시간이 시험시작시간보다 이르면 시험시작시간까지의 남은 시간을 구한다.
        $(".time").fadeIn();
        $("div.time-title").html("시험 시작까지 남은 시간");

        sec = parseInt(startTime - now) / 1000;
        day = parseInt(sec/60/60/24);
        sec = (sec - (day * 60 * 60 * 24));
        hour = parseInt(sec/60/60);
        sec = (sec - (hour*60*60));
        min = parseInt(sec/60);
        sec = parseInt(sec-(min*60));

        if(hour<10){hour="0"+hour;}
        if(min<10){min="0"+min;}
        if(sec<10){sec="0"+sec;}

        $(".hours").html(hour);
        $(".minutes").html(min);
        $(".seconds").html(sec);
    } else if(now>=endTime){ //현재시간이 시험종료시간보다 크면
        $("div.time-title").html("시험 종료");
        $(".time").fadeOut();

        let data = new FormData();
        data.append('answer', JSON.stringify(saveAnswer()));
        data.append('timestamp', timestamp);

        //시험이 종료되면 데이터 전송 후, 종료화면으로 이동
        $.ajax({
            url: "/exam",
            type: "POST",
            processData: false,
            contentType: false,
            data: data,
            success: function (response) {
                alert("timestamp와 답안이 성공적으로 저장되었습니다.");
                window.location.href = '/examEnd';
            },
            error: function (response) {
                alert("저장에 실패했습니다. 관리자에게 문의해주세요.");
                window.location.href = '/examEnd';
            },
        });
    } else { //현재시간이 시험시작시간보다 늦고 시험종료시간보다 이르면 시험종료시간까지 남은 시간을 구한다.
        $(".time").fadeIn();
        $("div.time-title").html("시험 종료까지 남은 시간");

        sec = parseInt(endTime - now) / 1000;
        day = parseInt(sec/60/60/24);
        sec = (sec - (day * 60 * 60 * 24));
        hour = parseInt(sec/60/60);
        sec = (sec - (hour*60*60));
        min = parseInt(sec/60);
        sec = parseInt(sec-(min*60));

        if(hour<10){hour="0"+hour;}
        if(min<10){min="0"+min;}
        if(sec<10){sec="0"+sec;}

        $(".hours").html(hour);
        $(".minutes").html(min);
        $(".seconds").html(sec);
    }
}
setInterval(remainTime,1000);

//끝내기 버튼을 누르면
$("#end-btn").click(function () {
    let data = new FormData();
    data.append('answers', JSON.stringify(saveAnswer()));
    data.append('timestamp', timestamp);

    //시험이 종료되면 데이터 전송 후, 종료화면으로 이동
    $.ajax({
        url: "/exam",
        type: "POST",
        processData: false,
        contentType: false,
        data: data,
        success: function (response) {
            alert("timestamp와 답안이 성공적으로 저장되었습니다.");
            window.location.href = '/examEnd';
        },
        error: function (response) {
            alert("저장에 실패했습니다. 관리자에게 문의해주세요.");
            window.location.href = '/examEnd';
        },
    });
});