/*
package com.citi.innovaciti.welcome.services;

import com.citi.innovaciti.welcome.daos.GuestDao;
import com.citi.innovaciti.welcome.domain.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

*/
/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 21/08/14
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 *//*

@Service
public class FaceRecognizerService {



    private static final String trainingDBPath = "C:/Users/Liron/welcomeProject2/WelcomeProjectServer/faceRecognition/facedata.xml";

    private FaceRecognition faceRecognition;

    @Autowired
    private GuestDao guestDao;


   @PostConstruct
    private void init() {
        faceRecognition = new FaceRecognition(trainingDBPath, guestDao.getAllGuests());
    }

    public void trainModel(List<Guest> allGuests) {
        faceRecognition.learn(allGuests);

    }

    public FaceRecognitionResult getRecognizedGuest(String imgPath){
       return faceRecognition.getNearestRecognition(imgPath);
    }




}
*/
