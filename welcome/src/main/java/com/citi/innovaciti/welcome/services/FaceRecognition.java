package com.citi.innovaciti.welcome.services;/*
 * FaceRecognition.java
 *
 * Created on Dec 7, 2011, 1:27:25 PM
 *
 * Description: Recognizes faces.
 *
 * Copyright (C) Dec 7, 2011, Stephen L. Reed, Texai.org. (Fixed April 22, 2012, Samuel Audet)
 *
 * This file is a translation from the OpenCV example http://www.shervinemami.info/faceRecognition.html, ported
 * to Java using the JavaCV library.  Notable changes are the addition of the Java Logging framework and the
 * installation of image files in a data directory child of the working directory. Some of the code has
 * been expanded to make debugging easier.  Expected results are 100% recognition of the lower3.txt test
 * image index set against the all10.txt training image index set.  See http://en.wikipedia.org/wiki/Eigenface
 * for a technical explanation of the algorithm.
 *
 * stephenreed@yahoo.com
 *
 * FaceRecognition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version (subject to the "Classpath" exception
 * as provided in the LICENSE.txt file that accompanied this code).
 *
 * FaceRecognition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JavaCV.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import com.citi.innovaciti.welcome.domain.Guest;
import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.cpp.opencv_legacy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

//import static org.bytedeco.javacpp.opencv_core.*;
//import static org.bytedeco.javacpp.opencv_highgui.*;

/**
 * Recognizes faces.
 *
 * @author reed
 */
public class FaceRecognition {

    /**
     * the logger
     */
    private static final Logger LOGGER = Logger.getLogger(FaceRecognition.class.getName());
    /**
     * the number of training faces
     */
    private int nTrainFaces = 0;
    /**
     * the training face image array
     */
    IplImage[] trainingFaceImgArr;

    /**
     * the person number array
     */
    CvMat personNumTruthMat;
    /**
     * the number of persons
     */
    int nPersons;
    /**
     * the person names
     */
    final List<String> personNames = new ArrayList<String>();
    /**
     * the number of eigenvalues
     */
    int nEigens = 0;
    /**
     * eigenvectors
     */
    IplImage[] eigenVectArr;
    /**
     * eigenvalues
     */
    CvMat eigenValMat;
    /**
     * the average image
     */
    IplImage pAvgTrainImg;
    /**
     * the projected training faces
     */
    CvMat projectedTrainFaceMat;


    String trainingDBPath;

    //CvMat trainDataMat;


    public static final String rootDir = "C:/Users/Liron/welcomeProject2/WelcomeProjectServer/welcome/src/main/resources/";

    /**
     * Constructs a new FaceRecognition instance.
     */
    public FaceRecognition(String trainingDBPath, List<Guest> guests) {
        this.trainingDBPath = trainingDBPath;
        loadTrainingData();
        learn(guests);

    }

    /**
     * Trains from the data in the given training text index file, and store the trained data into the file trainingDBPath.
     */
    public void learn(List<Guest> allGuests) {
        int i;

        // load training data
        LOGGER.info("===========================================");
        trainingFaceImgArr = loadFaceImgArray(allGuests);
        nTrainFaces = trainingFaceImgArr.length;
        LOGGER.info("Got " + nTrainFaces + " training images");
        if (nTrainFaces < 3) {
            LOGGER.severe("Need 3 or more training faces\n"
                    + "Input file contains only " + nTrainFaces);
            return;
        }

        // do Principal Component Analysis on the training faces
        doPCA();

        LOGGER.info("projecting the training images onto the PCA subspace");
        // project the training images onto the PCA subspace
        projectedTrainFaceMat = cvCreateMat(
                nTrainFaces, // rows
                nEigens, // cols
                CV_32FC1); // type, 32-bit float, 1 channel

        // initialize the training face matrix - for ease of debugging
        for (int i1 = 0; i1 < nTrainFaces; i1++) {
            for (int j1 = 0; j1 < nEigens; j1++) {
                projectedTrainFaceMat.put(i1, j1, 0.0);
            }
        }

        LOGGER.info("created projectedTrainFaceMat with " + nTrainFaces + " (nTrainFaces) rows and " + nEigens + " (nEigens) columns");
        if (nTrainFaces < 5) {
            LOGGER.info("projectedTrainFaceMat contents:\n" + oneChannelCvMatToString(projectedTrainFaceMat));
        }

        final FloatPointer floatPointer = new FloatPointer(nEigens);
        for (i = 0; i < nTrainFaces; i++) {
            opencv_legacy.cvEigenDecomposite(
                    trainingFaceImgArr[i], // obj
                    nEigens, // nEigObjs
                    eigenVectArr, // eigInput (Pointer)
                    0, // ioFlags
                    null, // userData (Pointer)
                    pAvgTrainImg, // avg
                    floatPointer); // coeffs (FloatPointer)

            if (nTrainFaces < 5) {
                LOGGER.info("floatPointer: " + floatPointerToString(floatPointer));
            }
            for (int j1 = 0; j1 < nEigens; j1++) {
                projectedTrainFaceMat.put(i, j1, floatPointer.get(j1));
            }
        }
        if (nTrainFaces < 5) {
            LOGGER.info("projectedTrainFaceMat after cvEigenDecomposite:\n" + projectedTrainFaceMat);
        }

        // store the recognition data as an xml file
        storeTrainingData();

        // Save all the eigenvectors as images, so that they can be checked.
        storeEigenfaceImages();
    }

    public FaceRecognitionResult getNearestRecognition(String imageToRecognize) {

        LOGGER.info("recognizing faces indexed from " + imageToRecognize);
        //CvMat trainPersonNumMat;  // the person numbers during training
        float[] projectedTestFace;
        float confidence = 0.0f;

        // load test images and ground truth for person number

        IplImage iplImage =
                cvLoadImage(
                        imageToRecognize, // filename
                        CV_LOAD_IMAGE_GRAYSCALE); // isColorv


        // load the saved training data
        //trainPersonNumMat = loadTrainingData();
       /* if (personNumTruthMat == null) {
            return null;
        }*/

        if (projectedTrainFaceMat== null) {
            return null;
        }

        // project the test images onto the PCA subspace
        projectedTestFace = new float[nEigens];

        int iNearest;
        int nearest;

        // project the test image onto the PCA subspace
        opencv_legacy.cvEigenDecomposite(
                iplImage, // obj
                nEigens, // nEigObjs
                eigenVectArr, // eigInput (Pointer)
                0, // ioFlags
                null, // userData
                pAvgTrainImg, // avg
                projectedTestFace);  // coeffs


        final FloatPointer pConfidence = new FloatPointer(confidence);
        iNearest = findNearestNeighbor(projectedTestFace, new FloatPointer(pConfidence));
        confidence = pConfidence.get();
        nearest = personNumTruthMat.data_i().get(iNearest);

        LOGGER.info("nearest = " + nearest + ". Confidence = " + confidence);

        return new FaceRecognitionResult(nearest, confidence);

    }

    /**
     * Reads the names & image filenames of people from a text file, and loads all those images listed.
     *
     * @return the face image array
     */
    private IplImage[] loadFaceImgArray(List<Guest> allGuests) {
        IplImage[] faceImgArr;
        String imgFilename;
        int i;

        LOGGER.info("number of guests: " + allGuests.size());

        if (allGuests.size() == 0) {
            return new IplImage[0];
        }

        // allocate the face-image array and person number matrix

        int numOfPics = allGuests.size() * 3;
        faceImgArr = new IplImage[numOfPics];
        personNumTruthMat = cvCreateMat(
                1, // rows
                numOfPics, // cols
                CV_32SC1); // type, 32-bit unsigned, one channel

        // initialize the person number matrix - for ease of debugging
        for (int j1 = 0; j1 < numOfPics; j1++) {
            personNumTruthMat.put(0, j1, 0);
        }

        personNames.clear();        // Make sure it starts as empty.

        int guestIndex = 0;
        // store the face images in an array
        for (Guest guest : allGuests) {


            String personName;
            String sPersonName;
            long personNumber;

            personNumber = guest.getId();
            personName = guest.getFirstName() + "_" + guest.getLastName();
            imgFilename = guest.getPicUrl();
            sPersonName = personName;
            LOGGER.info("Got " + guest + " " + personNumber + " " + personName + " " + imgFilename);

            personNames.add(sPersonName);
            LOGGER.info("Got new person " + sPersonName + " -> nPersons = " + nPersons + " [" + personNames.size() + "]");

            for (int k = 0; k < 3; k++) {


                // Keep the data
                personNumTruthMat.put(
                        0, // i
                        (guestIndex * 3) + k, // j
                        personNumber); // v

                // load the face image
                faceImgArr[(guestIndex * 3) + k] = cvLoadImage(
                        getImgDirPath(imgFilename) + "/" + k + ".jpg", // filename
                        CV_LOAD_IMAGE_GRAYSCALE); // isColor

                if (faceImgArr[(guestIndex * 3) + k] == null) {
                    throw new RuntimeException("Can't load image from " + imgFilename);
                }
            }
            guestIndex++;
        }


        final StringBuilder stringBuilder = new StringBuilder();
        nPersons = allGuests.size();
        stringBuilder.append("People: ");
        if (nPersons > 0) {
            stringBuilder.append("<").append(personNames.get(0)).append(">");
        }
        for (i = 1; i < nPersons && i < personNames.size(); i++) {
            stringBuilder.append(", <").append(personNames.get(i)).append(">");
        }
        LOGGER.info(stringBuilder.toString());

        return faceImgArr;
    }


    public String getImgDirPath(String dirId) {
        return rootDir + dirId;

    }

    /**
     * Does the Principal Component Analysis, finding the average image and the eigenfaces that represent any image in the given dataset.
     */
    private void doPCA() {
        int i;
        CvTermCriteria calcLimit;
        CvSize faceImgSize = new CvSize();

        // set the number of eigenvalues to use
        nEigens = nTrainFaces - 1;

        LOGGER.info("allocating images for principal component analysis, using " + nEigens + (nEigens == 1 ? " eigenvalue" : " eigenvalues"));

        // allocate the eigenvector images
        faceImgSize.width(trainingFaceImgArr[0].width());
        faceImgSize.height(trainingFaceImgArr[0].height());
        eigenVectArr = new IplImage[nEigens];
        for (i = 0; i < nEigens; i++) {
            eigenVectArr[i] = cvCreateImage(
                    faceImgSize, // size
                    IPL_DEPTH_32F, // depth
                    1); // channels
        }

        // allocate the eigenvalue array
        eigenValMat = cvCreateMat(
                1, // rows
                nEigens, // cols
                CV_32FC1); // type, 32-bit float, 1 channel

        // allocate the averaged image
        pAvgTrainImg = cvCreateImage(
                faceImgSize, // size
                IPL_DEPTH_32F, // depth
                1); // channels

        // set the PCA termination criterion
        calcLimit = cvTermCriteria(
                CV_TERMCRIT_ITER, // type
                nEigens, // max_iter
                1); // epsilon

        LOGGER.info("computing average image, eigenvalues and eigenvectors");
        // compute average image, eigenvalues, and eigenvectors
        opencv_legacy.cvCalcEigenObjects(
                nTrainFaces, // nObjects
                trainingFaceImgArr, // input
                eigenVectArr, // output
                opencv_legacy.CV_EIGOBJ_NO_CALLBACK, // ioFlags
                0, // ioBufSize
                null, // userData
                calcLimit,
                pAvgTrainImg, // avg
                eigenValMat.data_fl()); // eigVals

        LOGGER.info("normalizing the eigenvectors");
        cvNormalize(
                eigenValMat, // src (CvArr)
                eigenValMat, // dst (CvArr)
                1, // a
                0, // b
                CV_L1, // norm_type
                null); // mask
    }

    /**
     * Stores the training data to the file trainingDBPath.
     */
    private void storeTrainingData() {
        CvFileStorage fileStorage;
        int i;

        LOGGER.info("writing " + trainingDBPath);

        // create a file-storage interface
        fileStorage = cvOpenFileStorage(
                trainingDBPath, // filename
                null, // memstorage
                CV_STORAGE_WRITE, // flags
                null); // encoding

        // Store the person names. Added by Shervin.
        cvWriteInt(
                fileStorage, // fs
                "nPersons", // name
                nPersons); // value

        for (i = 0; i < nPersons; i++) {
            String varname = "personName_" + (i + 1);
            cvWriteString(
                    fileStorage, // fs
                    varname, // name
                    personNames.get(i), // string
                    0); // quote
        }

        // store all the data
        cvWriteInt(
                fileStorage, // fs
                "nEigens", // name
                nEigens); // value

        cvWriteInt(
                fileStorage, // fs
                "nTrainFaces", // name
                nTrainFaces); // value

        cvWrite(
                fileStorage, // fs
                "trainPersonNumMat", // name
                personNumTruthMat); // value

        cvWrite(
                fileStorage, // fs
                "eigenValMat", // name
                eigenValMat); // value

        cvWrite(
                fileStorage, // fs
                "projectedTrainFaceMat", // name
                projectedTrainFaceMat);

        cvWrite(fileStorage, // fs
                "avgTrainImg", // name
                pAvgTrainImg); // value

        for (i = 0; i < nEigens; i++) {
            String varname = "eigenVect_" + i;
            cvWrite(
                    fileStorage, // fs
                    varname, // name
                    eigenVectArr[i]); // value
        }

        // release the file-storage interface
        cvReleaseFileStorage(fileStorage);
    }

    /**
     * Opens the training data from the file trainingDBPath.
     *
     * @return the person numbers during training, or null if not successful
     */
    private CvMat loadTrainingData() {
        LOGGER.info("loading training data");
        CvMat pTrainPersonNumMat = null; // the person numbers during training
        CvFileStorage fileStorage;
        int i;

        // create a file-storage interface
        fileStorage = cvOpenFileStorage(
                trainingDBPath, // filename
                null, // memstorage
                CV_STORAGE_READ, // flags
                null); // encoding
        if (fileStorage == null) {
            LOGGER.severe("Can't open training database file " + trainingDBPath);
            return null;
        }

        // Load the person names.
        personNames.clear();        // Make sure it starts as empty.
        nPersons = cvReadIntByName(
                fileStorage, // fs
                null, // map
                "nPersons", // name
                0); // default_value
        if (nPersons == 0) {
            LOGGER.severe("No people found in the training database " + trainingDBPath);
            return null;
        } else {
            LOGGER.info(nPersons + " persons read from the training database");
        }

        // Load each person's name.
        for (i = 0; i < nPersons; i++) {
            String sPersonName;
            String varname = "personName_" + (i + 1);
            sPersonName = cvReadStringByName(
                    fileStorage, // fs
                    null, // map
                    varname,
                    "");
            personNames.add(sPersonName);
        }
        LOGGER.info("person names: " + personNames);

        // Load the data
        nEigens = cvReadIntByName(
                fileStorage, // fs
                null, // map
                "nEigens",
                0); // default_value
        nTrainFaces = cvReadIntByName(
                fileStorage,
                null, // map
                "nTrainFaces",
                0); // default_value
        Pointer pointer = cvReadByName(
                fileStorage, // fs
                null, // map
                "trainPersonNumMat"); // name
        pTrainPersonNumMat = new CvMat(pointer);

        pointer = cvReadByName(
                fileStorage, // fs
                null, // map
                "eigenValMat"); // name
        eigenValMat = new CvMat(pointer);

        pointer = cvReadByName(
                fileStorage, // fs
                null, // map
                "projectedTrainFaceMat"); // name
        projectedTrainFaceMat = new CvMat(pointer);

        pointer = cvReadByName(
                fileStorage,
                null, // map
                "avgTrainImg");
        pAvgTrainImg = new IplImage(pointer);

        eigenVectArr = new IplImage[nTrainFaces];
        for (i = 0; i <= nEigens; i++) {
            String varname = "eigenVect_" + i;
            pointer = cvReadByName(
                    fileStorage,
                    null, // map
                    varname);
            eigenVectArr[i] = new IplImage(pointer);
        }

        // release the file-storage interface
        cvReleaseFileStorage(fileStorage);

        LOGGER.info("Training data loaded (" + nTrainFaces + " training images of " + nPersons + " people)");
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("People: ");
        if (nPersons > 0) {
            stringBuilder.append("<").append(personNames.get(0)).append(">");
        }
        for (i = 1; i < nPersons; i++) {
            stringBuilder.append(", <").append(personNames.get(i)).append(">");
        }
        LOGGER.info(stringBuilder.toString());

        return pTrainPersonNumMat;
    }

    /**
     * Saves all the eigenvectors as images, so that they can be checked.
     */
    private void storeEigenfaceImages() {
        // Store the average image to a file
        LOGGER.info("Saving the image of the average face as 'data/out_averageImage.bmp'");
        cvSaveImage("data/out_averageImage.bmp", pAvgTrainImg);

        // Create a large image made of many eigenface images.
        // Must also convert each eigenface image to a normal 8-bit UCHAR image instead of a 32-bit float image.
        LOGGER.info("Saving the " + nEigens + " eigenvector images as 'data/out_eigenfaces.bmp'");

        if (nEigens > 0) {
            // Put all the eigenfaces next to each other.
            int COLUMNS = 8;        // Put upto 8 images on a row.
            int nCols = Math.min(nEigens, COLUMNS);
            int nRows = 1 + (nEigens / COLUMNS);        // Put the rest on new rows.
            int w = eigenVectArr[0].width();
            int h = eigenVectArr[0].height();
            CvSize size = cvSize(nCols * w, nRows * h);
            final IplImage bigImg = cvCreateImage(
                    size,
                    IPL_DEPTH_8U, // depth, 8-bit Greyscale UCHAR image
                    1);        // channels
            for (int i = 0; i < nEigens; i++) {
                // Get the eigenface image.
                IplImage byteImg = convertFloatImageToUcharImage(eigenVectArr[i]);
                // Paste it into the correct position.
                int x = w * (i % COLUMNS);
                int y = h * (i / COLUMNS);
                CvRect ROI = cvRect(x, y, w, h);
                cvSetImageROI(
                        bigImg, // image
                        ROI); // rect
                cvCopy(
                        byteImg, // src
                        bigImg, // dst
                        null); // mask
                cvResetImageROI(bigImg);
                cvReleaseImage(byteImg);
            }
            cvSaveImage(
                    "data/out_eigenfaces.bmp", // filename
                    bigImg); // image
            cvReleaseImage(bigImg);
        }
    }

    /**
     * Converts the given float image to an unsigned character image.
     *
     * @param srcImg the given float image
     * @return the unsigned character image
     */
    private IplImage convertFloatImageToUcharImage(IplImage srcImg) {
        IplImage dstImg;
        if ((srcImg != null) && (srcImg.width() > 0 && srcImg.height() > 0)) {
            // Spread the 32bit floating point pixels to fit within 8bit pixel range.
            double[] minVal = new double[1];
            double[] maxVal = new double[1];
            cvMinMaxLoc(srcImg, minVal, maxVal);
            // Deal with NaN and extreme values, since the DFT seems to give some NaN results.
            if (minVal[0] < -1e30) {
                minVal[0] = -1e30;
            }
            if (maxVal[0] > 1e30) {
                maxVal[0] = 1e30;
            }
            if (maxVal[0] - minVal[0] == 0.0f) {
                maxVal[0] = minVal[0] + 0.001;  // remove potential divide by zero errors.
            }                        // Convert the format
            dstImg = cvCreateImage(cvSize(srcImg.width(), srcImg.height()), 8, 1);
            cvConvertScale(srcImg, dstImg, 255.0 / (maxVal[0] - minVal[0]), -minVal[0] * 255.0 / (maxVal[0] - minVal[0]));
            return dstImg;
        }
        return null;
    }

    /**
     * Find the most likely person based on a detection. Returns the index, and stores the confidence value into pConfidence.
     *
     * @param projectedTestFace  the projected test face
     * @param pConfidencePointer a pointer containing the confidence value
     * @return the index
     */
    private int findNearestNeighbor(float projectedTestFace[], FloatPointer pConfidencePointer) {
        double leastDistSq = Double.MAX_VALUE;
        int i = 0;
        int iTrain = 0;
        int iNearest = 0;

        LOGGER.info("................");
        LOGGER.info("find nearest neighbor from " + nTrainFaces + " training faces");
        for (iTrain = 0; iTrain < nTrainFaces; iTrain++) {
            //LOGGER.info("considering training face " + (iTrain + 1));
            double distSq = 0;

            for (i = 0; i < nEigens; i++) {
                //LOGGER.debug("  projected test face distance from eigenface " + (i + 1) + " is " + projectedTestFace[i]);

                float projectedTrainFaceDistance = (float) projectedTrainFaceMat.get(iTrain, i);
                float d_i = projectedTestFace[i] - projectedTrainFaceDistance;
                distSq += d_i * d_i; // / eigenValMat.data_fl().get(i);  // Mahalanobis distance (might give better results than Eucalidean distance)
//          if (iTrain < 5) {
//            LOGGER.info("    ** projected training face " + (iTrain + 1) + " distance from eigenface " + (i + 1) + " is " + projectedTrainFaceDistance);
//            LOGGER.info("    distance between them " + d_i);
//            LOGGER.info("    distance squared " + distSq);
//          }
            }

            if (distSq < leastDistSq) {
                leastDistSq = distSq;
                iNearest = iTrain;
                LOGGER.info("  training face " + (iTrain + 1) + " is the new best match, least squared distance: " + leastDistSq);
            }
        }

        // Return the confidence level based on the Euclidean distance,
        // so that similar images should give a confidence between 0.5 to 1.0,
        // and very different images should give a confidence between 0.0 to 0.5.
        float pConfidence = (float) (1.0f - Math.sqrt(leastDistSq / (float) (nTrainFaces * nEigens)) / 255.0f);
        pConfidencePointer.put(pConfidence);

        LOGGER.info("training face " + (iNearest + 1) + " is the final best match, confidence " + pConfidence);
        return iNearest;
    }

    /**
     * Returns a string representation of the given float array.
     *
     * @param floatArray the given float array
     * @return a string representation of the given float array
     */
    private String floatArrayToString(final float[] floatArray) {
        final StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        stringBuilder.append('[');
        for (int i = 0; i < floatArray.length; i++) {
            if (isFirst) {
                isFirst = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append(floatArray[i]);
        }
        stringBuilder.append(']');

        return stringBuilder.toString();
    }

    /**
     * Returns a string representation of the given float pointer.
     *
     * @param floatPointer the given float pointer
     * @return a string representation of the given float pointer
     */
    private String floatPointerToString(final FloatPointer floatPointer) {
        final StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        stringBuilder.append('[');
        for (int i = 0; i < floatPointer.capacity(); i++) {
            if (isFirst) {
                isFirst = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append(floatPointer.get(i));
        }
        stringBuilder.append(']');

        return stringBuilder.toString();
    }

    /**
     * Returns a string representation of the given one-channel CvMat object.
     *
     * @param cvMat the given CvMat object
     * @return a string representation of the given CvMat object
     */
    public String oneChannelCvMatToString(final CvMat cvMat) {
        //Preconditions
        if (cvMat.channels() != 1) {
            throw new RuntimeException("illegal argument - CvMat must have one channel");
        }

        final int type = cvMat.type();
        StringBuilder s = new StringBuilder("[ ");
        for (int i = 0; i < cvMat.rows(); i++) {
            for (int j = 0; j < cvMat.cols(); j++) {
                if (type == CV_32FC1 || type == CV_32SC1) {
                    s.append(cvMat.get(i, j));
                } else {
                    throw new RuntimeException("illegal argument - CvMat must have one channel and type of float or signed integer");
                }
                if (j < cvMat.cols() - 1) {
                    s.append(", ");
                }
            }
            if (i < cvMat.rows() - 1) {
                s.append("\n  ");
            }
        }
        s.append(" ]");
        return s.toString();
    }
}
