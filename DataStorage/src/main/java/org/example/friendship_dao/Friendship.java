//package org.example.friendship_dao;
//
//import jakarta.persistence.*;
//import org.example.user_dao.User;
//
//@Entity
//@Table(name = "friendships")
//public class Friendship {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "friend_id")
//    private User friend;
//
//    public Friendship(){}
//
//    public void setUserLogin(User user){
//        this.user = user;
//    }
//
//    public void setFriendLogin(User friend){
//        this.friend = friend;
//    }
//
//    public User getUserLogin(){
//        return user;
//    }
//
//    public User getFriendLogin(){
//        return friend;
//    }
//}
