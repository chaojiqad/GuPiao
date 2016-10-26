package com.subzero.shares.bean;

import java.util.List;

/**
 *图片url解析结果
 */
public class ThrendlItemPhotoBean {

    /**
     * photo : [{"alt":"","url":"20160428/5721c8039e166.jpg"}]
     * thumb :
     */

    private String thumb;
    private List<ThrendItemPhotoUrlBean> photo;

    public List<ThrendItemPhotoUrlBean> getPhoto() {
        return photo;
    }

    public void setPhoto(List<ThrendItemPhotoUrlBean> photo) {
        this.photo = photo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
