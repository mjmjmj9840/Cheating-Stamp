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

// 화면 녹화
let blobs;
let blob; // 데이터
let rec; // 미디어스트림 기반 Media Recorder 객체
let stream; // 미디어스트림
// 임시 버튼
const startBtn = document.getElementById('start-btn');
const endBtn = document.getElementById('end-btn');
const download = document.getElementById('download');

window.onload = async () => {
	startBtn.onclick = async () => {
		var constraints = {
			audio: false,
			video: { facingMode: "user" }
		};

		navigator.mediaDevices.getUserMedia(constraints)
			.then((requestedStream) => {
				stream = requestedStream;

				blobs = [];

				rec = new MediaRecorder(stream, {mimeType: 'video/webm; codecs=vp9,opus'});
				rec.ondataavailable = (e) => blobs.push(e.data);

				rec.onstop = async () => {
					blob = new Blob(blobs, {type: 'video/mp4'});
					console.log(blob);
					let url = window.URL.createObjectURL(blob);
					download.href = url;
					download.download = 'test.mp4';
					download.style.display = 'block';
				};

				rec.start(); // 녹화 시작
			})
			.catch((err) => {
				console.log(err);
			});
	}

	endBtn.onclick = () => {
		rec.stop(); // 화면녹화 종료 및 녹화된 영상 다운로드
		stream.getVideoTracks().forEach(s=>s.stop())
		stream = null;
	};
};