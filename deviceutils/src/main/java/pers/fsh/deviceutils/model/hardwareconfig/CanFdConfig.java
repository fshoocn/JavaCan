package pers.fsh.deviceutils.model.hardwareconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pers.fsh.deviceutils.model.BusType;

/**
 * @author fanshuhua
 * @date 2025/5/13 15:34
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CanFdConfig implements HardwareConfigInterface {
    private int arbitrationBitRate; // 仲裁比特率
    private int sjwAbr; // 仲裁比特率的同步跳转宽度
    private int tseg1Abr; // 仲裁比特率的时间段1
    private int tseg2Abr; // 仲裁比特率的时间段2
    private int dataBitRate; // 数据比特率
    private int sjwDbr; // 数据比特率的同步跳转宽度
    private int tseg1Dbr; // 数据比特率的时间段1
    private int tseg2Dbr; // 数据比特率的时间段2

    private long hardwareIndex; // 硬件索引
    private String hardwareName; // 硬件名称
    private BusType busType;
    private long accessMask; // 硬件访问掩码

    @Override
    public long getHardwareIndex() {
        return hardwareIndex;
    }

    @Override
    public String getHardwareName() {
        return hardwareName;
    }

    @Override
    public BusType getBusType() {
        return busType;
    }

    @Override
    public long getHardwareAccessMask() {
        return accessMask;
    }
}
