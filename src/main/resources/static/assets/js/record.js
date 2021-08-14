window.onload = () => {
    const startBtn = document.getElementById('startBtn');
    const stopBtn = document.getElementById('stopBtn');
    const download = document.getElementById('download');

    let blobs;
    let blob; // 데이터
    let rec; // 스트림을 기반으로 동작하는 mediarecorder 객체
    let stream; // 통합
    // let voiceStream; // 오디오스트림
    let desktopStream; // 비디오스트림

    // const mergeAudioStreams = (desktopStream, voiceStream) => { // 비디오, 오디오스트림 연결
    //     const context = new AudioContext();
    //     const destination = context.createMediaStreamDestination();
    //     let hasDesktop = false;
    //     let hasVoice = false;
    //     if (desktopStream && desktopStream.getAudioTracks().length > 0) {
    //         const source1 = context.createMediaStreamSource(desktopStream);
    //         const desktopGain = context.createGain();
    //         desktopGain.gain.value = 0.7;
    //         source1.connect(desktopGain).connect(destination);
    //         hasDesktop = true;
    //     }
    //
    //     if (voiceStream && voiceStream.getAudioTracks().length > 0) {
    //         const source2 = context.createMediaStreamSource(voiceStream);
    //         const voiceGain = context.createGain();
    //         voiceGain.gain.value = 0.7;
    //         source2.connect(voiceGain).connect(destination);
    //         hasVoice = true;
    //     }
    //
    //     return (hasDesktop || hasVoice) ? destination.stream.getAudioTracks() : [];
    // };

    startBtn.onclick = async () => { // 녹화 시작 버튼을 누른 경우

        desktopStream = await navigator.mediaDevices.getDisplayMedia({ video: { width: 720 , height: 480 }, audio: false }); // 비디오스트림 생성
        // voiceStream = await navigator.mediaDevices.getUserMedia({ video: false, audio: true }); // 오디오스트림 생성

        const tracks = [
            ...desktopStream.getVideoTracks(),
        ];

        stream = new MediaStream(tracks);

        blobs = [];

        rec = new MediaRecorder(stream, {mimeType: 'video/webm; codecs=vp9,opus'}); // mediaRecorder객체 생성
        rec.ondataavailable = (e) => blobs.push(e.data);
        rec.onstop = async () => {
            blob = new Blob(blobs, {type: 'video/mp4'});
            let url = window.URL.createObjectURL(blob);
            download.href = url;
            download.download = 'test.mp4';
            download.style.display = 'block';
        };
        startBtn.disabled = true; // 시작 버튼 비활성화
        stopBtn.disabled = false; // 종료 버튼 활성화
        rec.start(); // 녹화 시작
    };

    stopBtn.onclick = () => { // 종료 버튼을 누른 경우
        // 버튼 비활성화
        startBtn.disabled = true;
        stopBtn.disabled = true;

        rec.stop(); // 화면녹화 종료 및 녹화된 영상 다운로드

        desktopStream.getTracks().forEach(s=>s.stop())
        // voiceStream.getTracks().forEach(s=>s.stop())
        desktopStream = null;
        // voiceStream = null;

        startBtn.disabled = false; // 시작 버튼 활성화
    };
};
