function form_check() {
    var username = document.getElementById("username");
    var password = document.getElementById("password");
    var password_check = document.getElementById("password_check");

    var un_valid = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/; //형식:알파벳+숫자@알파벳+숫자.알파벳+숫자
    if(!un_valid.test(username.value)) {
        alert("올바른 이메일 형식이 아닙니다.");
        username.focus();
        return false;
    }

    var pw_valid = /^.*(?=.{6,20})(?=.*[0-9])(?=.*[a-zA-Z]).*$/; //형식:알파벳+숫자(6~20자리)
    if(!pw_valid.test(password.value)) {
        alert("비밀번호는 영문자+숫자 조합으로 6~20자리 사용해야 합니다.");
        password.focus();
        return false;
    }

    if (password.value != password_check.value) {  //비밀번호 동일여부 확인
      alert("비밀번호가 일치하지 않습니다. 확인해 주세요");
      password_check.focus();
      return false;
    }
}