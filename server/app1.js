const express = require('express'); //requires express module
const socket = require('socket.io'); //requires socket.io module
const fs = require('fs');
const app = express();
var PORT = process.env.PORT || 3000;
const server = app.listen(PORT); //tells to host server on localhost:3000


//Playing variables:
app.use(express.static('public')); //show static files in 'public' directory
console.log('Server is running');
const io = socket(server);


const users = {}

//Socket.io Connection------------------
io.on('connection', (socket) => {
    
    console.log("New socket connection: " + socket.id)
    var username =''
    var roomname=''    
    socket.on('user',(userdata)=>{
     const data =JSON.parse(userdata);
     username =data.name
     users[socket.id]=username
      roomname =data.room
     console.log(data.name)
     socket.join(`${roomname}`)
     console.log(`${username} has joined the room : ${roomname}`)
    io.to(`${roomname}`).emit('user',JSON.stringify(username));
    io.to(`${roomname}`).emit('connection',users[socket.id]+" has joined the chat")
    })
        socket.on('chat message', (msg)  => {
           // io.emit('chat message', users[socket.id]+" : "+msg);
          const mydata = JSON.parse(msg);
          const chat = mydata.chat

          const send = {
            username:users[socket.id],
            chat:chat
          }
          socket.broadcast.to(`${roomname}`).emit('chat message', JSON.stringify(send))
          console.log('message: '+roomname+"-"+ users[socket.id]+" : "+chat);
        });
      
    
})