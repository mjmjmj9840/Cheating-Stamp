let tzoffset = (new Date()).getTimezoneOffset() * 60000;

let localISOTime = (new Date(Date.now() - tzoffset)).toISOString().substring(0, 16);
document.querySelector('#startTime').value = localISOTime;
document.querySelector('#endTime').value = localISOTime;

let id = 1;
let id2 = 1;
$(document).ready(function () {
    $('.btnAdd').click(function () {
        $('.buttons').append(
            "<span class='Qnum'></span><div class='question'><textarea class='QA' type='text' name='examQuestion" + id + "' onkeydown='resize(this)' onkeyup='resize(this)'></textarea> <input type='button' class='btnRemove' value='-'></div>"
        );
        id2 = 1;
        $('.buttons').find('span').each(function () {
            $(this)[0].innerText = `Q${id2}`
            id2 += 1;
        });

        id += 1;
        $('.btnRemove').on('click', function () {
            $(this).prev().parent().prev().remove();
            $(this).prev().parent().remove();
            $(this).prev().remove(); // remove the textbox
            $(this).remove(); // remove the button

            id = 1
            $('.buttons').find('div').each(function () {
                $(this)[0].children[0].name = `examQuestion${id}`
                id += 1
            });
            id2 = 1;
            $('.buttons').find('span').each(function () {
                $(this)[0].innerText = `Q${id2}`
                id2 += 1;
            });
        });
    });

    $("#submit-btn").click(function () {
        let formData = $("#form").serializeObject();

        // form validation check
        if (formData['title'].length == 0) {
            alert("시험 제목을 입력해주세요.");
        } else if (formData['startTime'] < localISOTime) {
            alert("시작 날짜를 다시 설정해주세요.");
        } else if (formData['endTime'] < formData['startTime']) {
            alert("종료 날짜를 다시 설정해주세요.");
        } else if (id <= 1) {
            alert("시험 문제를 입력해주세요.");
        } else {
            // question 목록을 json array로 저장
            let jsonArray = new Array();
            for (let i = 1; i < id; i++) {
                let key = 'examQuestion' + i;
                let jsonObj = new Object();
                jsonObj[i] = formData[key];
                jsonObj = JSON.stringify(jsonObj);
                jsonArray.push(JSON.parse(jsonObj));
            }

            let data = new Object();
            data.title = formData['title'];
            data.startTime = formData['startTime'];
            data.endTime = formData['endTime'];
            data.questions = JSON.stringify(jsonArray);

            $.ajax({
                url: "/createExam",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(data),
                success: function (response) {
                    alert("시험을 성공적으로 생성했습니다.");
                    window.location.href = '/';
                },
                error: function (response) {
                    alert("시험 생성에 실패했습니다. 관리자에게 문의해주세요.");
                    window.location.href = '/';
                }
            });
        }
    });
});

function resize(obj) {
    obj.style.height = "1px";
    obj.style.height = (12 + obj.scrollHeight) + "px";
}