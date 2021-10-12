var video = document.getElementById('video');
var canvas = document.getElementById('motion');
var score = document.getElementById('score');
var notice = document.getElementById('notice');

function initSuccess() {
	DiffCamEngine.start();
}

function initError() {
	alert('Something went wrong.');
}

function capture(payload) {
	score.textContent = payload.score;

	if (payload.score >= 8000) {
        notice.textContent = "수험자의 움직임이 경계 범위를 넘었습니다. 주의하세요.";
	}
	else {
	    notice.textContent = "";
	}
}

DiffCamEngine.init({
	video: video,
	motionCanvas: canvas,
	initSuccessCallback: initSuccess,
	initErrorCallback: initError,
	captureCallback: capture
});
