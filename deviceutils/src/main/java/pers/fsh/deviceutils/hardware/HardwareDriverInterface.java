package pers.fsh.deviceutils.hardware;


import pers.fsh.deviceutils.model.Result;
import pers.fsh.deviceutils.model.hardwareconfig.HardwareConfigInterface;
import pers.fsh.deviceutils.model.message.MessageAbstract;

import java.util.List;
import java.util.function.Consumer;

/**
 * 硬件接口定义，用于控制各种CAN总线硬件
 *
 * @author fanshuhua
 * @date 2025/5/10 15:31
 */

public interface HardwareDriverInterface<M extends MessageAbstract> {
    String DRIVER_NAME = "JavaCan";

    Result init();

    List<HardwareConfigInterface> getHardwareList();

    Result connect(List<HardwareConfigInterface> hardwareList);

    Result addMsgListener(Consumer<M> msgListener);

    void disconnect();

    Result send(M... messages);

    boolean isConnected();

    void release();
}
