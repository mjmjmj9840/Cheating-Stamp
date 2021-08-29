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

// 사용자 작성 답안을 JSON 배열로 리턴
function saveAnswer() {

    let jsonArray = new Array();
    var answerNum = $("#exam").children(".answer").length;  // answer 개수
    for (let i = 0; i < answerNum; i++) {
        let key = i+1;
        let jsonObj = new Object();
        jsonObj[key] = $("#exam").children(".answer").eq(i).val();
        jsonObj = JSON.stringify(jsonObj);  // {"examAnswer1":"답안1"}
        jsonArray.push(JSON.parse(jsonObj));
    }

    return jsonArray
}

/*타이머*/
function remainTime() {
  var now = new Date(); //현재시간을 구한다.
  var end = new Date(now.getFullYear(),now.getMonth(),now.getDate(),14,39,0);
//오늘날짜의 오후 x시 - 시험종료시간

  var open = new Date(now.getFullYear(),now.getMonth(),now.getDate(),4,45,17);
//오늘날짜의 오후 x시 - 시험시작시간

  var nt = now.getTime(); // 현재시간
  var ot = open.getTime(); // 시험시작시간
  var et = end.getTime(); // 시험종료시간

if(nt<=ot){ //현재시간이 시험시작시간보다 이르면 시험시작시간까지의 남은 시간을 구한다. 
  $(".time").fadeIn();
  $("div.time-title").html("시험 시작까지 남은 시간");

  sec =parseInt(ot - nt) / 1000;
  day  = parseInt(sec/60/60/24);
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

 } else if(nt>=et){ //현재시간이 시험종료시간보다 크면
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

        timestamp.push(nowString)
        console.log(timestamp)
    }
}, 1000 );

// 사용자 작성 답안을 JSON 배열로 리턴
function saveAnswer() {

    let jsonArray = new Array();
    var answerNum = $("#exam").children(".answer").length;  // answer 개수
    for (let i = 0; i < answerNum; i++) {
        let key = i+1;
        let jsonObj = new Object();
        jsonObj[key] = $("#exam").children(".answer").eq(i).val();
        jsonObj = JSON.stringify(jsonObj);  // {"examAnswer1":"답안1"}
        jsonArray.push(JSON.parse(jsonObj));
    }

    return jsonArray
}

/*타이머*/
function remainTime() {
    var now = new Date(); //현재시간을 구한다.
    var end = new Date(now.getFullYear(),now.getMonth(),now.getDate(),8,50,0); //오늘날짜의 오후 x시 - 시험종료시간

    var open = new Date(now.getFullYear(),now.getMonth(),now.getDate(),4,45,17); //오늘날짜의 오후 x시 - 시험시작시간

    var nt = now.getTime(); // 현재시간
    var ot = open.getTime(); // 시험시작시간
    var et = end.getTime(); // 시험종료시간

    if(nt<=ot){ //현재시간이 시험시작시간보다 이르면 시험시작시간까지의 남은 시간을 구한다.
        $(".time").fadeIn();
        $("div.time-title").html("시험 시작까지 남은 시간");

        sec =parseInt(ot - nt) / 1000;
        day  = parseInt(sec/60/60/24);
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
    } else if(nt>=et){ //현재시간이 시험종료시간보다 크면
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

        sec =parseInt(et - nt) / 1000;
        day  = parseInt(sec/60/60/24);
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