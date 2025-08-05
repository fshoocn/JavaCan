package pers.fsh.deviceutils.hardware.vectorhardware.mapping;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

import java.util.Arrays;
import java.util.List;

/**
 * CAN发送事件结构
 * 对应VxlAPI中的XLcanTxEvent结构
 */
public class XLcanTxEvent extends Structure {
    /**
     * 2 - type of the event
     */
    public short tag;
    /**
     * 2
     */
    public short transId;
    /**
     * 1 - internal has to be 0
     */
    public byte channelIndex;
    /**
     * 3 - has to be zero<br>
     * C type : unsigned char[3]
     */
    public byte[] reserved = new byte[3];
    /**
     * C type : tagData_union
     */
    public tagData_union tagData;

    public XLcanTxEvent() {
        super(8);
    }

    /**
     * @param tag          2 - type of the event<br>
     * @param transId      2<br>
     * @param channelIndex 1 - internal has to be 0<br>
     * @param reserved     3 - has to be zero<br>
     *                     C type : unsigned char[3]<br>
     * @param tagData      C type : tagData_union
     */
    public XLcanTxEvent(short tag, short transId, byte channelIndex, byte[] reserved, tagData_union tagData) {
        super(8);
        this.tag = tag;
        this.transId = transId;
        this.channelIndex = channelIndex;
        if ((reserved.length != this.reserved.length))
            throw new IllegalArgumentException("Wrong array size !");
        this.reserved = reserved;
        this.tagData = tagData;
    }

    public XLcanTxEvent(Pointer peer) {
        super(peer, 8);
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("tag", "transId", "channelIndex", "reserved", "tagData");
    }

    /**
     * <i>native declaration : vxlapi.h:67</i>
     */
    public static class tagData_union extends Union {
        /**
         * C type : XL_CAN_TX_MSG
         */
        public XL_CAN_TX_MSG canMsg;

        public tagData_union() {
            super();
            System.out.println("tagData_union");
        }

        /**
         * @param canMsg C type : XL_CAN_TX_MSG
         */
        public tagData_union(XL_CAN_TX_MSG canMsg) {
            super();
            this.canMsg = canMsg;
            setType(XL_CAN_TX_MSG.class);
        }

        public tagData_union(Pointer peer) {
            super(peer, 8);
        }

        public static class ByReference extends tagData_union implements Structure.ByReference {

        }

        public static class ByValue extends tagData_union implements Structure.ByValue {

        }

    }

    public static class ByReference extends XLcanTxEvent implements Structure.ByReference {

    }

    public static class ByValue extends XLcanTxEvent implements Structure.ByValue {

    }

}