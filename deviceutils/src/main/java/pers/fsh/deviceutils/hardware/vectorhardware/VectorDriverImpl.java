package pers.fsh.deviceutils.hardware.vectorhardware;

import com.sun.jna.NativeLong;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pers.fsh.deviceutils.hardware.HardwareDriverInterface;
import pers.fsh.deviceutils.hardware.vectorhardware.mapping.*;
import pers.fsh.deviceutils.hardware.vectorhardware.typemapper.XLstatus;
import pers.fsh.deviceutils.model.BusType;
import pers.fsh.deviceutils.model.Result;
import pers.fsh.deviceutils.model.hardwareconfig.CanFdConfig;
import pers.fsh.deviceutils.model.hardwareconfig.HardwareConfigInterface;
import pers.fsh.deviceutils.model.message.CanMessage;
import pers.fsh.deviceutils.model.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static pers.fsh.deviceutils.hardware.vectorhardware.VxlapiDllLibrary.*;
import static pers.fsh.deviceutils.hardware.vectorhardware.typemapper.XlBusType.XL_BUS_TYPE_CAN;
import static pers.fsh.deviceutils.hardware.vectorhardware.typemapper.XlInterfaceVersion.XL_INTERFACE_VERSION_V4;


/**
 * Vector CAN硬件实现类
 *
 * @Author fanshuhua
 * @Date 2025/5/10 17:41
 */
@Log4j2
public class VectorDriverImpl implements HardwareDriverInterface {
    // Vector 事件类型常量
    private static final short XL_TIMER_EVENT = 0x0008;      // 定时器事件
    private static final short XL_CAN_EV_TAG_RX_OK = 0x0400; // 接收成功事件
    private static final short XL_CAN_EV_TAG_TX_OK = 0x0404; // 发送成功事件
    private static final short XL_CAN_EV_TAG_TX_MSG = 0x0440; // 发送消息事件

    // Vector CAN 消息标志位常量
    private static final int XL_CAN_MSG_FLAG_EXT = 0x0001;   // 扩展帧标志
    private static final int XL_CAN_MSG_FLAG_RTR = 0x0010;   // 远程帧标志
    private static final int XL_CAN_TXMSG_FLAG_EDL = 0x0001; // 扩展数据长度
    private static final int XL_CAN_TXMSG_FLAG_BRS = 0x0002; // 波特率切换
    private static final int XL_CAN_TXMSG_FLAG_HIGHPRIO = 0x0080; // 高优先级消息
    private static final int XL_CAN_TXMSG_FLAG_WAKEUP = 0x0200;   // 生成唤醒消息

    private static final int XL_ACTIVATE_RESET_CLOCK = (int) 8;

    private final NativeLongByReference pPortHandle = new NativeLongByReference(); // 通道句柄
    private final LongByReference permissionMask = new LongByReference(); // 权限掩码
    private final WinNT.HANDLE notificationHandle = new WinNT.HANDLE();
    @Getter
    @Setter
    private int rxQueueSize = 1024; // 接收队列大小，默认256

    private Consumer<Message> msgListener;
    private Thread msgListenerThread;

    public VectorDriverImpl() {
    }

    @Override
    public Result<String> init() {
        XLstatus xLstatus;
        xLstatus = xlOpenDriver();
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "Vector初始化失败", xLstatus.getDescription());
        }
        return Result.success("Vector驱动初始化成功");
    }

    @Override
    public List<HardwareConfigInterface> getHardwareList() {
        s_xl_driver_config driverConfig = new s_xl_driver_config();
        XLstatus xLstatus = xlGetDriverConfig(driverConfig);
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return null;
        }
        log.debug("Vector驱动信息: " + driverConfig);

        List<HardwareConfigInterface> hardwareList = new ArrayList<>();

        // 遍历所有通道
        for (int i = 0; i < driverConfig.channelCount; i++) {
            s_xl_channel_config channelConfig = driverConfig.channel[i];
            if (channelConfig != null) {
                //#define XL_BUS_TYPE_NONE          0x00000000u
                //#define XL_BUS_TYPE_CAN           0x00000001u
                //#define XL_BUS_TYPE_LIN           0x00000002u
                //#define XL_BUS_TYPE_FLEXRAY       0x00000004u
                //#define XL_BUS_TYPE_AFDX          0x00000008u  // former BUS_TYPE_BEAN
                //#define XL_BUS_TYPE_MOST          0x00000010u
                //#define XL_BUS_TYPE_DAIO          0x00000040u  // IO cab/piggy
                //#define XL_BUS_TYPE_J1708         0x00000100u
                //#define XL_BUS_TYPE_KLINE         0x00000800u
                //#define XL_BUS_TYPE_ETHERNET      0x00001000u
                //#define XL_BUS_TYPE_A429          0x00002000u
                // TODO 目前只支持CANFD类型
                switch (channelConfig.busParams.busType) {
                    case 0x00000000:
                        log.warn("这个一个Vector硬件的无类型通道，暂不支持: " + channelConfig.getName());
                        break;
                    case 0x00000001:
                        CanFdConfig canFdConfig = new CanFdConfig()
                                .setArbitrationBitRate(channelConfig.busParams.data.canFD.arbitrationBitRate)
                                .setSjwAbr(channelConfig.busParams.data.canFD.sjwAbr)
                                .setTseg1Abr(channelConfig.busParams.data.canFD.tseg1Abr)
                                .setTseg2Abr(channelConfig.busParams.data.canFD.tseg2Abr)
                                .setDataBitRate(channelConfig.busParams.data.canFD.dataBitRate)
                                .setSjwDbr(channelConfig.busParams.data.canFD.sjwDbr)
                                .setTseg1Dbr(channelConfig.busParams.data.canFD.tseg1Dbr)
                                .setTseg2Dbr(channelConfig.busParams.data.canFD.tseg2Dbr)
                                .setHardwareIndex(channelConfig.channelIndex)
                                .setHardwareName(channelConfig.getName() + " " + channelConfig.getTransceiverName())
                                .setAccessMask(channelConfig.channelMask);
                        canFdConfig.setBusType(BusType.BUS_TYPE_CAN);
                        hardwareList.add(canFdConfig);
                        break;
                    case 0x00000040:
                        log.warn("这个一个Vector硬件的IO口，暂不支持: " + channelConfig.getName());
                        break;
                    default:
                        log.warn("不支持的总线类型: " + channelConfig.busParams.busType);
                        break;
                }
            }
        }
        return hardwareList.isEmpty() ? null : hardwareList;
    }

    @Override
    public Result<String> connect(List<HardwareConfigInterface> hardwareList) {
        // 1. 检查硬件列表是否为空
        if (hardwareList == null || hardwareList.isEmpty()) {
            return Result.error("硬件列表不能为空");
        }
        // 2. 生成 accessMask（指定访问哪些通道） 和 permissionMask（返回具有哪些通道的配置权限）
        long accessMask = 0;
        for (HardwareConfigInterface hardwareConfig : hardwareList) {
            if (hardwareConfig instanceof CanFdConfig canFdConfig) {
                accessMask |= canFdConfig.getHardwareAccessMask();
                log.info("连接硬件: " + canFdConfig.getHardwareName() + ", Access Mask: " + canFdConfig.getHardwareAccessMask());
            } else {
                log.warn("不支持的硬件类型: " + hardwareConfig.getClass().getSimpleName());
            }
        }
        permissionMask.setValue(accessMask);
        // 3. 打开端口
        XLstatus xLstatus = VxlapiDllLibrary.xlOpenPort(pPortHandle, DRIVER_NAME, accessMask, permissionMask, rxQueueSize, XL_INTERFACE_VERSION_V4, XL_BUS_TYPE_CAN);
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "连接硬件失败", xLstatus.getDescription());
        }
        if (permissionMask.getValue() > 0) {
//            有某个通道的Init权限，可以设置波特率、采样点等信息
            long mask = 1L;

            // 遍历所有位（通道）
            for (int i = 0; i < 32; i++) {
                // 检查该位是否有权限 (permissionMask & mask) != 0
                if ((permissionMask.getValue() & mask) != 0) {
                    log.info("获得通道 " + i + " 的配置权限");
                    XLcanFdConf xlCanFdConf = new XLcanFdConf();
                    long finalMask = mask;
                    CanFdConfig canFdConfig = (CanFdConfig) hardwareList.stream().filter(e -> e.getHardwareAccessMask() == finalMask).findFirst().orElse(null);
                    if (canFdConfig != null) {
                        xlCanFdConf.arbitrationBitRate = canFdConfig.getArbitrationBitRate();
                        xlCanFdConf.dataBitRate = canFdConfig.getDataBitRate();
                        xlCanFdConf.sjwAbr = canFdConfig.getSjwAbr();
                        xlCanFdConf.tseg1Abr = canFdConfig.getTseg1Abr();
                        xlCanFdConf.tseg2Abr = canFdConfig.getTseg2Abr();
                        xlCanFdConf.sjwDbr = canFdConfig.getSjwDbr();
                        xlCanFdConf.tseg1Dbr = canFdConfig.getTseg1Dbr();
                        xlCanFdConf.tseg2Dbr = canFdConfig.getTseg2Dbr();
                        xLstatus = xlCanFdSetConfiguration(pPortHandle.getValue(), mask, xlCanFdConf);
                        if (xLstatus != XLstatus.XL_SUCCESS) {
                            return Result.error(xLstatus.getCode(), "设置通道 mask " + mask + " 配置失败", xLstatus.getDescription());
                        } else {
                            log.info("设置通道 mask " + mask + " 配置成功");
                        }
//                        xlCanSetChannelOutput(pPortHandle.getValue(), mask,1);
                    } else {
                        log.error("mask = {},未找到对应的配置项", mask);
                    }

                }
                mask <<= 1; // 移位检查下一个通道
            }
        }


        xLstatus = xlCanSetChannelMode(pPortHandle.getValue(), accessMask, 1, 1);
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "设置通道模式失败", xLstatus.getDescription());
        }
        xLstatus = xlCanSetReceiveMode(pPortHandle.getValue(), (byte) 0, (byte) 1);
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "设置接收模式失败", xLstatus.getDescription());
        }
        PointerByReference notificationHandlePre = new PointerByReference();
        xLstatus = xlSetNotification(pPortHandle.getValue(), notificationHandlePre, 1);
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "设置通知失败", xLstatus.getDescription());
        }
        xLstatus = xlActivateChannel(pPortHandle.getValue(), accessMask, XL_BUS_TYPE_CAN, XL_ACTIVATE_RESET_CLOCK);
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "激活通道失败", xLstatus.getDescription());
        }
        xLstatus = xlResetClock(pPortHandle.getValue());
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "重置时钟失败", xLstatus.getDescription());
        }
        xLstatus = xlSetTimerRate(pPortHandle.getValue(), new NativeLong(150));
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "设置定时器速率失败", xLstatus.getDescription());
        }
        notificationHandle.setPointer(notificationHandlePre.getValue());
        return Result.success("连接硬件成功", "Port Handle: " + pPortHandle.getValue() + ", Permission Mask: " + permissionMask.getValue());
    }

    @Override
    public Result<String> setMsgListener(Consumer<Message> msgListener) {
        if (msgListener == null) {
            return Result.error("消息监听器不能为空");
        }
        if (this.msgListener != null) {
            return Result.error("消息监听器已设置");
        }
        this.msgListener = msgListener;
        // 启动消息监听线程
        msgListenerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                int waitResult = Kernel32.INSTANCE.WaitForSingleObject(notificationHandle, 1000);
                if (waitResult == -1) {
                    log.error("WaitForSingleObject error: " + Kernel32.INSTANCE.GetLastError());
                    break;
                }
                if (waitResult != 0) {
                    continue; // 超时或其他原因，继续等待
                }
                // 接收消息
                XLcanRxEvent xLcanRxEvent = new XLcanRxEvent();
                XLstatus xLstatus = xlCanReceive(pPortHandle.getValue(), xLcanRxEvent);
                if (xLstatus == XLstatus.XL_ERR_QUEUE_IS_EMPTY) {
                    continue; // 队列为空，继续等待
                }
                if (xLstatus != XLstatus.XL_SUCCESS) {
                    log.error("xlCanReceive error: " + xLstatus.getDescription());
                    continue;
                }

                // 根据消息类型处理
                if (xLcanRxEvent.tag == XL_TIMER_EVENT) {
                    // 定时器事件，更新硬件时间
//                    executor.updateHardwareTime(xLcanRxEvent.timeStampSync);
                } else if (xLcanRxEvent.tag == XL_CAN_EV_TAG_RX_OK) {
                    xLcanRxEvent.tagData.setType(XL_CAN_EV_RX_MSG.class);
                    xLcanRxEvent.tagData.read();
                    // CAN接收消息处理
                    XL_CAN_EV_RX_MSG canMsg = xLcanRxEvent.tagData.canRxOkMsg;

                    // 创建CAN消息对象
                    CanMessage canMessage = new CanMessage(canMsg, xLcanRxEvent.channelIndex, xLcanRxEvent.timeStampSync, false);

//                    log.debug("接收到CAN消息: {}", canMsg);
                    log.debug("接收到CAN消息: {}", canMessage);
                } else if (xLcanRxEvent.tag == XL_CAN_EV_TAG_TX_OK) {
                    xLcanRxEvent.tagData.setType(XL_CAN_EV_RX_MSG.class);
                    xLcanRxEvent.tagData.read();
                    // CAN发送成功事件处理
                    XL_CAN_EV_RX_MSG canMsg = xLcanRxEvent.tagData.canTxOkMsg;
                    CanMessage canMessage = new CanMessage(canMsg, xLcanRxEvent.channelIndex, xLcanRxEvent.timeStampSync, true);
                    log.debug("发送CAN消息成功: {}", canMessage);
                } else {
                    // 其他类型的消息，使用字符串表示
                    String msgStr = xlCanGetEventString(xLcanRxEvent);
//                    StringMsg message = new StringMsg(msgStr);
                    log.debug("接收到其他类型消息: tag=0x{}, {}", Integer.toHexString(xLcanRxEvent.tag), msgStr);
                }
            }
        });
        msgListenerThread.start();
        return Result.success("消息监听器设置成功");
    }

    @Override
    public void disconnect() {

    }

    @Override
    public Result<String> send(Message data) {
//        TODO 假如发送错误帧可以考虑CAN流程中的发送函数？
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void release() {

    }
}

