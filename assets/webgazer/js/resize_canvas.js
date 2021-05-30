/**
* This function occurs on resizing the frame
* clears the canvas & then resizes it (as plots have moved position, can't resize without clear)
*/
function resize() {
    var canvas = document.getElementById('plotting_canvas');
    var context = canvas.getContext('2d');
    context.clearRect(0, 0, canvas.width, canvas.height);
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    if (canvas.width <= 768 || canvas.height <= 500) {
        $('#warning-message').css('display', 'block');
        $(".Calibration").hide();
    }
    else {
        $('#warning-message').css('display', 'none');
        $(".Calibration").show();
    }

};
window.addEventListener('resize', resize, false);
