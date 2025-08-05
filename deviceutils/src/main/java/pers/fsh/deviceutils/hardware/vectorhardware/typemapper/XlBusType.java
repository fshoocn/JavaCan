package pers.fsh.deviceutils.hardware.vectorhardware.typemapper;

import lombok.Getter;

/**
 * @author fanshuhua
 * @date 2025/5/13 19:34
 */
@Getter
public enum XlBusType {
    XL_BUS_TYPE_NONE(0x00000000),
    XL_BUS_TYPE_CAN(0x00000001),
    XL_BUS_TYPE_LIN(0x00000002),
    XL_BUS_TYPE_FLEXRAY(0x00000004),
    XL_BUS_TYPE_AFDX(0x00000008),
    XL_BUS_TYPE_MOST(0x00000010),
    XL_BUS_TYPE_DAIO(0x00000040),
    XL_BUS_TYPE_J1708(0x00000100),
    XL_BUS_TYPE_A429(0x00002000),
    XL_BUS_TYPE_KLINE(0x00000800),
    XL_BUS_TYPE_ETHERNET(0x00001000);
    @Getter
    private final int value;

    XlBusType(int value) {
        this.value = value;
    }

}
