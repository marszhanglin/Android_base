/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.bean;

/**
 * 
 * 2014-7-22œ¬ŒÁ4:42:22 ¿‡Picture
 * 
 * @author Mars zhang
 * 
 */
public class Picture {
    /** MemberVariables */
    private String title;
    /** MemberVariables */
    private int imageld;
    /** MemberVariables */
    private String fileNmae;

    /**
     * Picture
     */
    public Picture() {
        super();
    }

    /**
     * Picture
     * 
     * @param title
     * @param imageld
     */
    public Picture(String title, int imageld) {
        super();
        this.title = title;
        this.imageld = imageld;
    }

    public String getFileNmae() {
        return fileNmae;
    }

    public void setFileNmae(String fileNmae) {
        this.fileNmae = fileNmae;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageld() {
        return imageld;
    }

    public void setImageld(int imageld) {
        this.imageld = imageld;
    }

}
