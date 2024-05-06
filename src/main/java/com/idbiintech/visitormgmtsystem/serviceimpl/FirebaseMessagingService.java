package com.idbiintech.visitormgmtsystem.serviceimpl;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.idbiintech.visitormgmtsystem.model.Note;


@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }


    public String sendNotification(Note note, String topic,String usertoken) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .setImage(note.getImage())
                .build();
        System.out.println("note-------content----"+note.getContent()+ "note subject ---------"+note.getSubject()+"Image---------"+note.getImage());
        Message message = Message.builder()
       //.setToken("e3Gu2_DcpEnip987witbeV:APA91bHuEiFk-b0WvLm_avOpXvzDomjlYSTVZ6d2mlA1jlafe6IXcNLvXJGkfxYqPZKM0UXK0pvDxEe81_8vw6Aj9G2eZH8EsmyLRutNjzv15BmT0-U8_v6E611xZ5kNjArhYBYE0bE-")
        		.setToken(usertoken)
                //.setTopic(topic)
        		.setNotification(notification)
        		.setApnsConfig(getApnsConfig("visitorNotification","VMNotificationSound1.caf",true))
        		 .setAndroidConfig(getAndroidConfig("visitorNotification","VMNotificationSound1.mp3"))
                .putAllData(note.getData())
                .build();
        return firebaseMessaging.send(message);
    }
    
    
    public String sendNotificationtosecurity(Note note, String topic,String usertoken) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .setImage(note.getImage())
                .build();
        System.out.println("note-------content----"+note.getContent()+ "note subject ---------"+note.getSubject()+"Image---------"+note.getImage());
        Message message = Message.builder()
       //.setToken("e3Gu2_DcpEnip987witbeV:APA91bHuEiFk-b0WvLm_avOpXvzDomjlYSTVZ6d2mlA1jlafe6IXcNLvXJGkfxYqPZKM0UXK0pvDxEe81_8vw6Aj9G2eZH8EsmyLRutNjzv15BmT0-U8_v6E611xZ5kNjArhYBYE0bE-")
        		.setToken(usertoken)
                //.setTopic(topic)
        		.setNotification(notification)
        		.setApnsConfig(getApnsConfig("securityNotification","default",true))
        		 .setAndroidConfig(getAndroidConfig("securityNotification","default"))
                .putAllData(note.getData())
                .build();
        return firebaseMessaging.send(message);
    }

    
    
    public AndroidConfig getAndroidConfig(String category,String sound) {
        return AndroidConfig.builder().setCollapseKey(category)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setClickAction(category).setSound(sound)
                .build()).build();
    }
    
    public ApnsConfig getApnsConfig(String category,String sound,Boolean mutableContent) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(category).setSound(sound).setMutableContent(mutableContent).build()).build();
    }
    
    

}