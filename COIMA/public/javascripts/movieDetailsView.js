//const userService = require('../../services/userService')

 function getUserName(){
  alert(window.localStorage.getItem('username'))
  
  // alert('1')
 //const userService = require('/services/userService')
 var myScript = document.createElement('script'); // Create new script element
 myScript.type = 'text/javascript'; // Set appropriate type
 myScript.src = '../../services/userService.js'; // Load javascript file
 //alert(myScript.getUserName)

  return userService.getUserName()

}

//var name =getUserName()


 function documentControl() {
      var id = 0
      const details = document.getElementById('comments')
      const inputTxtComment = document.getElementById('inputTxtComment')
      const txtCommented = document.getElementById('txtAdded')
  

      document
        .querySelectorAll('.divBtComment')
        .forEach(div => {
            const btn = div.querySelector('.btn')
            btn.addEventListener('click', () =>{
              alert('Clicked button')
              loadDoc()
            })

            div.querySelectorAll('.media-body').forEach(d => {
              const btReply = d.querySelector('.btnReplyI')
              const btReplyB = d.querySelector('#btnReplyB')
             
              btReply.addEventListener('click', () =>{
                alert('ReplyI')
                document.getElementById("txtAdded").innerHTML = 'new comment'
                //loadDoc()
              })
              // btReplyB.addEventListener('click', () =>{
              //   alert('ReplyB')
              //   //loadDoc()
              // })
            })
          
            alert('end')
        })
      }
    
  function toString(id){return document.getElementById(id).innerHTML}

  function addComment(){
     details.innerHTML += '<br>'+txtComment.value+'  <button onclick="movieDetailsFrontend.AddComment()" class="btn-sm btn-primary">reply</button>'
      // details.innerHTML += '<p>'+txtComment.value+'</p>
  }

  function addReply(){
        details.innerHTML += '<p>'+txtComment.value+'</p>'
  }

  function incrementID(){
    id++
    document.getElementById("divAddComment").outerHTML= document.getElementById("divAddComment").outerHTML.replace("postDate", 'postDate'+id)
    document.getElementById("divAddComment").outerHTML= document.getElementById("divAddComment").outerHTML.replace("txtAdded", 'txtAdded'+id)
    document.getElementById("divAddComment").outerHTML= document.getElementById("divAddComment").outerHTML.replace("divAddComment", 'divAddComment'+id)
  }




  function loadDoc() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        var temp = document.getElementById('divAddComment').outerHTML
        //document.getElementById("userName").innerHTML = 'USer'
       
        document.getElementById("postDate").innerHTML = Date().toString().slice(0,25)//'Posted on January 03, 2017'
        document.getElementById("txtAdded").innerHTML = inputTxtComment.value + '<br>'
        //const visibleComment =  document.getElementById('divAddComment'+id).outerHTML.replace("hidden", "none")
        //alert(userService.getUserName)
        var showComment = document.getElementById('divAddComment').outerHTML.replace("hidden", "none")
        document.getElementById("divAddCommentEmpty").innerHTML +=  showComment
       
        alert(this.responseText)
      }
    }
    xhttp.open("GET", "http://127.0.0.1:5984/movies/allComments", true)
    //xhttp.open("GET", "https://api.themoviedb.org/3/movie/860?api_key=b531994cdfaa8e3b441e4086b1c6756d", true);
    // xhttp.setRequestHeader('Access-Control-Allow-Origin', "*");
    // xhttp.setRequestHeader('Access-Control-Allow-Methods','GET,PUT,POST,DELETE');
    // xhttp.setRequestHeader('Access-Control-Allow-Headers', 'Content-Type');
    //alert(xhttp.getAllResponseHeaders())
    xhttp.send();
  }


window.onload = documentControl
