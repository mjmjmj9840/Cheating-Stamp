// let tmp = {
//   "timestamp": ["00:00:10", "00:00:16", "00:00:20", "00:00:26", "00:00:50", "00:00:54", "00:00:55", ]
// }



$(document).ready(function () {

  // for (let i = 0; i < tmp.timestamp.length; i++) {
  //   $('.stamp').append(
  //     "<div class='time'>" + "<p class='str'>[ 시선 이탈 ]</p>" + "<p class='num'>" + tmp.timestamp[i] + "</p>" + "</div>"
  //   );
  // }

  let timeEls = document.querySelectorAll('.num')

  timeEls.forEach(function (timeEl, index) {
    timeEl.classList.add(`num${index}`)
    timeEl.addEventListener('click', function () {
      let sec = 0;
      sec += parseInt(timeEl.textContent.slice(0, 2)) * 3600
      sec += parseInt(timeEl.textContent.slice(3, 5)) * 60
      sec += parseInt(timeEl.textContent.slice(6))

      let video = document.querySelector('video')
      video.currentTime = sec
      let video2 = document.querySelectorAll('video')[1]
      video2.currentTime = sec
    })
  })

});


