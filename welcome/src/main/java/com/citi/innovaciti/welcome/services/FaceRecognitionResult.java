package com.citi.innovaciti.welcome.services;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 14/09/14
 * Time: 09:25
 * To change this template use File | Settings | File Templates.
 */
public class FaceRecognitionResult {

    private long guestId;
    private float confidence;



    public FaceRecognitionResult(long guestId, float confidence) {

        this.guestId = guestId;
        this.confidence = confidence;
    }

    public long getGuestId() {
        return guestId;
    }

    public void setGuestId(long guestId) {
        this.guestId = guestId;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FaceRecognitionResult)) return false;

        FaceRecognitionResult that = (FaceRecognitionResult) o;

        if (Float.compare(that.confidence, confidence) != 0) return false;
        if (guestId != that.guestId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (guestId ^ (guestId >>> 32));
        result = 31 * result + (confidence != +0.0f ? Float.floatToIntBits(confidence) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FaceRecognitionResult{" +
                "guestId=" + guestId +
                ", confidence=" + confidence +
                '}';
    }
}
