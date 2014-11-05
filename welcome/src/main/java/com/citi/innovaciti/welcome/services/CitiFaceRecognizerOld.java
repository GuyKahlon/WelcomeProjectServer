/*
package com.citi.innovaciti.welcome.services;


import com.googlecode.javacv.cpp.opencv_contrib;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_highgui;
import org.springframework.stereotype.Service;


@Service
public class CitiFaceRecognizer {


    public static void main(String[] args)  {
        long size=2;
        opencv_core.MatVector imgs=new opencv_core.MatVector(size);
        int[] id={0,0};

        opencv_contrib.FaceRecognizer fr=opencv_contrib.createEigenFaceRecognizer();
        for(int i=0;i<=1;i++)
        {
            String url="C:\\Users\\Liron\\Desktop\\tmp\\images\\training\\1-Liron_"+(i+1)+".jpg";
            opencv_core.IplImage img= opencv_highgui.cvLoadImage(url);
            imgs=imgs.put(i,img);

        }
        fr.train(imgs,id);
        opencv_core.IplImage testImage=opencv_highgui.cvLoadImage("C:\\Users\\Liron\\Desktop\\tmp\\images\\test\\test.JPG");
        opencv_core.CvMat mat= testImage.asCvMat();
        int val=fr.predict(mat);
        System.out.println(val);
    }


   // public static void main(String[] args) {
       // FaceRecognizer fr=opencv_contrib.createEigenFaceRecognizer();

//        String trainingDir = "C:\\Users\\Liron\\Desktop\\tmp\\images\\training";
//        IplImage testImage = cvLoadImage("C:\\Users\\Liron\\Desktop\\tmp\\images\\test\\test.JPG");
//
//        File root = new File(trainingDir);
//
//        FilenameFilter pngFilter = new FilenameFilter() {
//            public boolean accept(File dir, String name) {
//                return name.toLowerCase().endsWith(".JPG");
//            }
//        };
//
//        File[] imageFiles = root.listFiles(pngFilter);
//
//        MatVector images = new MatVector(imageFiles.length);
//
//        int[] labels = new int[imageFiles.length];
//
//        int counter = 0;
//        int label;
//
//        IplImage img;
//        IplImage grayImg;
//
//        for (File image : imageFiles) {
//            img = cvLoadImage(image.getAbsolutePath());
//
//            label = Integer.parseInt(image.getName().split("\\-")[0]);
//
//            grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);
//
//            cvCvtColor(img, grayImg, CV_BGR2GRAY);
//
//            images.put(counter, grayImg);
//
//            labels[counter] = label;
//
//            counter++;
//        }
//
//        IplImage greyTestImage = IplImage.create(testImage.width(), testImage.height(), IPL_DEPTH_8U, 1);
//
//        FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
//        // FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
//        // FaceRecognizer faceRecognizer = createLBPHFaceRecognizer()
//
//        faceRecognizer.train(images, labels);
//
//        cvCvtColor(testImage, greyTestImage, CV_BGR2GRAY);
//
//        int predictedLabel = faceRecognizer.predict(greyTestImage);
//
//        System.out.println("Predicted label: " + predictedLabel);
       // printList();
    //}




}



*/
