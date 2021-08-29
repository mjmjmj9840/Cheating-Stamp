/*체크박스 선택 및 버튼 활성화*/

$(document).ready(function(){

  //전체 체크 클릭 시, 나머지 체크 
  $("#select-all").click(function(){
      var selectAll=$("#select-all").prop("checked");
  
      if(selectAll){
          $(".select").prop("checked",true);
          $(".delete").css({"backgroundColor":"#e96b56","cursor":"pointer"}).prop("disabled",false);
      }
      else{
          $(".select").prop("checked",false);
          $(".delete").css({"backgroundColor":"#ee8b7a","cursor":"auto"}).prop("disabled",true);
      }
  });
  
  // 모든 체크박스를 클릭하면 버튼 활성화시키기
  $('.select').click(function(){
      var tmpp = $(this).prop('checked'); 
      //자식 체크 전체 체크시, 부모 체크박스 체크 됨
      var tt = $(".select").length;
      var ss = $(".select:checked").length;
      
      //선택한 체크박스 값이 true 이거나 체크박스 1개 이상 체크시 버튼 활성화시키기
      if(tmpp==true || ss>0){
      $(".delete").css({"backgroundColor":"#e96b56","cursor":"pointer"}).prop("disabled",false);
      }
      else{
      $(".delete").css({"backgroundColor":"#ee8b7a","cursor":"auto"}).prop("disabled",true);
      }
      
      
      // 체크박스가 모두 선택되었을 때 상위 체크박스 선택되도록 설정
      if(tt == ss){
        $("#select-all").prop("checked",true);
      }else{
        $("#select-all").prop("checked",false);
      }
      
    });
    
    
});


/*삭제 시 비밀번호 묻는 팝업(임의)*/
function password() {
  var testV = 1;
  var pass1 = prompt('삭제 시 암호가 필요합니다. (임시암호=abc123)','암호를 입력하세요'); // 초기시 암호 물어보는 멘트
  
  while (testV < 3) {
    if (!pass1) history.go(-1);
    if (pass1.toLowerCase() == "abc123") { // 암호지정, 임의암호=abc123
      alert('삭제되는 경고문입니다.'); // 암호가 맞았을때 나오는 멘트
      // window.open('http://'); // 이동할 웹페이지 지정 - 새창으로 뜰때
      // location.href ='http://'; // 이동할 웹페이지 지정 - 현재창에서 이동
      break;
    } 
    testV+=1;
    var pass1 = prompt('암호가 틀리면 삭제할 수 없습니다! (3번까지 시도가능)','암호 확인'); // 암호가 틀렸을때 멘트
  }

  if (pass1.toLowerCase()!="password" & testV ==3)  history.go(-1);

  return " ";
} 