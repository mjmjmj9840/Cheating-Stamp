let tzoffset = (new Date()).getTimezoneOffset() * 60000; 

        let localISOTime = (new Date(Date.now()-tzoffset)).toISOString().substring(0, 16);
        document.querySelector('#examStartTime').value= localISOTime;
        document.querySelector('#examEndTime').value= localISOTime;

        let id = 1;
        let id2 = 1;
        $(document).ready (function () {                
            $('.btnAdd').click (function () {                                        
                $('.buttons').append (                        
                    "<span class='Qnum'></span><div class='question'><textarea class='QA' type='text' name='examQuestion"+id+"' onkeydown='resize(this)' onkeyup='resize(this)'></textarea> <input type='button' class='btnRemove' value='-'></div>"
                );
                id2 = 1;
                $('.buttons').find('span').each(function(){
                    $(this)[0].innerText = `Q${id2}`
                    id2 += 1;
                });

                id += 1;                            
                $('.btnRemove').on('click', function () { 
                    $(this).prev().parent().prev().remove ();
                    $(this).prev().parent().remove ();
                    $(this).prev().remove (); // remove the textbox
                    $(this).remove (); // remove the button

                    id = 1
                    $('.buttons').find('div').each(function(){
                        $(this)[0].children[0].name = `examQuestion${id}`
                        id += 1
                    });
                    id2 = 1;
                    $('.buttons').find('span').each(function(){
                        $(this)[0].innerText = `Q${id2}`
                        id2 += 1;
                    });
                });
            });                                          
        });

        function resize(obj) {
            obj.style.height = "1px";
            obj.style.height = (12+obj.scrollHeight)+"px";
        }