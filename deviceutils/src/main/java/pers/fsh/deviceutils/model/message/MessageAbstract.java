package pers.fsh.deviceutils.model.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author fanshuhua
 * @date 2025/8/5 20:23
 */
@Data
@Accessors(chain = true)
public abstract class MessageAbstract {
    protected int canId;
    protected byte[] data;
    protected Dir dir;
    protected long timeStamp;

    public MessageAbstract setData(byte[] data) {
        if (data == null || data.length == 0) {
            this.data = new byte[64]; // 默认长度为64字节
        } else {
            System.arraycopy(data, 0, this.data, 0, Math.min(data.length, 64));
        }
        return this;
    }
}
