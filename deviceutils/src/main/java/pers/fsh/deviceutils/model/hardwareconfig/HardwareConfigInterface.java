package pers.fsh.deviceutils.model.hardwareconfig;


import pers.fsh.deviceutils.model.BusType;

/**
 * @author fanshuhua
 * @date 2025/5/15 17:24
 */
public interface HardwareConfigInterface {
    long getHardwareIndex();

    String getHardwareName();

    BusType getBusType();

    long getHardwareAccessMask();
}
