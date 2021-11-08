function remainTime() {
    var now = new Date(); //현재시간을 구한다.
    var end = new Date(now.getFullYear(),now.getMonth(),now.getDate(),16,36,0);   //오늘날짜의 오후 x시 - 시험종료시간

    var open = new Date(now.getFullYear(),now.getMonth(),now.getDate(),16,33,17); //오늘날짜의 오후 x시 - 시험시작시간

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

    // 버튼 비활성화
    const target = document.getElementById('target_btn');
    target.disabled = true;
    target.classList.add('disabled')

} else if(nt>=et){ //현재시간이 시험종료시간보다 크면
    $("div.time-title").html("시험 종료");
    $(".time").fadeOut();

    // 버튼 비활성화
    const target = document.getElementById('target_btn');
    target.disabled = true;
    target.classList.add('disabled')
    const target2 = document.getElementById('var');
    target2.classList.add('end')

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

    // 버튼 활성화
    const target = document.getElementById('target_btn');
    target.disabled = false;
    target.classList.remove('disabled')

    }
}
setInterval(remainTime,1000);