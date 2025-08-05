package pers.fsh.deviceutils.model;

import lombok.Getter;

/**
 * @author fanshuhua
 * @date 2025/5/19 19:46
 */
@Getter
public enum BusType {

    BUS_TYPE_NONE(0x00000000),
    BUS_TYPE_CAN(0x00000001),
    BUS_TYPE_LIN(0x00000002),
    BUS_TYPE_FLEXRAY(0x00000004),
    BUS_TYPE_AFDX(0x00000008),
    BUS_TYPE_MOST(0x00000010),
    BUS_TYPE_DAIO(0x00000040),
    BUS_TYPE_J1708(0x00000100),
    BUS_TYPE_A429(0x00002000),
    BUS_TYPE_KLINE(0x00000800),
    BUS_TYPE_ETHERNET(0x00001000);

    private final int value;

    BusType(int value) {
        this.value = value;
    }

}
