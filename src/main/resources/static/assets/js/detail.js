let testerEmails=[];
let supervisorEmails=[];
let supervisors = [];
let testers = [];


      function saveEmailS() {

        let jsonArray = new Array();
        var emailNumS = supervisors.length  // 감독관 email 수
        for (let i = 0; i < emailNumS; i++) {
            let key = "S" + (i+1);
            let jsonObj = new Object();
            jsonObj[key] = supervisors[i];
            jsonObj = JSON.stringify(jsonObj);  // {S1:"email1"}
            jsonArray.push(JSON.parse(jsonObj));
        }

        return jsonArray
      }
      function saveEmailT() {

        let jsonArray = new Array();
        var emailNumS = testers.length  // 수험자 email 수
        for (let i = 0; i < emailNumS; i++) {
            let key = "T" + (i+1);
            let jsonObj = new Object();
            jsonObj[key] = testers[i];
            jsonObj = JSON.stringify(jsonObj);  // {T1:"email1"}
            jsonArray.push(JSON.parse(jsonObj));
        }

        return jsonArray
      }


      function clearInput(){

        let email = document.getElementById("supervisorEmail");
        email.value = null;
        email = document.getElementById("testerEmail");
        email.value = null;

      }
      let btn = document.querySelector('.removeBtn')
      if(btn != null){
        btn.addEventListener("click", function() {
      })
      }

      let id = 0;
      let id2 = 0;

      $(document).ready(function () {


        let tmp = {
                    "supervisors": ["ab@ab", "bc@bc"], 
                    "testers": ["aa@aa", "bb@bb"],
                  }  

        for (let i = 0; i < tmp.supervisors.length; i++) {
          $('.screenS').append(
            "<div class='" + id + "'>" + tmp.supervisors[i] + "<button class='removeBtnS' >-</button></div>"
          );
          supervisorEmails.push(tmp.supervisors[i]);
          id += 1;
        }
        
        for (let i = 0; i < tmp.testers.length; i++) {
          $('.screenT').append(
            "<div class='" + id2 + "'>" + tmp.testers[i] + "<button class='removeBtnT' >-</button></div>"
          );
          testerEmails.push(tmp.testers[i]);
          id2 += 1;
        }

        let test = $(".screenS").val();
        console.log(test)
        

        $('.removeBtnS').on('click', function () {
              let x = $(this).parent()[0].className
              $(this).parent()[0].remove()
              supervisorEmails.splice(x, 1, null);

        });
        $('.removeBtnT').on('click', function () {
              let x = $(this).parent()[0].className
              $(this).parent()[0].remove()
              testerEmails.splice(x, 1, null);

        });

        $('.addBtnS').click(function () {
          let email = document.getElementById("supervisorEmail").value;
          if(email == "") return
          supervisorEmails.push(email);

          clearInput();

          $('.screenS').append(
            "<div class='" + id + "'>" + email + "<button class='removeBtnS' >-</button></div>"
          );
          $('.addBox').append(
            document.getElementById("supervisorEmail").value
          );
          
          id += 1;
        
          $('.removeBtnS').on('click', function () {
              let x = $(this).parent()[0].className
              $(this).parent()[0].remove()
              supervisorEmails.splice(x, 1, null);

            });
        });

        $('.addBtnT').click(function () {
          let email = document.getElementById("testerEmail").value;
          if(email == "") return
          testerEmails.push(email);

          clearInput();

          $('.screenT').append(
            "<div class='" + id2 + "'>" + email + "<button class='removeBtnT' >-</button></div>"
          );
          $('.addBox').append(
            document.getElementById("testerEmail").value
          );
          
          id2 += 1;
        
          $('.removeBtnT').on('click', function () {
              let x = $(this).parent()[0].className
              $(this).parent()[0].remove()
              testerEmails.splice(x, 1, null);

            });
        });
      });

      $("#submit-btn").click(function () {
        let i;
        supervisors = [];
        testers = [];

        let screenS = document.querySelector('.screenS');
        let screenT = document.querySelector('.screenT');

        for(i = 0; i<screenS.children.length; i++){
          let tmp = screenS.children[i].className
          supervisors.push(supervisorEmails[tmp]); //supervisors[]에 감독관 이메일 목록 저장
        }

        for(i = 0; i<screenT.children.length; i++){
          let tmp = screenT.children[i].className
          testers.push(testerEmails[tmp]); //testers[]에 수험자 이메일 목록 저장
        }

        let data = new FormData();
        data.append('supervisors', JSON.stringify(saveEmailS()));
        data.append('testers', JSON.stringify(saveEmailT()));

        console.log(data.get("supervisors"));
        console.log(data.get("testers"));

/*       
        // 감독관 / 수험자 목록 저장
        $.ajax({
          url: "/detailExam",
          type: "POST",
          processData: false,
          contentType: "application/json",
          data: data,
          success: function (response) {
            alert("감독관과 수험자가 성공적으로 저장되었습니다.");
            window.location.href = '/detailExam';
          },
          error: function (response) {
            alert("저장에 실패했습니다. 관리자에게 문의해주세요.");
            window.location.href = '/detailExam';
          },
        });
*/

    });
