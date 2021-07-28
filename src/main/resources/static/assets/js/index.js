window.saveDataAcrossSessions = true

webgazer
  .setGazeListener((data, timestamp) => {
    console.log(data, timestamp)
  })
  .begin()
