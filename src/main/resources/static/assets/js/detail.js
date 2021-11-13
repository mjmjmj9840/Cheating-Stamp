$(document).ready(function () {
    let examId = $("#examId").val();

    $('.removeBtn').on('click', function () {
        let username = $(this).prev().text();
        $.ajax({
            url: "/deleteExam/" + examId + "/" + username,
            type: "GET",
            success: function (response) {
                alert("성공적으로 삭제되었습니다.");
                window.location.reload();
            },
            error: function (response) {
                alert("삭제에 실패했습니다. 관리자에게 문의해주세요.");
                window.location.reload();
            },
        });
    });

    $('.addBtnS').click(function () {
        let username = $('#supervisorEmail').val();
        if (username == "") {
            alert("이메일을 입력해주세요.")
            return
        }

        $.ajax({
            url: "/detailExam",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({examId: examId, username: username}),
            success: function (response) {
                alert("성공적으로 저장되었습니다.");
                window.location.reload();
            },
            error: function (response) {
                alert("존재하지 않는 사용자입니다.");
                window.location.reload();
            },
        });
    });

    $('.addBtnT').click(function () {
        let username = $('#testerEmail').val();
        if (username == "") {
            alert("이메일을 입력해주세요.")
            return
        }

        $.ajax({
            url: "/detailExam",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({examId: examId, username: username}),
            success: function (response) {
                alert("성공적으로 저장되었습니다.");
                window.location.reload();
            },
            error: function (response) {
                alert("존재하지 않는 사용자입니다.");
                window.location.reload();
            },
        });
    });
});