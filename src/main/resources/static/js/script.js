const user = {
    id: 1,
    name:'Jone',
    email:'jone00@mail.ru',
    age: 2,
    authorized: false
}

/* Я связал post с user при помощи user_id, думаю так будет правильно, для дальнейшего апгрейда проекта, допустим для удобства при добовления в базу*/
const post = {
    id: 1,
    img: 'kitten.jpg',
    postName: 'books',
    description: 'how to read books correctly',
    date: '17.10.2022',
    liked: false,
    likes: 0,
    user: user,
}

const comment = {
    id: 1,
    user_id: 5,
    publication_id: 4,
    commentText: 'Everyone can read',
    date: '17.10.2022',
}

const posts = [];



function addPost(){
    let massiveLenght = posts.length;
    const newPost = {
        id: massiveLenght + 1,
        img: 'kitten.jpg',
        postName: 'books',
        description: 'how to read books correctly',
        date: '17.10.2022',
        liked: false,
        likes: 0,
        user: user,
    }
    posts.push(newPost);
}

function authentication(user){
    if(user.authorized === false){
        user.authorized = true;
    }
}

function liked(post_id){
    for(let i = 0; i < posts.length; i++){
        if(post_id === posts[i].id){
            if(posts[i].liked === false){
                posts[i].liked = true;
                posts[i].likes += 1;
            }else if(posts[i].liked === true){
                posts[i].liked = false;
                posts[i].likes -= 1;
            }

        }
    }
}


authentication(user);
addPost();
addPost();
liked(1);
liked(2);
liked(1);
console.log(posts);
console.log(user);

/* HomeWork58 */

function hide(){
    var splashScreen = document.getElementById("modal");
    splashScreen.style = 'display: none';
}

function show(){
    var splashScreen = document.getElementById("modal");
    splashScreen.style = 'display: block';
}

function createPostElement(post){
    let createPost = document.createElement('div');
    createPost.classList.add('post');
    createPost.setAttribute('id', "post-" + post.id)
    if(post.liked){
        createPost.innerHTML = `<img class="img" src="${post.img}" alt="Post image" id="img-${post.id}"><br>`
            + `<span class="h1 mx-2 text-danger" id="like-${post.id}"><i class="fas fa-heart" id="fas-${post.id}"></i></span>`
            + `<span class="h1 mx-2 text-danger hide-like" id="heart-${post.id}"><i class="fas fa-heart"></i></span>`
            + `<i class="fa-regular fa-message icons-setting" id="message-${post.id}"></i>`
            + `<i class="fa-solid fa-bookmark icons-setting icon-save" id="save-${post.id}"></i>`
            + `<div class="post-comment-${post.id}" hide-comment></div>`
            +   `<div class="comment hide-comment" id="post-comment-${post.id}">
                <form id="comment-form">
                    <input type="number" placeholder="user_id" name="user_id" hidden value="3">
                        <input type="number" placeholder="post_id" name="publication_id" hidden value="${post.id}">
                            <textarea type="text" placeholder="commentText" name="commentText"></textarea>
                            <button class="message-send-button" type="submit">Отправить</button>
                </form>
            </div>`
    }else {
        createPost.innerHTML = `<img class="img" src="${post.img}" alt="Post image" id="img-${post.id}"><br>`
            + `<span class="h1 mx-2 muted" id="like-${post.id}"><i class="far fa-heart" id="fas-${post.id}"></i></span>`
            + `<span class="h1 mx-2 text-danger hide-like" id="heart-${post.id}"><i class="fas fa-heart"></i></span>`
            + `<i class="fa-regular fa-message icons-setting" id="message-${post.id}"></i>`
            + `<i class="fa-solid fa-bookmark icons-setting icon-save" id="save-${post.id}"></i>`
            + `<div class="comment hide-comment" id="post-comment-${post.id}">
                    <div id="post-comments-${post.id}"></div>
                    <form id="comment-form">
                        <input type="number" placeholder="user_id" name="user_id" hidden value="3">
                        <input type="number" placeholder="post_id" name="publication_id" hidden value="${post.id}">
                        <textarea type="text" placeholder="commentText" name="commentText"></textarea>
                        <button class="message-send-button" type="submit">Отправить</button>
                    </form>
                </div>`
    }

    createPost.style.marginTop = '10px';
    return createPost;
}



function commentHandler(e){
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    const object = {}
    data.forEach((value, key) => {
        object[key] = value;
    });
    let commentText = object['commentText'];
    let user_id = object['user_id'];
    let publication_id = object['publication_id'];

    axios.post("http://localhost:8080/comment/create", {commentText, user_id, publication_id})
        .then((response) => {
        console.log(response.data)
        addCommentToHtml(response.data);
    })

}

function createCommentElement(comment){
    let createCommnet = document.createElement('div');
    createCommnet.classList.add('comment');
    createCommnet.innerHTML =
        `<div class="comment-description">Anonim: ${comment.commentText}</div>`
    return createCommnet
}

function addCommentToHtml(comment){
    let commentElement = createCommentElement(comment)
    document.getElementById("post-comments-" + comment.publication_id).prepend(commentElement);
}

function addPostToHtml(post){
    document.getElementById("posts").prepend(createPostElement(post));
    let likePost = document.getElementById('like-' + post.id);

    var clickLike = document.getElementById("like-" + post.id);
    var likeIcon = document.getElementById("fas-" + post.id);
    console.log(clickLike);
    console.log(likeIcon);
    likePost.addEventListener('click', function(){
        if(!clickLike.classList.contains('text-danger')){
            clickLike.classList.remove('muted');
            clickLike.classList.add('text-danger');
            likeIcon.classList.remove('far');
            likeIcon.classList.add('fas');
        }else{
            clickLike.classList.remove('text-danger');
            clickLike.classList.add('muted');
            likeIcon.classList.remove('fas');
            likeIcon.classList.add('far');
        }
    });


    let dblClickImg = document.getElementById('img-' + post.id);
    let getImg = document.getElementById('heart-' + post.id)
    dblClickImg.addEventListener('dblclick', function (){
        getImg.classList.remove('hide-like');
        getImg.classList.add('show-like');
        var milliseconds = 1000;
        setTimeout(function() {
            getImg.classList.remove('show-like');
            getImg.classList.add('hide-like');
        }, milliseconds);

        if(!clickLike.classList.contains('text-danger')){
            clickLike.classList.remove('muted');
            clickLike.classList.add('text-danger');
            likeIcon.classList.remove('far');
            likeIcon.classList.add('fas');
        }else{
            clickLike.classList.remove('text-danger');
            clickLike.classList.add('muted');
            likeIcon.classList.remove('fas');
            likeIcon.classList.add('far');
        }
    });

    let saveNewPost = document.getElementById('save-' + post.id);
    saveNewPost.addEventListener('click', function (){
        if(saveNewPost.classList.contains('savedPost')){
            saveNewPost.classList.remove('savedPost');
        }else{
            saveNewPost.classList.add('savedPost');
        }
    });

    let commentToPost = document.getElementById('message-' + post.id);
    commentToPost.addEventListener('click', function (){
        let showForm = document.getElementById('post-comment-' + post.id)
        if (showForm.classList.contains("hide-comment")){
            showForm.classList.remove('hide-comment');
            showForm.classList.add('show-comment');
            console.log(post.id);

            let parent = document.getElementById("post-comments-" + post.id);
            while (parent.firstChild){
                parent.firstChild.remove();
            }
            loadToAllComment(post.id);
        }else {
            showForm.classList.remove('show-comment');
            showForm.classList.add('hide-comment');
        }

    })
    let commentForm = document.getElementById('comment-form');
    commentForm.addEventListener('submit', commentHandler);
}

let commentsMassive = [];

function loadToAllComment(post_id){
    axios.get("http://localhost:8080/comments/publication/" + post_id).then(
        (response) => {
            var result = response.data;
            console.log(result);
            leadAllComments(result);
        },
        (error) => {
            console.log(error);
        }
    );
}

// addPostToHtml(post);


/* HomeWork59 */

function hide(){
    var splashScreen = document.getElementById("modal");
    splashScreen.style = 'display: none';
}

function show(){
    var splashScreen = document.getElementById("modal");
    splashScreen.style = 'display: block';
}

// addPostToHtml(post);

/* HomeWork60 */

const loginForm = document.getElementById('login-form');
loginForm.addEventListener('submit', loginHandler);

function loginHandler(e){
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    const object = {}
    data.forEach((value, key) => {
        object[key] = value;
    });
    let file = object['img'];
    let description = object['description'];
    let user_id = object['user_id'];
    console.log(file);
    console.log(description);
    console.log(user_id);

    axios.post("http://localhost:8080/", {user_id, description, file}, {
        headers: {
            "Content-Type": "multipart/form-data"
        }

    }).then((response) => {
            console.log(response)
            console.log(response.data)
            addPostToHtml(response.data);
        })

}

/* HomeWork61 */

const getAllPosts = document.getElementsByClassName('posts-add-button')[0];
getAllPosts.addEventListener('click', postsHandler);

function postsHandler(){
    axios.get("http://localhost:8080/publications/").then(
        (response) => {
            var result = response.data;
            loadAllPosts(result);
        },
        (error) => {
            console.log(error);
        }
    );
}

function loadAllPosts(result){
    for (let i = 0; i < result.length; i++){
        addPostToHtml(result[i]);
    }
}



function leadAllComments(result){
    for (let i = 0; i < result.length; i++){
        addCommentToHtml(result[i]);
    }
}

/* HomeWork 62 */

const registrationForm = document.getElementById('registration-form');
registrationForm.addEventListener('submit', onRegisterHandler);


function onRegisterHandler(e) {
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    createUser(data)
}

const baseUrl = 'http://localhost:8080';
async function createUser(userFormData) {
    const userJSON = JSON.stringify(Object.fromEntries(userFormData));
    console.log(userJSON);
    const settings = {
        method: 'POST',
        cache: 'no-cache',
        mode : 'cors',
        headers: {
            'Content-Type': 'application/json'
        },
        body: userJSON
    };

    const response = await fetch(baseUrl + '/users/register/', settings);
    // const responseData = await response.json();
    // console.log(responseData);
}

function hideRegister(){
    let splashScreen = document.getElementById("modal-register");
    splashScreen.style = 'display: none';
}

function showRegister(){
    let splashScreen = document.getElementById("modal-register");
    splashScreen.style = 'display: block';
}




