package pers.fsh.deviceutils.model.message;

/**
 * @author fanshuhua
 * @date 2025/5/29 17:48
 */
public class StringMsg extends Message {
    private String str;

    public StringMsg(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
