package me.slinng.tribusevent.gui;

public class Placeholder {

    private String placeholder;
    private String replaced;

    public Placeholder(String placeholder, String replaced) {
        this.placeholder = placeholder;
        this.replaced = replaced;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getReplaced() {
        return replaced;
    }

    public void setReplaced(String replaced) {
        this.replaced = replaced;
    }
}
