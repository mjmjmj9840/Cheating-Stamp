let video = document.getElementById('video');
let timestamp_info = document.getElementById('timestamp');
let timestamp = "";
let hostname = window.location.hostname;
let mobileUrl = window.location.search.slice(6);

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
		timestamp_info.append(tempHtml);
		if (timestamp.length > 0) time = "," + time;  // 처음 시간을 저장하는게 아닐 경우
		timestamp += time;
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
	captureWidth: 720,
	captureHeight: 480,
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

window.onload = async () => {
	startBtn.onclick = async () => {
		var constraints = {
			audio: false,
			video: { facingMode: "user", width: 240, height: 360}
		};

		navigator.mediaDevices.getUserMedia(constraints)
			.then((requestedStream) => {
				stream = requestedStream;

				blobs = [];

				rec = new MediaRecorder(stream, {mimeType: 'video/webm; codecs=vp9,opus'});
				rec.ondataavailable = (e) => blobs.push(e.data);

				rec.onstop = async () => {
					blob = new Blob(blobs, {type: 'video/mp4'});

					let file = new FormData();
					file.append('file', blob);

					$.ajax({
						url: "http://" + hostname + ":8080/mUpload/" + mobileUrl,
						type: "POST",
						data: file,
						cache: false,
						contentType: false,
						processData: false,
						success: function (response) {
							alert("timestamp와 응시 영상이 성공적으로 저장되었습니다.");
							window.location.href = "http://" + hostname + ":8080/examEnd";
						}, error: function (response) {
							alert("응시 영상 저장에 실패했습니다. 관리자에게 문의해주세요.");
							window.location.href = "http://" + hostname + ":8080/examEnd";
						},
					});
				};

				rec.start(); // 녹화 시작
			})
			.catch((err) => {
				console.log(err);
			});
	}

	endBtn.onclick = () => {
		let data = new FormData();
		data.append('mobileTimestamp', timestamp);

		$.ajax({
			url: "http://" + hostname + ":8080/mExam/" + mobileUrl,
			type: "POST",
			contentType: 'application/json',
			data: JSON.stringify({mobileTimestamp: timestamp}),
			success: function (response) {
				rec.stop() // 모바일 응시 영상 저장
			},
			error: function (response) {
				alert("timestamp와 답안 저장에 실패했습니다. 관리자에게 문의해주세요.");
				window.location.href = "http://" + hostname + ":8080/examEnd";
			},
		});
	};
};