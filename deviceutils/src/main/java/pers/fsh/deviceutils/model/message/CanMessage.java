package pers.fsh.deviceutils.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fanshuhua
 * @date 2025/8/5 20:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CanMessage extends MessageAbstract {
    private short channelIndex;
    private byte dlc;
    private boolean fdf; // 是否为CANFD
    private boolean remote;   // 是否为远程帧
    private boolean brs;      // 是否为波特率切换帧
    private boolean highPriority; // 是否为高优先级帧
    private boolean wakeup;   // 是否为唤醒帧
    private boolean extended; // 是否为扩展帧
    private short totalBitCnt;
    private int crc;


    public CanMessage() {
        this.data = new byte[64];
    }

    @Override
    public String toString() {
        StringBuilder dataStr = new StringBuilder();
        // 只遍历有效的数据长度，而不是整个64字节数组
        int effectiveLength = Math.min(getDlc(), getData() != null ? getData().length : 0);
        for (int i = 0; i < effectiveLength; i++) {
            dataStr.append(String.format("%02X ", getData()[i]));
        }
        return String.format("%s id=0x%08X %s %s %s %s %s dlc=%d data=[%s]",
                getDir(),
                getCanId(),
                isExtended() ? "Ext" : "Std",
                isFdf() ? "FDF" : "noFDF",
                isWakeup() ? "Wake" : "noWake",
                isHighPriority() ? "HP" : "noHP",
                isRemote() ? "RTR" : "noRTR",
                getDlc(),
                dataStr.toString().trim()
        );
    }
}
