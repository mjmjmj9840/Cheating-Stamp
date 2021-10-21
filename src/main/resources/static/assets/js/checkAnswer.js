// 시험 문제 및 답안 출력
let questions = JSON.parse($("#questions").val());
let answers = JSON.parse($("#answers").val());
for (let i = 0; i < questions.length; i++) {
    let tempHtml = `
        <div class="answer">
            <div class="num">Q${i + 1}</div>
            <div class="question">${questions[i][i + 1]}</div>
            <div class="content">
                <p>${answers[i][i + 1]}</p>
            </div>
        </div>
    `;
    $('#question-answer-box').append(tempHtml);
}
