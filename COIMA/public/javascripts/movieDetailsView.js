function documentControl() {
  var id = 0
  const details = document.getElementById('comments')
  const inputTxtComment = document.getElementById('inputTxtComment')
  const txtCommented = document.getElementById('txtAdded')

  const submitBt = document.getElementById('submitBt')
  submitBt.addEventListener('click', () =>{
    loadDoc();
  })

  /*  document
     .querySelectorAll('.divBtComment')
     .forEach(div => {
       const btn = div.querySelector('.btn')
       btn.addEventListener('click', () => {
         alert('Clicked button')
         loadDoc()
       })
 
       div.querySelectorAll('.media-body').forEach(d => {
         const btReply = d.querySelector('.btnReplyI')
         const btReplyB = d.querySelector('#btnReplyB')
 
         btReply.addEventListener('click', () => {
           alert('ReplyI')
           document.getElementById("txtAdded").innerHTML = 'new comment'
           //loadDoc()
         })
         // btReplyB.addEventListener('click', () =>{
         //   alert('ReplyB')
         //   //loadDoc()
         // })
       })
 
     }) */
}



function UserComment(dataObj){
  this.user = dataObj.user
  this.postDate = dataObj.postDate
  this.addedComment = dataObj.addedComment
  this.reply = dataObj.reply | []
}

function toString(id) { return document.getElementById(id).innerHTML }

function addComment() {
  details.innerHTML += '<br>' + txtComment.value + '  <button onclick="movieDetailsFrontend.AddComment()" class="btn-sm btn-primary">reply</button>'
  // details.innerHTML += '<p>'+txtComment.value+'</p>
}

function getUserName(){
  return getElementById('userName').innerHTML.toString()
}

function getPostDate(){
  //var datetime = document.getElementById("postDate").innerHTML = Date().toString().slice(0, 25)//'Posted on January 03, 2017'
  var datetime = Date().toString().slice(0, 25)//'Posted on January 03, 2017'
  return datetime
}

function getAddedComment(){
  return document.getElementById("txtAreaComment").value
}

function setAddedComment(){
  return document.getElementById("txtAreaComment").value
}

function getReply(){
  return []
}

function addReply() {
  details.innerHTML += '<p>' + txtComment.value + '</p>'
}

function incrementID() {
  id++
  document.getElementById("divAddComment").outerHTML = document.getElementById("divAddComment").outerHTML.replace("postDate", 'postDate' + id)
  document.getElementById("divAddComment").outerHTML = document.getElementById("divAddComment").outerHTML.replace("txtAdded", 'txtAdded' + id)
  document.getElementById("divAddComment").outerHTML = document.getElementById("divAddComment").outerHTML.replace("divAddComment", 'divAddComment' + id)
}

function showComment() {
  var commentAdded = getAddedComment()
  document.getElementById("addComment").innerHTML = commentAdded
  document.getElementById("postDate").innerHTML = getPostDate()


  var commentStructure = document.getElementById("divAddComment")
  //commentStructure.style.visibility = "none"
  commentStructure = commentStructure.outerHTML.replace("hidden", "none")
 
 
  console.log(document.getElementById("postDate").innerHTML)
  document.getElementById("allComments").innerHTML += commentStructure
  //console.log(document.getElementById("html").outerHTML)
}

function showComment2(movieId, comments) {
  var commentAdded = getAddedComment()
  document.getElementById("addComment").innerHTML = commentAdded
  document.getElementById("postDate").innerHTML = getPostDate()

  
  var commentStructure = document.getElementById("divAddComment")
  //commentStructure.style.visibility = "none"
  commentStructure = commentStructure.outerHTML.replace("hidden", "none")
 
 
  console.log(document.getElementById("postDate").innerHTML)
  document.getElementById("allComments").innerHTML += commentStructure
  //console.log(document.getElementById("html").outerHTML)
}



function loadDoc() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      //var temp = document.getElementById('divAddComment').outerHTML
      //document.getElementById("userName").innerHTML = 'USer'
      alert(this.responseText)

     
      var commentsByJson = JSON.parse(this.response)
      console.log(commentsByJson)
      console.log(JSON.parse(this.response).comment)
      var comments1 = new UserComment(JSON.parse(this.response).comment);
      console.log(comments1)
      showComment()
      
      console.log("###################\n\n" + this.responseText)
      

      //document.getElementById("txtAdded").innerHTML = inputTxtComment.value + '<br>'
      //const visibleComment =  document.getElementById('divAddComment'+id).outerHTML.replace("hidden", "none")
      //alert(userService.getUserName)
     // var showComment = document.getElementById('divAddComment').outerHTML.replace("hidden", "none")
     // document.getElementById("divAddCommentEmpty").innerHTML += showComment

    
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
