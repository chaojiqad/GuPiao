package com.subzero.shares.bean;

import java.util.List;

/**
 * OptionalStockPoolItemPhotoBean股票量化池itemphotobean
 * Created by The_p on 2016/4/28.
 */
public class OptionalStockPoolItemPhotoBean {

    /**
     * photo : [{"alt":"","url":"20160428/5721c8039e166.jpg"}]
     * thumb :
     */

    private String thumb;
    private List<OptionalStockPoolItemPhotoUrlBean> photo;
    /**
     * content : <p>会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍会员等级介绍</p>
     * image : {"thumb":"","photo":[{"url":"20160524\/5744227b09b31.jpg","alt":""}]}
     */

    private String image;

    public List<OptionalStockPoolItemPhotoUrlBean> getPhoto() {
        return photo;
    }

    public void setPhoto(List<OptionalStockPoolItemPhotoUrlBean> photo) {
        this.photo = photo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
