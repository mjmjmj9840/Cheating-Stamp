var video = document.getElementById('video');
var canvas = document.getElementById('motion');
var score = document.getElementById('score');
var warning = document.getElementById('warning');

function initSuccess() {
	DiffCamEngine.start();
}

function initError() {
	alert('Something went wrong.');
}

function capture(payload) {
	score.textContent = payload.score;
	if (payload.score >= 100) {
		warning.style.display = 'block';
	}
	else {
		warning.style.display = 'none';
	}
}

DiffCamEngine.init({
	video: video,
	motionCanvas: canvas,
	initSuccessCallback: initSuccess,
	initErrorCallback: initError,
	captureCallback: capture
});
