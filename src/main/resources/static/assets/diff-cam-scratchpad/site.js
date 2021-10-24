let video = document.getElementById('video');
let timestamp = document.getElementById('timestamp');

function initSuccess() {
	DiffCamEngine.start();
}

function initError() {
	alert('Something went wrong.');
}

function capture(payload) {
	score = payload.score;
	time = getNowTime();
	if (score >= 500 && time >= '00:00:03') {
		let tempHtml = `움직임이 감지되었습니다. timestamp ${time} score = ${score}`;
		timestamp.append(tempHtml);
	}
}

let start = new Date();

function getNowTime() {
	let now = new Date();
	now = now.getTime() - start.getTime();
	let hours = Math.floor((now % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
	let minutes = Math.floor((now % (1000 * 60 * 60)) / (1000 * 60));
	let seconds = Math.floor((now % (1000 * 60)) / 1000) - 1;
	let nowString = ('00' + hours).slice(-2) + ':' + ('00' + minutes).slice(-2)  + ':' + ('00' + seconds).slice(-2);

	return nowString;
}

DiffCamEngine.init({
	video: video,
	initSuccessCallback: initSuccess,
	initErrorCallback: initError,
	captureCallback: capture
});