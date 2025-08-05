package pers.fsh.deviceutils.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pers.fsh.deviceutils.hardware.vectorhardware.mapping.XL_CAN_EV_RX_MSG;

/**
 * CAN消息类
 *
 * @author fanshuhua
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CanMessage extends Message {
    /**
     * 消息ID
     */
    private int canId;

    /**
     * 数据长度 (0-15)
     */
    private byte dlc; // 数据长度代码（Data Length Code），表示数据字段的长度，范围从0到15字节

    /*
     * DLC 与 dataLength的对应关系
     * dlc -> dataLength
     * 0 -> 0
     * 1 -> 1
     * 2 -> 2
     * 3 -> 3
     * 4 -> 4
     * 5 -> 5
     * 6 -> 6
     * 7 -> 7
     * 8 -> 8
     * 9 -> 12
     * 10 -> 16
     * 11 -> 20
     * 12 -> 24
     * 13 -> 32
     * 14 -> 48
     * 15 -> 64
     * */
    private byte dataLength; // 数据长度 0~ 64 字节

    /**
     * 标志位
     * // to be used with XLcanTxEvent::XL_CAN_TX_MSG::msgFlags
     * #define XL_CAN_TXMSG_FLAG_EDL         0x0001u  // extended data length
     * #define XL_CAN_TXMSG_FLAG_BRS         0x0002u  // baud rate switch
     * #define XL_CAN_TXMSG_FLAG_RTR         0x0010u  // remote transmission request
     * #define XL_CAN_TXMSG_FLAG_HIGHPRIO    0x0080u  // high priority message - clears all send buffers - then transmits
     * #define XL_CAN_TXMSG_FLAG_WAKEUP      0x0200u  // generate a wakeup message
     */
//    private byte flags;
//扩展帧的话是id+ 0x80000000
    private boolean FDF; // 是否为CANFD
    private boolean remote;   // 是否为远程帧
    private boolean BRS;      // 是否为波特率切换帧
    private boolean highPriority; // 是否为高优先级帧
    private boolean wakeup;   // 是否为唤醒帧


    /**
     * 消息数据
     */
    private byte[] data;

    /**
     * 通道索引
     */
    private short channelIndex;

    /**
     * 时间戳 (硬件同步时间)
     */
    private long timestamp;

    /**
     * 是否为发送消息
     */
    private boolean dir; // 发送方向，true表示Tx消息，false表示Rx消息

    /**
     * 默认构造函数
     */
    public CanMessage() {
        this.data = new byte[64];
    }

    /**
     * 带参数构造函数
     */
    public CanMessage(XL_CAN_EV_RX_MSG msg, short channelIndex, long timestamp, boolean transmit) {
        this.canId = msg.canId;
        this.dlc = msg.dlc;
        this.dataLength = ConvertDlcToDataLength(msg.dlc); // 将DLC转换为dataLength
        this.FDF = (msg.msgFlags & 0x01) != 0; // EDL
        this.remote = (msg.msgFlags & 0x10) != 0; // RTR
        this.BRS = (msg.msgFlags & 0x02) != 0; // BRS
        this.highPriority = (msg.msgFlags & 0x80) != 0; // HIGHPRIO
        this.wakeup = (msg.msgFlags & 0x200) != 0; // WAKEUP
        this.data = new byte[64];
        System.arraycopy(msg.data, 0, this.data, 0, msg.data.length);
        this.channelIndex = channelIndex;
        this.timestamp = timestamp;
        this.dir = transmit;
    }


    //    提供DLC与dataLength转换的函数
    private byte ConvertDlcToDataLength(byte dlc) {
        return switch (dlc) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> 4;
            case 5 -> 5;
            case 6 -> 6;
            case 7 -> 7;
            case 8 -> 8;
            case 9 -> 12;
            case 10 -> 16;
            case 11 -> 20;
            case 12 -> 24;
            case 13 -> 32;
            case 14 -> 48;
            case 15 -> 64;
            default -> throw new IllegalArgumentException("Invalid DLC value");
        };
    }

    //    提供dataLength与DLC转换的函数
    private byte ConvertDataLengthToDlc(byte dataLength) {
        return switch (dataLength) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> 4;
            case 5 -> 5;
            case 6 -> 6;
            case 7 -> 7;
            case 8 -> 8;
            case 9, 10, 11, 12 -> 9;
            case 13, 14, 15, 16 -> 10;
            case 17, 18, 19, 20 -> 11;
            case 21, 22, 23, 24 -> 12;
            case 25, 26, 27, 28, 29, 30, 31, 32 -> 13;
            case 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48 -> 14;
            case 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64 -> 15;
            default -> throw new IllegalArgumentException("Invalid data length value");
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dir ? "TX " : "RX ");
        sb.append(String.format("Ch:%d ", channelIndex));
        sb.append(String.format("ID:0x%X ", canId));
        sb.append(String.format("DLC:%d ", dlc));
        sb.append(String.format("Type:[%s%s%s%s%s] ",
                FDF ? "CANFD " : "",
                remote ? "远程 " : "",
                BRS ? "可变速率 " : "",
                highPriority ? "高优先级 " : "",
                wakeup ? "WAKEUP " : ""));
        sb.append("Data:");

        if (!isRemote()) {
            for (int i = 0; i < dataLength; i++) {
                sb.append(String.format("%02X ", data[i]));
            }
        }

        sb.append(String.format(", timestamp:%d", timestamp));
        return sb.toString();
    }
}
