let qrcode = new QRCode("qrcode");

function makeCode () {    
  let elText = document.getElementById("text");

  let mobileUrl = $('#mobileUrl').text();
  console.log(mobileUrl);

  elText.value = `https://localhost:8080/m?code=${mobileUrl}`;
  
  qrcode.makeCode(elText.value);
}

makeCode();

// 대기화면으로 이동
$('#target_btn').on('click', function () {
    window.location.href = "/waiting";
});