package com.citi.innovaciti.welcome.web;

import com.citi.innovaciti.welcome.daos.GuestDao;
import com.citi.innovaciti.welcome.domain.Guest;
import com.citi.innovaciti.welcome.domain.PictureCollection;
import com.citi.innovaciti.welcome.services.FaceRecognition;
import com.citi.innovaciti.welcome.services.FaceRecognitionResult;
import com.citi.innovaciti.welcome.services.FaceRecognizerService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Liron on 27/04/2014.
 */
@RequestMapping(value = "/guests")
@Controller
public class GuestController {


    private final static Logger log = LoggerFactory.getLogger(GuestController.class);

    @Autowired
    private GuestDao guestDao;

    @Autowired
    private FaceRecognizerService faceRecognizerService;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> showGuests(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(value = "size", required = false, defaultValue = "100") int size) {

        Map<String, Object> model = new HashMap<String, Object>();
        List<Guest> guests = guestDao.getGuests(page, size);
        model.put("guests", guests);

        long guestsCount = guestDao.getGuestsCount();
        float numOfPages = (float) guestsCount / size;
        model.put("maxPages", (int) ((numOfPages > (int) numOfPages || numOfPages == 0.0) ? numOfPages + 1 : numOfPages));

        return model;

    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> searchByPic(@RequestBody PictureCollection pictureCollection) {

        int numOfPics=0;
        if(pictureCollection!=null){
            numOfPics=pictureCollection.getPictures().size();
        }
        log.info("Recived "+numOfPics+" pictures");

        Map<String, Object> model = new HashMap<String, Object>();

        List<FaceRecognitionResult> results = new ArrayList<FaceRecognitionResult>();

        try {

            for (String pic : pictureCollection.getPictures()) {

                FaceRecognitionResult result = recognize(pic);
                results.add(result);
            }

        } catch (Exception e) {

            log.error("Failed to execute Face Recognition", e);
            model.put("picUrl", "");
            model.put("guest", "{}");
            return model;
        }


        Map<Long, List<Float>> guestIdToConfidence = new HashMap<Long, List<Float>>();

        for (FaceRecognitionResult result : results) {

            if (result != null) {

                List<Float> confidenceList = guestIdToConfidence.get(result.getGuestId());

                if (confidenceList == null) {    //there is no entry in the map for this guestId
                    guestIdToConfidence.put(result.getGuestId(), new ArrayList<Float>());
                }
                guestIdToConfidence.get(result.getGuestId()).add(result.getConfidence());
            }

        }


        long bestGuestMatch = -1;
        float bestConfidence = Float.NEGATIVE_INFINITY;

        for (Map.Entry<Long,List<Float>> entry: guestIdToConfidence.entrySet()) {

            Long guestId = entry.getKey();
            List<Float> confidenceList = entry.getValue();
            float sum = 0;
            for (Float confidence : confidenceList) {
                sum += confidence;
            }

            float confidenceAvg = sum / confidenceList.size();
            if (confidenceAvg > bestConfidence) {
                bestConfidence = confidenceAvg;
                bestGuestMatch = guestId;

            }

        }

        if (bestConfidence >= -14) {
            log.info("Face recognition recognized the picture as guest ID " + bestGuestMatch + ". Avg Confidence: " + bestConfidence);
            model.put("guest", guestDao.findById(bestGuestMatch));
            return model;
        }




        //guest wasn't found, save the pictures and return identifier for their folder

        //create a new folder under /resources
        long currTime = System.currentTimeMillis();
        String guestDirPath = FaceRecognition.rootDir + currTime + "/";
        File guestDir = new File(guestDirPath);
        guestDir.mkdir();

        for (int i = 0; i < pictureCollection.getPictures().size(); i++) {

            String fileName = i + ".jpg";
            String picFilePath = guestDirPath + fileName;
            saveBase64Picture(pictureCollection.getPictures().get(i), picFilePath);

        }

        model.put("picUrl", currTime);

        model.put("guest", "{}");

        return model;
    }


    private FaceRecognitionResult recognize(String picToMatchInBase64) throws IOException {

        //create a temp file
        File picToMatchTempFile = null;
        try {
            picToMatchTempFile = File.createTempFile("tempPicfile", ".JPG");

        } catch (IOException e) {

            log.error("Failed to create a temp file for the given picture", e);
            throw e;
        }

        saveBase64Picture(picToMatchInBase64, picToMatchTempFile.getPath());

        //send the given picture for recognition
        log.info("Sending a picture for face recognition");
        FaceRecognitionResult recognitionResult = faceRecognizerService.getRecognizedGuest(picToMatchTempFile.getPath());

        return recognitionResult;
    }


    private void saveBase64Picture(String base64Pic, String picFilePath) {

        byte[] imageByteArray = Base64.decodeBase64(base64Pic);

        FileOutputStream imageOutFile = null;
        try {

            imageOutFile = new FileOutputStream(picFilePath);
            imageOutFile.write(imageByteArray);
            imageOutFile.close();

        } catch (FileNotFoundException e) {
            log.error("Failed to find image file", e);

        } catch (IOException e) {
            log.error("Failed to find image file", e);
        } finally {

            if (imageOutFile != null) {

                try {
                    imageOutFile.close();
                } catch (IOException e) {
                    log.error("Failed to close image file", e);
                }
            }

        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> create(@RequestBody Guest guest) {

        Map<String, Object> model = new HashMap<String, Object>();

        guestDao.save(guest);

        model.put("guest", guest);

/*        //add the new guest pictures to trainFaces.txt and train the model
        String guestPicsDirPath = FaceRecognition.rootDir + guest.getPicUrl();
        File guestPicsDir = new File(guestPicsDirPath);
        File[] guestPics = guestPicsDir.listFiles();
        if (guestPics == null) {
            return model;
        }
        StringBuilder lines = new StringBuilder();

        for (File pic : guestPics) {

            lines.append(guest.getId()).append(" ")
                    .append(guest.getFirstName()).append("_").append(guest.getLastName())
                    .append(" ").append(pic.getPath()).append("\n");
        }*/


        /* Writer trainingFileWriter = null;

        try {
            trainingFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(trainingFilePath,true), "utf-8"));
            trainingFileWriter.write(lines.toString());

        } catch (IOException ex) {

            log.error("Failed to update training file with details of guest "+guest.getId(),ex);
            return model;

        } finally {

            try {
                trainingFileWriter.close();
            } catch (Exception ex) {

            }
        }*/

        log.info("Training the model");
        try {

            faceRecognizerService.trainModel(guestDao.getAllGuests());
        } catch (Exception e) {
            log.error("Failed to train the Face-Recognition model", e);
            return model;
        }
        log.info("Finished Training the model");

        return model;
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showTestJson(Model uiModel) {

        return "test";
    }
}
