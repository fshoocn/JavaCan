package pers.fsh.deviceutils.hardware.vectorhardware;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;
import pers.fsh.deviceutils.hardware.vectorhardware.mapping.*;
import pers.fsh.deviceutils.hardware.vectorhardware.typemapper.XLstatus;
import pers.fsh.deviceutils.hardware.vectorhardware.typemapper.XlBusType;
import pers.fsh.deviceutils.hardware.vectorhardware.typemapper.XlInterfaceVersion;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fanshuhua
 * @date 2025/5/10 17:50
 * <p>
 * <p>
 * Vxlapi64DllLibrary 接口为Jnaerator生成的接口，由于Jnaerator为2013年的项目，不太适配JNA最新的特性，所以需要手动编写这个类
 * JNA 支持直接映射方法，可以大幅提高性能，接近自定义 JNI 的性能
 * 直接映射支持与接口映射相同的类型映射，除了 Pointer/Structure/String/WString/NativeMapped 数组作为函数参数。此外，直接映射不支持 NIO 缓冲区或原始数组作为类型映射器或 NativeMapped 返回的类型。此外，只有在使用自定义 TypeMapper 的情况下才能使用原始包装类。
 * <p>
 * 如果使用的分析器重写本机方法，则可能需要将系统属性 jna.profiler.prefix 设置为分析器使用的前缀，以避免绑定到本机方法时出现链接错误。
 */
public class VxlapiDllLibrary {
    //Driver Init

    static {
        Map<String, Object> options = new HashMap<>();
        options.put(Library.OPTION_TYPE_MAPPER, new VectorTypeMapper());

        NativeLibrary nativeLibrary = NativeLibrary.getInstance("./XLDriverLibrary/vxlapi64", options);
        Native.register(nativeLibrary);
    }

    /**
     * 打开驱动程序
     *
     * @return {@link XLstatus }
     */
    public static native XLstatus xlOpenDriver();

    public static native XLstatus xlGetApplConfig(ByteBuffer appName, int appChannel, IntByReference pHwType, IntByReference pHwIndex, IntByReference pHwChannel, XlBusType busType);

    /**
     * 设置应用程序配置
     *
     * @param appName    应用程序名称
     * @param appChannel 应用程序通道
     * @param hwType     硬件类型
     * @param hwIndex    硬件索引
     * @param hwChannel  硬件通道
     * @param busType    总线类型
     * @return {@link XLstatus }
     */
    public static native XLstatus xlSetApplConfig(ByteBuffer appName, int appChannel, int hwType, int hwIndex, int hwChannel, XlBusType busType);

    public static native XLstatus xlGetDriverConfig(s_xl_driver_config pDriverConfig);

    public static native long xlGetChannelMask(int hwType, int hwIndex, int hwChannel);

    public static native int xlGetChannelIndex(int hwType, int hwIndex, int hwChannel);

    public static native XLstatus xlOpenPort(NativeLongByReference pPortHandle, String userName, long accessMask, LongByReference pPermissionMask, int rxQueueSize, XlInterfaceVersion xlInterfaceVersion, XlBusType busType);

    // Channel Setup
    public static native XLstatus xlCanFdSetConfiguration(NativeLong portHandle, long accessMask, XLcanFdConf pCanFdConf);

    public static native XLstatus xlCanSetChannelParams(NativeLong portHandle, long accessMask, XLchipParams pChipParams);

    public static native XLstatus xlCanSetChannelParamsC200(NativeLong portHandle, long accessMask, byte btr0, byte btr1);

    public static native XLstatus xlCanSetChannelBitrate(NativeLong portHandle, long accessMask, NativeLong bitrate);

    /**
     * 设置通道输出模式 <br>
     * 如果模式为XL_OUTPUT_mode_SILENT，则CAN芯片在收到CAN消息时不会生成任何确认日志。无法传输消息，但可以在静音模式下接收。如果不调用此函数，则默认模式为正常模式。<br>
     *
     * @param portHandle 端口句柄 <br>
     * @param accessMask 访问掩码 <br>
     * @param mode       模式 <br>
     *                   - XL_OUTPUT_MODE_SILENT <br>
     *                   接收时不会生成确认（静音模式）。注意：在V5.5驱动程序版本中，静音模式已更改。Tx引脚现在已关闭（不再使用“SJA1000静音模式”）。<br>
     *                   - XL_OUTPUT_MODE_NORMAL <br>
     *                   在正常模式下，CAN 芯片可以发送和接收消息。<br>
     * @return {@link XLstatus }
     */
    public static native XLstatus xlCanSetChannelOutput(NativeLong portHandle, long accessMask, int mode);

    public static native XLstatus xlCanSetChannelTransceiver(NativeLong portHandle, long accessMask, int type, int lineMode, int resNet);

    /**
     * 设置通道模式
     *
     * @param portHandle
     * @param accessMask
     * @param tx         一个标志，指定当 CAN 芯片发送消息时，通道是否生成回执。<br>
     *                   - ‘1’ = 生成回执 <br>
     *                   - ‘0’ = 不生成回执 <br>
     *                   设置 XL_CAN_MSG_FLAG_TX_COMPLETED 标志。<br>
     * @param txrq       一个标志，指定当 CAN 芯片准备好发送消息时，通道是否应生成回执。<br>
     *                   - ‘1’ = 生成回执 <br>
     *                   - ‘0’ = 不生成回执 <br>
     *                   设置 XL_CAN_MSG_FLAG_TX_REQUEST 标志。<br>
     * @return {@link XLstatus }
     */
    public static native XLstatus xlCanSetChannelMode(NativeLong portHandle, long accessMask, int tx, int txrq);

    /*Suppresses error frames and chipstate events with ‘1’, but allows those with ‘0’.
    Error frames and chipstate events are allowed by default.
    Input parameters ► portHandle
    The port handle retrieved by xlOpenPort().
    ► ErrorFrame
    Suppresses error frames.
    ► ChipState
    Suppresses chipstate events.
    */
    public static native XLstatus xlCanSetReceiveMode(NativeLong portHandle, byte ErrorFrame, byte ChipState);

    /**
     * 设置通知 <br>
     * 该函数设置给定端口的接收队列上的通知的队列级别，并返回该队列的通知句柄。此通知句柄是自动重置Windows事件的句柄，在端口关闭之前一直有效。应用程序可以将此句柄传递给Windows WaitForSingleObject（）或aitForMultipleObjects（）函数，以等待传入的驱动程序事件，如xlReceive示例所示。对于写入的每个事件，如果生成的接收队列级别大于或等于此函数设置的队列级别，驱动程序会向Windows事件发出信号。队列级别是以字节为单位还是以事件数为单位进行评估，取决于xlOpenPort的rxQueueSize参数所描述的端口。因此，传递queueLevel=1指示驱动程序在接收队列不再为空时立即发出事件信号，这是应用程序通常需要的。Windows事件有信号状态和非信号状态，但不计数。WaitForSingleObject（）和WaitForMultipleObject（）会阻止调用线程，直到Windows事件达到信号状态，并在线程执行继续时将事件重置为非信号状态。在重置Windows事件之前，可能插入了多个驱动程序事件。为了确保所有传入的驱动程序事件最终得到处理，线程必须在循环中依次调用端口接收函数（例如xlReceive、xlCanReceive等），直到接收函数返回XL_ERR_QUEUE_IS_EMPTY，然后再等待。
     *
     * @param portHandle 端口句柄
     * @param pHandle    通知句柄的指针 <br>
     *                   指向WIN32事件句柄的指针。
     * @param queueLevel 队列级别 <br>
     *                   队列级别，以事件数或队列上要设置的字节数表示。对于LIN，该值固定为“1”。对于其他类型的总线，建议值为“1”。
     * @return {@link XLstatus }
     */
    public static native XLstatus xlSetNotification(NativeLong portHandle, PointerByReference pHandle, int queueLevel);

    public static native XLstatus xlCanRemoveAcceptanceRange(NativeLong portHandle, long accessMask, NativeLong first_id, NativeLong last_id);

    public static native XLstatus xlCanSetChannelAcceptance(NativeLong portHandle, long accessMask, NativeLong code, NativeLong mask, int idRange);

    /**
     * 添加ID的筛选器 <br>
     * 此函数设置接受的标准ID的筛选器，可以多次调用以打开多个ID窗口。不同的端口可能对信道有不同的过滤器。如果CAN硬件无法实现过滤器，驱动程序将虚拟化过滤。但是，在某些具有多个端口的配置中，应用程序将接收消息，尽管它安装了一个过滤器来阻止这些消息ID。
     *
     * @param portHandle 端口句柄
     * @param accessMask 访问掩码
     * @param first_id   第一个id
     * @param last_id    最后一个id
     * @return {@link XLstatus }
     */
    public static native XLstatus xlCanAddAcceptanceRange(NativeLong portHandle, long accessMask, NativeLong first_id, NativeLong last_id);

    /**
     * 重置接受过滤器。所选筛选器（取决于idRange标志）处于打开状态。
     *
     * @param portHandle 端口句柄
     * @param accessMask 访问掩码
     * @param idRange    id范围 <br>
     *                   为了区分过滤器是针对标准标识符还是扩展标识符重置的。<br>
     *                   XL_CAN_STD<br>
     *                   打开标准邮件ID的筛选器。<br>
     *                   XL_CAN_EXT<br>
     *                   打开扩展邮件ID的筛选器。<br>
     * @return {@link XLstatus }
     */
    public static native XLstatus xlCanResetAcceptance(NativeLong portHandle, long accessMask, int idRange);

    /**
     * 对于所选端口和通道，显示“on bus”。此时，用户可以在总线上发送和接收消息。
     *
     * @param portHandle 端口句柄
     * @param accessMask 访问掩码
     * @param busType    总线类型 <br>
     *                   Bus type that has also been used for xlOpenPort().
     * @param flags      标志 <br>
     *                   激活频道的其他标志： <br>
     *                   XL_ACTIVATE_NONE <br>
     *                   激活通道后重置内部时钟。 <br>
     *                   XL_ACTIVATE_RESET_CLOCK <br>
     * @return {@link XLstatus }
     */
    public static native XLstatus xlActivateChannel(NativeLong portHandle, long accessMask, XlBusType busType, int flags);

    //On Bus
    public static native XLstatus xlResetClock(NativeLong portHandle);

    public static native XLstatus xlSetTimerRate(NativeLong portHandle, NativeLong timerRate);

    public static native XLstatus xlCanReceive(NativeLong portHandle, XLcanRxEvent pXlCanRxEvt);

    /**
     * 发送CAN消息 <br>
     * 该函数在选定的通道上传输CAN FD消息。可以连续发送多条消息(通过单个调用)。如果消息发送成功，pMsgCntSent将包含已发送的消息数。如果消息发送失败，pMsgCntSent将为0。<br>
     *
     * @param portHandle  端口句柄
     * @param accessMask  访问掩码
     * @param msgCnt      用户要发送的消息数量 <br>
     * @param pMsgCntSent 指向已发送消息数量的指针 <br>
     * @param pXlCanTxEvt 指向包含要发送的消息的结构的指针 <br>
     * @return {@link XLstatus }
     */
    public static native XLstatus xlCanTransmitEx(NativeLong portHandle, long accessMask, int msgCnt, IntByReference pMsgCntSent, XLcanTxEvent pXlCanTxEvt);

    public static native String xlCanGetEventString(XLcanRxEvent pEv);

    public static native XLstatus xlCanFlushTransmitQueue(NativeLong portHandle, long accessMask);

    public static native XLstatus xlCanRequestChipState(NativeLong portHandle, long accessMask);

    public static native XLstatus xlGetReceiveQueueLevel(NativeLong portHandle, IntByReference level);

    public static native XLstatus xlFlushReceiveQueue(NativeLong portHandle);

    public static native String xlGetErrorString(short err);

    public static native XLstatus xlDeactivateChannel(NativeLong portHandle, long accessMask);

    public static native XLstatus xlClosePort(NativeLong portHandle);

    public static native XLstatus xlCloseDriver();
}
