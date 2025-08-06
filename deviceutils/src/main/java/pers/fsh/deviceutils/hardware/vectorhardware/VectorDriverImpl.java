package pers.fsh.deviceutils.hardware.vectorhardware;

import com.sun.jna.NativeLong;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
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
import pers.fsh.deviceutils.model.message.Dir;
import pers.fsh.deviceutils.threadpool.ThreadPoolManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
public class VectorDriverImpl implements HardwareDriverInterface<CanMessage> {
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

    private final List<Consumer<CanMessage>> msgListeners = new ArrayList<>();
    private long accessMask = 0;
    private final NativeLongByReference pPortHandle = new NativeLongByReference(); // 通道句柄
    private final LongByReference permissionMask = new LongByReference(); // 权限掩码
    private final WinNT.HANDLE notificationHandle = new WinNT.HANDLE();
    @Getter
    @Setter
    private int rxQueueSize = 1024; // 接收队列大小，默认256
    private List<Long> accessMaskList = new ArrayList<>(); // 用于存储访问掩码
    private Thread msgListenerThread;
    private volatile boolean connected = false;

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
        accessMask = 0;
        accessMaskList.clear();
        Map<Long, CanFdConfig> configMap = hardwareList.stream()
                .filter(c -> c instanceof CanFdConfig)
                .map(c -> (CanFdConfig) c)
                .peek(c -> log.info("连接硬件: " + c.getHardwareName() + ", Access Mask: " + c.getHardwareAccessMask()))
                .collect(Collectors.toMap(CanFdConfig::getHardwareAccessMask, c -> c, (c1, c2) -> c1));

        for (HardwareConfigInterface hardwareConfig : hardwareList) {
            if (hardwareConfig instanceof CanFdConfig canFdConfig) {
                accessMask |= canFdConfig.getHardwareAccessMask();
                accessMaskList.add(canFdConfig.getHardwareAccessMask());
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

        Result<String> configResult = configureCanChannels(permissionMask.getValue(), configMap);
        if (!configResult.isSuccess()) {
            return configResult;
        }

        Result<String> activationResult = activateCanChannels(accessMask);
        if (!activationResult.isSuccess()) {
            return activationResult;
        }

        startMessageListenerThread();
        connected = true;

        return Result.success("连接硬件成功", "Port Handle: " + pPortHandle.getValue() + ", Permission Mask: " + permissionMask.getValue());
    }

    /**
     * 配置启用的CAN通道
     *
     * @param permissionMaskValue 权限掩码
     * @param configMap           通道配置信息
     * @return 配置结果
     */
    private Result<String> configureCanChannels(long permissionMaskValue, Map<Long, CanFdConfig> configMap) {
        if (permissionMaskValue > 0) {
            long mask = 1L;
            for (int i = 0; i < 32; i++) {
                if ((permissionMaskValue & mask) != 0) {
                    log.info("获得通道 " + i + " 的配置权限");
                    XLcanFdConf xlCanFdConf = new XLcanFdConf();
                    CanFdConfig canFdConfig = configMap.get(mask);
                    if (canFdConfig != null) {
                        xlCanFdConf.arbitrationBitRate = canFdConfig.getArbitrationBitRate();
                        xlCanFdConf.dataBitRate = canFdConfig.getDataBitRate();
                        xlCanFdConf.sjwAbr = canFdConfig.getSjwAbr();
                        xlCanFdConf.tseg1Abr = canFdConfig.getTseg1Abr();
                        xlCanFdConf.tseg2Abr = canFdConfig.getTseg2Abr();
                        xlCanFdConf.sjwDbr = canFdConfig.getSjwDbr();
                        xlCanFdConf.tseg1Dbr = canFdConfig.getTseg1Dbr();
                        xlCanFdConf.tseg2Dbr = canFdConfig.getTseg2Dbr();
                        XLstatus xLstatus = xlCanFdSetConfiguration(pPortHandle.getValue(), mask, xlCanFdConf);
                        if (xLstatus != XLstatus.XL_SUCCESS) {
                            return Result.error(xLstatus.getCode(), "设置通道 mask " + mask + " 配置失败", xLstatus.getDescription());
                        } else {
                            log.info("设置通道 mask " + mask + " 配置成功");
                        }
                    } else {
                        log.error("mask = {},未找到对应的配置项", mask);
                    }
                }
                mask <<= 1;
            }
        }
        return Result.success("通道配置成功");
    }

    /**
     * 激活CAN通道
     *
     * @param accessMask 访问掩码
     * @return 激活结果
     */
    private Result<String> activateCanChannels(long accessMask) {
        XLstatus xLstatus = xlCanSetChannelMode(pPortHandle.getValue(), accessMask, 1, 1);
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "设置通道模式失败", xLstatus.getDescription());
        }
        xLstatus = xlCanSetReceiveMode(pPortHandle.getValue(), (byte) 1, (byte) 1);
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "设置接收模式失败", xLstatus.getDescription());
        }
        PointerByReference notificationHandlePre = new PointerByReference();
        xLstatus = xlSetNotification(pPortHandle.getValue(), notificationHandlePre, 1);
        if (xLstatus != XLstatus.XL_SUCCESS) {
            return Result.error(xLstatus.getCode(), "设置通知失败", xLstatus.getDescription());
        }
        notificationHandle.setPointer(notificationHandlePre.getValue());
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
        return Result.success("通道激活成功");
    }

    /**
     * 启动消息监听线程
     */
    private void startMessageListenerThread() {
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
                XLstatus xLstatusReceive = xlCanReceive(pPortHandle.getValue(), xLcanRxEvent);
                if (xLstatusReceive == XLstatus.XL_ERR_QUEUE_IS_EMPTY) {
                    continue; // 队列为空，继续等待
                }
                if (xLstatusReceive != XLstatus.XL_SUCCESS) {
                    log.error("xlCanReceive error: " + xLstatusReceive.getDescription());
                    continue;
                }

                // 根据消息类型处理
                if (xLcanRxEvent.tag == XL_TIMER_EVENT) {
                    // 定时器事件，更新硬件时间
//                    executor.updateHardwareTime(xLcanRxEvent.timeStampSync);
                } else if (xLcanRxEvent.tag == XL_CAN_EV_TAG_RX_OK) {
                    xLcanRxEvent.tagData.setType("canRxOkMsg");
                    xLcanRxEvent.tagData.read();
                    // CAN接收消息处理
                    XL_CAN_EV_RX_MSG canMsg = xLcanRxEvent.tagData.canRxOkMsg;
                    // 创建CAN消息对象
                    CanMessage canMessage = createCanMessage(canMsg, xLcanRxEvent.channelIndex, xLcanRxEvent.timeStampSync, false);
                    synchronized (msgListeners) {
                        for (Consumer<CanMessage> listener : msgListeners) {
                            ThreadPoolManager.getInstance().execute(() -> listener.accept(canMessage));
                        }
                    }
                    log.debug("接收到CAN消息: {}", canMessage);
                } else if (xLcanRxEvent.tag == XL_CAN_EV_TAG_TX_OK) {
                    xLcanRxEvent.tagData.setType("canTxOkMsg");
                    xLcanRxEvent.tagData.read();
                    // CAN发送成功事件处理
                    XL_CAN_EV_RX_MSG canMsg = xLcanRxEvent.tagData.canTxOkMsg;
                    CanMessage canMessage = createCanMessage(canMsg, xLcanRxEvent.channelIndex, xLcanRxEvent.timeStampSync, true);
                    synchronized (msgListeners) {
                        for (Consumer<CanMessage> listener : msgListeners) {
                            ThreadPoolManager.getInstance().execute(() -> listener.accept(canMessage));
                        }
                    }
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
    }

    @Override
    public Result<String> addMsgListener(Consumer<CanMessage> msgListener) {
        if (msgListener == null) {
            return Result.error("消息监听器不能为空");
        }
        synchronized (msgListeners) {
            if (!msgListeners.contains(msgListener)) {
                msgListeners.add(msgListener);
            }
        }
        return Result.success("消息监听器设置成功");
    }

    @Override
    public void disconnect() {
        if (!connected) {
            return;
        }
        // 停止监听线程
        if (msgListenerThread != null && msgListenerThread.isAlive()) {
            msgListenerThread.interrupt();
            try {
                msgListenerThread.join(1000); // 等待线程结束
            } catch (InterruptedException e) {
                log.error("等待消息监听线程结束时被中断", e);
                Thread.currentThread().interrupt();
            }
        }

        // 停用通道
        if (pPortHandle.getValue() != null) {
            xlDeactivateChannel(pPortHandle.getValue(), permissionMask.getValue());
            // 关闭端口
            xlClosePort(pPortHandle.getValue());
        }

        // 清理资源
        msgListeners.clear();
        connected = false;
        log.info("Vector hardware disconnected.");
    }

    @Override
    public Result<String> send(CanMessage... data) {
        if (!connected) {
            return Result.error("硬件未连接");
        }
        if (data == null || data.length == 0) {
            return Result.error("发送数据不能为空");
        }

        // 按通道分组消息
        Map<Short, List<CanMessage>> messagesByChannel = new HashMap<>();
        for (CanMessage canMessage : data) {
            if (canMessage.getDlc() < 0 || canMessage.getDlc() > 15) {
                return Result.error("DLC必须在0~15之间");
            }
            messagesByChannel.computeIfAbsent(canMessage.getChannelIndex(), k -> new ArrayList<>()).add(canMessage);
        }

        int totalSentCount = 0;
        short transId = 0;

        // 为每个通道分别发送消息
        for (Map.Entry<Short, List<CanMessage>> entry : messagesByChannel.entrySet()) {
            short channelIndex = entry.getKey();
            List<CanMessage> channelMessages = entry.getValue();

            // 计算该通道的accessMask
            long channelAccessMask = 1L << channelIndex;

            // 构建发送事件数组
            XLcanTxEvent.ByReference[] txMsgList = (XLcanTxEvent.ByReference[]) new XLcanTxEvent.ByReference().toArray(channelMessages.size());

            for (int i = 0; i < channelMessages.size(); i++) {
                CanMessage canMessage = channelMessages.get(i);
                XLcanTxEvent.ByReference txEvent = txMsgList[i];
                txEvent.tag = XL_CAN_EV_TAG_TX_MSG;
                txEvent.transId = transId++;
                txEvent.channelIndex = (byte) canMessage.getChannelIndex();
                txEvent.tagData.setType(XL_CAN_TX_MSG.class);

                XL_CAN_TX_MSG canTxMsg = txEvent.tagData.canMsg;
                canTxMsg.canId = canMessage.getCanId();
                canTxMsg.msgFlags = 0;

                if (canMessage.isExtended()) {
                    canTxMsg.msgFlags |= XL_CAN_MSG_FLAG_EXT;
                }
                if (canMessage.isRemote()) {
                    canTxMsg.msgFlags |= XL_CAN_MSG_FLAG_RTR;
                }
                if (canMessage.isFdf()) {
                    canTxMsg.msgFlags |= XL_CAN_TXMSG_FLAG_EDL;
                }
                if (canMessage.isBrs()) {
                    canTxMsg.msgFlags |= XL_CAN_TXMSG_FLAG_BRS;
                }
                if (canMessage.isHighPriority()) {
                    canTxMsg.msgFlags |= XL_CAN_TXMSG_FLAG_HIGHPRIO;
                }
                if (canMessage.isWakeup()) {
                    canTxMsg.msgFlags |= XL_CAN_TXMSG_FLAG_WAKEUP;
                }

                canTxMsg.dlc = canMessage.getDlc();
                System.arraycopy(canMessage.getData(), 0, canTxMsg.data, 0, Math.min(canMessage.getData().length, 64));
            }

            // 发送该通道的消息
            IntByReference msgCountSent = new IntByReference();
            XLstatus xLstatus = xlCanTransmitEx(pPortHandle.getValue(), channelAccessMask, channelMessages.size(), msgCountSent, txMsgList[0]);

            if (xLstatus != XLstatus.XL_SUCCESS) {
                return Result.error(xLstatus.getCode(), "通道 " + channelIndex + " 发送CAN消息失败", xLstatus.getDescription());
            }

            totalSentCount += msgCountSent.getValue();
            log.debug("通道 {} 成功发送 {} 条消息", channelIndex, msgCountSent.getValue());
        }

        return Result.success("发送CAN消息成功", "总共发送消息数量: " + totalSentCount);
    }

    @Override
    public boolean isConnected() {
        return connected && pPortHandle.getValue() != null;
    }

    @Override
    public void release() {
        disconnect();
        xlCloseDriver();
        ThreadPoolManager.getInstance().shutdown();
        log.info("Vector driver released.");
    }


    private CanMessage createCanMessage(XL_CAN_EV_RX_MSG canMsg, short channelIndex, long timeStamp, boolean isTx) {
        CanMessage canMessage = new CanMessage();
        canMessage.setChannelIndex(channelIndex);
        System.out.println("canMsg = " + canMsg);
        canMessage.setCanId(canMsg.canId);
        canMessage.setDlc((byte) canMsg.dlc);
        canMessage.setData(new byte[64]);
        // 确保正确复制数据，限制在有效的DLC范围内
        int copyLength = Math.min(canMsg.dlc, 64);
        if (copyLength > 0) {
            System.arraycopy(canMsg.data, 0, canMessage.getData(), 0, copyLength);
        }
        canMessage.setDir(isTx ? Dir.Tx : Dir.Rx);
        canMessage.setTimeStamp(timeStamp);
        canMessage.setExtended((canMsg.msgFlags & XL_CAN_MSG_FLAG_EXT) != 0);
        canMessage.setRemote((canMsg.msgFlags & XL_CAN_MSG_FLAG_RTR) != 0);
        canMessage.setFdf((canMsg.msgFlags & XL_CAN_TXMSG_FLAG_EDL) != 0);
        canMessage.setBrs((canMsg.msgFlags & XL_CAN_TXMSG_FLAG_BRS) != 0);
        canMessage.setHighPriority((canMsg.msgFlags & XL_CAN_TXMSG_FLAG_HIGHPRIO) != 0);
        canMessage.setWakeup((canMsg.msgFlags & XL_CAN_TXMSG_FLAG_WAKEUP) != 0);
        canMessage.setTotalBitCnt(canMsg.totalBitCnt);
        canMessage.setCrc(canMsg.crc);

        // 添加调试日志来检查数据是否正确设置
        log.debug("创建CanMessage: id=0x{}, dlc={}, flags=0x{}",
                Integer.toHexString(canMsg.canId), canMsg.dlc, Integer.toHexString(canMsg.msgFlags));

        return canMessage;
    }
}
