package dev.moutamid.mobilebrowser.models;

public class TabsModel {
    public String link, bitmapName;

    public TabsModel(String link, String bitmapName) {
        this.link = link;
        this.bitmapName = bitmapName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBitmapName() {
        return bitmapName;
    }

    public void setBitmapName(String bitmapName) {
        this.bitmapName = bitmapName;
    }

    public TabsModel() {
    }
}
