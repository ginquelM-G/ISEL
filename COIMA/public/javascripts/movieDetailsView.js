window.onload = function(){
 
 
      const details = document.getElementById('comments')
      const txtComment = document.getElementById('txtAddedComment')
      //const btAddComment = document.getElementById('btAddComment')
      const btAddComment = document.querySelector('#bt')
 
      document
        .querySelectorAll('.divAddComment')
        .forEach(div => {
            const btn = div.querySelector('.btn')

            btn.addEventListener('click', () =>{
              alert('Clicked button')
              loadDoc()
            })
        })
     

  function addComment(){
     details.innerHTML += '<br>'+txtComment.value+'  <button onclick="movieDetailsFrontend.AddComment()" class="btn-sm btn-primary">reply</button>'
      // details.innerHTML += '<p>'+txtComment.value+'</p>
  }

  function addReply(){
        details.innerHTML += '<p>'+txtComment.value+'</p>'
  }

  //btAddComment.addEventListener('click', loadDoc)



  function loadDoc() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        document.getElementById("comments").innerHTML +=txtComment.value + '<br>'
        this.responseText;
      }
    };
    xhttp.open("GET", "https://api.themoviedb.org/3/movie/860?api_key=b531994cdfaa8e3b441e4086b1c6756d", true);
    xhttp.send();
  }


}
