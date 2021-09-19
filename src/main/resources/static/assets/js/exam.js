let blobs;
let blob; // 데이터
let rec; // 미디어스트림 기반 Media Recorder 객체
let stream; // 미디어스트림
let videoStream; // 비디오스트림
let start = new Date();

window.onload = async () => {  // 비디오 녹화 함수
    videoStream = await navigator.mediaDevices.getDisplayMedia({video: {width: 720, height: 480}, audio: false});
    
    const tracks = [
        ...videoStream.getVideoTracks(),
    ];

    stream = new MediaStream(tracks);

    blobs = [];

    rec = new MediaRecorder(stream, {mimeType: 'video/webm; codecs=vp9,opus'});
    rec.ondataavailable = (e) => blobs.push(e.data);

    start = new Date();
    console.log(start);

    rec.onstop = async () => { // 녹화 종료시 영상 파일 만들고 서버로 전송
        blob = new Blob(blobs, {type: 'video/mp4'});
        let code = $("#examCode").val()
        let form = new FormData();
        form.append('file', blob);
        $.ajax({
            url: "/upload/" + code,
            type: "POST",
            data: form,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response) {
                alert("timestamp와 응시 영상이 성공적으로 저장되었습니다.");
                window.location.href = '/examEnd'
            }, error: function (response) {
                alert("응시 영상 저장에 실패했습니다. 관리자에게 문의해주세요.");
                window.location.href = '/examEnd'
            },
        });
    };

    rec.start(); // 녹화 시작
};

$(document).ready(function () {
    // 시험 문제 및 답안란 출력
    let questionsString = $("#questions").val();
    console.log(questionsString);
    let questions = JSON.parse($("#questions").val());
    for (var i = 0; i < questions.length; i++) {
        var question = '<p class="question">' + questions[i][i+1] + '</p>';
        var answer = '<textarea rows="8" cols="30" class="answer"></textarea>';
        $('#exam').append(question);
        $('#exam').append(answer);
    }

    setInterval(function(){
        eyetracking();
        remainTime();
    }, 1000);
});

timestamp = new Array

function eyetracking() {
    const alert_ = document.querySelector('.alert-true')
    if (alert_ !== null) {
        let now = new Date();
        now = now.getTime() - start.getTime();
        let hours = Math.floor((now % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        let minutes = Math.floor((now % (1000 * 60 * 60)) / (1000 * 60));
        let seconds = Math.floor((now % (1000 * 60)) / 1000) - 1;
        let nowString = ('00' + hours).slice(-2) + ':' + ('00' + minutes).slice(-2)  + ':' + ('00' + seconds).slice(-2);

        timestamp.push(nowString)
        console.log(timestamp)
    }
}

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
    var month = time.substring(4,6) - 1;
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

        let code = $("#examCode").val()
        let data = new FormData();
        data.append('examId', $('#examId').val());
        data.append('answer', JSON.stringify(saveAnswer()));
        data.append('timestamp', timestamp);

        //시험이 종료되면 데이터 전송 후, 종료화면으로 이동
        $.ajax({
            url: "/exam/" + code,
            type: "POST",
            processData: false,
            contentType: false,
            data: data,
            success: function (response) {
                rec.stop() // 응시 영상 저장
            },
            error: function (response) {
                alert("timestamp와 답안 저장에 실패했습니다. 관리자에게 문의해주세요.");
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

//끝내기 버튼을 누르면
$("#end-btn").click(function () {
    let code = $("#examCode").val()
    let data = new FormData();
    data.append('examId', $('#examId').val());
    data.append('answers', JSON.stringify(saveAnswer()));
    data.append('timestamp', timestamp);

    //시험이 종료되면 데이터 전송 후, 종료화면으로 이동
    $.ajax({
        url: "/exam/" + code,
        type: "POST",
        processData: false,
        contentType: false,
        data: data,
        success: function (response) {
            rec.stop() // 응시 영상 저장
        },
        error: function (response) {
            alert("timestamp와 답안 저장에 실패했습니다. 관리자에게 문의해주세요.");
            window.location.href = '/examEnd';
        },
    });
});