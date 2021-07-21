var PointCalibrate = 0;
var CalibrationPoints={};

/**
 * Clear the canvas and the calibration button.
 */
function ClearCanvas(){
  $(".Calibration").hide();
  $(".RandomCalibration").hide();
  var canvas = document.getElementById("plotting_canvas");
  canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height);
}

/**
 * Show the instruction of using calibration at the start up screen.
 */
function PopUpInstruction(){
  ClearCanvas();
  swal({
    title:"Calibration",
    text: "Please click on each of the 9 points on the screen. You must click on each point 5 times till it goes yellow. This will calibrate your eye movements.",
    buttons:{
      cancel: false,
      confirm: true
    }
  }).then(isConfirm => {
    ShowCalibrationPoint();
  });

}
/**
  * Show the help instructions right at the start.
  */
function helpModalShow() {
    $('#helpModal').modal('show');
}

/**
 * Load this function when the index page starts.
* This function listens for button clicks on the html page
* checks that all buttons have been clicked 5 times each, and then goes on to measuring the precision
*/
$(document).ready(function(){
  ClearCanvas();
  helpModalShow();
     $(".Calibration").click(function(){ // click event on the calibration buttons

      var id = $(this).attr('id');

      if (!CalibrationPoints[id]){ // initialises if not done
        CalibrationPoints[id]=0;
      }
      CalibrationPoints[id]++; // increments values

      if (CalibrationPoints[id]==5){ //only turn to yellow after 5 clicks
        $(this).css('background-color','yellow');
        $(this).prop('disabled', true); //disables the button
        PointCalibrate++;
      }else if (CalibrationPoints[id]<5){
        //Gradually increase the opacity of calibration points when click to give some indication to user.
        var opacity = 0.2*CalibrationPoints[id]+0.2;
        $(this).css('opacity',opacity);
      }

      //Show the middle calibration point after all other points have been clicked.
      if (PointCalibrate == 8){
        $("#Pt5").show();
      }

      if (PointCalibrate >= 9){ // last point is calibrated
        swal({
          title:"Random Calibration",
          text: "화면에 나타나는 점을 2초내에 클릭해주세요. ",
          buttons:{
            cancel: false,
            confirm: true
          }
        }).then(isConfirm => {
            //using jquery to grab every element in Calibration class and hide them except the middle point.
            $(".Calibration").hide();
            let timerId = setInterval(() => ShowRandomCalibraionPoint(), 2000);
            setTimeout(() => {
              clearInterval(timerId);
              $(".RandomCalibration").hide();
              if (PointCalibrate < 12){
                swal({
                  title:"Pleas Recalibrate.",
                  text: "정확도가 너무 낮습니다. 보정 과정을 다시 진행해주세요. ",
                  buttons:{
                    cancel: false,
                    confirm: true
                  }
                }).then(isConfirm => {
                    // recalibrate
                    webgazer.clearData();
                    ClearCalibration();
                    ClearCanvas();
                    ShowCalibrationPoint();
                });
              } else{
                sleep(2000).then(() => {
                  CalculateMeasurement();
                });
              }
            }, 15000);
        });
      }
    });
});

function ShowRandomCalibraionPoint() {
  // 랜덤 위치에 점 띄우기
  let random_x = Math.floor(Math.random() * 86) + 10;
  let random_y = Math.floor(Math.random() * 73) + 20;
  $(".RandomCalibration").css('top', random_y + '%');
  $(".RandomCalibration").css('left', random_x + '%');
  $(".RandomCalibration").css('background-color', 'red');
  $(".RandomCalibration").show();

  $(".RandomCalibration").click(function(){ // click event on the random calibration button
    PointCalibrate++;
    $(".RandomCalibration").css('background-color', 'yellow');
  });
}

/**
 * Show the Calibration Points
 */
function ShowCalibrationPoint() {
  $(".Calibration").show();
  $("#Pt5").hide(); // initially hides the middle button
}

function CalculateMeasurement() {
  $("#Pt5").show();
  // clears the canvas
  var canvas = document.getElementById("plotting_canvas");
  canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height);

  // notification for the measurement process
  swal({
    title: "Calculating measurement",
    text: "Please don't move your mouse & stare at the middle dot for the next 5 seconds. This will allow us to calculate the accuracy of our predictions.",
    closeOnEsc: false,
    allowOutsideClick: false,
    closeModal: true
  }).then( isConfirm => {
      // makes the variables true for 5 seconds & plots the points
      $(document).ready(function(){

        store_points_variable(); // start storing the prediction points

        sleep(5000).then(() => {
            stop_storing_points_variable(); // stop storing the prediction points
            var past50 = webgazer.getStoredPoints(); // retrieve the stored points
            var precision_measurement = calculatePrecision(past50);
            // var accuracyLabel = "<a>Accuracy | "+precision_measurement+"%</a>";
            // document.getElementById("Accuracy").innerHTML = accuracyLabel; // Show the accuracy in the nav bar.
            // 정확도가 75% 이상이어야 시험 입장 가능
            if(precision_measurement < 65){
              swal({
                title: "Your accuracy measure is " + precision_measurement + "%",
                text: "정확도가 너무 낮습니다. 보정 과정을 다시 진행해주세요. ",
                buttons:{
                  cancel: false,
                  confirm: true
                }
              }).then(isConfirm => {
                // recalibrate
                webgazer.clearData();
                ClearCalibration();
                ClearCanvas();
                ShowCalibrationPoint();
              });
            } else{
              swal({
                title: "Your accuracy measure is " + precision_measurement + "%",
                allowOutsideClick: false,
                buttons: {
                  cancel: false,
                  confirm: true,
                }
              }).then(isConfirm => {
                  location.replace("exam.html");
              });
            }
        });
      });
  });
}

/**
* This function clears the calibration buttons memory
*/
function ClearCalibration(){
  // Clear data from WebGazer

  $(".Calibration").css('background-color','red');
  $(".Calibration").css('opacity',0.2);
  $(".Calibration").prop('disabled',false);

  CalibrationPoints = {};
  PointCalibrate = 0;
}

// sleep function because java doesn't have one, sourced from http://stackoverflow.com/questions/951021/what-is-the-javascript-version-of-sleep
function sleep (time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}
