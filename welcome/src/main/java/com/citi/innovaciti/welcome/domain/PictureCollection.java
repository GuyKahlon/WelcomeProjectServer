package com.citi.innovaciti.welcome.domain;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 06/07/14
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
public class PictureCollection {

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    private List<String> pictures;


}
