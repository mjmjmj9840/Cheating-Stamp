window.onload = async () => {
    const stopBtn = document.getElementById('end-btn');

    let blobs;
    let blob; // 데이터
    let rec; // 미디어스트림 기반 Media Recorder 객체
    let stream; // 미디어스트림
    let videoStream; // 비디오스트림


    videoStream = await navigator.mediaDevices.getDisplayMedia({video: {width: 720, height: 480}, audio: false});

    const tracks = [
        ...videoStream.getVideoTracks(),
    ];

    stream = new MediaStream(tracks);

    blobs = [];

    rec = new MediaRecorder(stream, {mimeType: 'video/webm; codecs=vp9,opus'});
    rec.ondataavailable = (e) => blobs.push(e.data);
    
    rec.onstop = async () => { // 녹화 종료시 영상 파일 만들고 서버로 전송
        blob = new Blob(blobs, {type: 'video/mp4'});
        let form = new FormData();
        form.append('file', blob);
        $.ajax({
            url: "/upload",
            type: "POST",
            data: form,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response) {
                alert("응시 영상이 성공적으로 저장되었습니다.");
                window.location.href = '/examEnd'
            }, error: function (response) {
                alert("응시 영상 저장에 실패했습니다. 관리자에게 문의해주세요.");
                window.location.href = '/examEnd'
            },
        });
    };
    
    rec.start(); // 녹화 시작

    stopBtn.onclick = () => { // 끝내기 버튼을 누른 경우
        rec.stop(); // 화면녹화 종료
    };
};
