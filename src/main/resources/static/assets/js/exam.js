timestamp = new Array

setInterval(function()
{
  const alert_ = document.querySelector('.alert-true')

  if (alert_ !== null) {
    let now = new Date();   
  
    let hours = ('0' + now.getHours()).slice(-2); 
    let minutes = ('0' + now.getMinutes()).slice(-2);
    let seconds = ('0' + now.getSeconds()).slice(-2); 
  
    let nowString = hours + ':' + minutes  + ':' + seconds;
  
    timestamp.push(nowString)
    console.log(timestamp)
  }

}, 1000 );
