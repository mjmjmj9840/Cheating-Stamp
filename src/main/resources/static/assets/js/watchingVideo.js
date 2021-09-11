let tmp = {"timestamp": ["22:22:00", "22:22:00", "22:23:00", "22:42:00", "4:22:00", "5:22:00", "21:22:00", ]}  



$(document).ready(function () {
  
  for(let i = 0; i < tmp.timestamp.length; i++){
    $('.stamp').append(
      "<div class='time'>" + "<p class='str'>[ 시선 이탈 ]</p>" + "<p class='num'>" + tmp.timestamp[i] + "</p>" + "</div>"
    );
  }

});
