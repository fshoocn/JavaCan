package pers.fsh.deviceutils.hardware.vectorhardware.typemapper;

/**
 * @author fanshuhua
 * @date 2025/5/12 20:43
 */
public class XLstatus {
    // 预定义常量
    public static final XLstatus XL_SUCCESS = new XLstatus(0, "XL_SUCCESS", "操作成功");
    public static final XLstatus XL_ERR_QUEUE_IS_EMPTY = new XLstatus(10, "XL_ERR_QUEUE_IS_EMPTY", "接收队列为空，用户可以正常继续");
    private final int code;
    private final String message;
    private final String description;

    /**
     * 创建状态对象
     */
    public XLstatus(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    /**
     * 从状态码创建XLstatus对象
     */
    public static XLstatus valueOf(int code) {
        return switch (code) {
            case 0 -> XL_SUCCESS;
            case 1 -> new XLstatus(1, "XL_PENDING", "操作正在进行中");
            case 10 -> XL_ERR_QUEUE_IS_EMPTY;
            case 11 -> new XLstatus(11, "XL_ERR_QUEUE_IS_FULL", "传输队列已满��传输事件将丢失");
            case 12 -> new XLstatus(12, "XL_ERR_TX_NOT_POSSIBLE", "硬件繁忙，无法立即传输事件");
            case 14 -> new XLstatus(14, "XL_ERR_NO_LICENSE", "用户请求的操作需要许可证解锁");
            case 101 -> new XLstatus(101, "XL_ERR_WRONG_PARAMETER", "传递给驱动的至少一个参数错误或无效");
            case 110 -> new XLstatus(110, "XL_ERR_TWICE_REGISTER", "用户尝试注册已经注册或已在使用的内容");
            case 111 -> new XLstatus(111, "XL_ERR_INVALID_CHAN_INDEX", "驱动尝试访问无效索引的通道");
            case 112 ->
                    new XLstatus(112, "XL_ERR_INVALID_ACCESS", "用户调用端口时指定了未在打开端口时声明访问权限的通道");
            case 113 ->
                    new XLstatus(113, "XL_ERR_PORT_IS_OFFLINE", "用户调用了需要在线执行的端口功能，但端口处于离线状态");
            case 116 ->
                    new XLstatus(116, "XL_ERR_CHAN_IS_ONLINE", "用户调用了需要通道处于离线状态的功能，但至少有一个通道在线");
            case 117 -> new XLstatus(117, "XL_ERR_NOT_IMPLEMENTED", "用户调用的功能尚未实现");
            case 118 -> new XLstatus(118, "XL_ERR_INVALID_PORT", "驱动尝试通过无效指针或索引访问端口");
            case 120 -> new XLstatus(120, "XL_ERR_HW_NOT_READY", "访问的硬件未准备好");
            case 121 -> new XLstatus(121, "XL_ERR_CMD_TIMEOUT", "等待命令响应事件时发生超时");
            case 122 -> new XLstatus(122, "XL_ERR_CMD_HANDLING", "发生了与命令执行相关的内部错误");
            case 129 ->
                    new XLstatus(129, "XL_ERR_HW_NOT_PRESENT", "硬件不存在或在通道上找不到，可能是可移动硬件或硬件故障");
            case 131 -> new XLstatus(131, "XL_ERR_NOTIFY_ALREADY_ACTIVE", "此错误代码出于历史原因存在");
            case 132 -> new XLstatus(132, "XL_ERR_INVALID_TAG", "驱动拒绝事件结构中的标记");
            case 133 -> new XLstatus(133, "XL_ERR_INVALID_RESERVED_FLD", "用户传递了一个包含保留位设置为1的结构");
            case 134 -> new XLstatus(134, "XL_ERR_INVALID_SIZE", "驱动拒绝事件结构中的大小值");
            case 135 -> new XLstatus(135, "XL_ERR_INSUFFICIENT_BUFFER", "用户传递的缓冲区太小，无法接收所有数据");
            case 136 -> new XLstatus(136, "XL_ERR_ERROR_CRC", "校验和计算失败");
            case 137 ->
                    new XLstatus(137, "XL_ERR_BAD_EXE_FORMAT", "操作系统加载可执行文件失败，例如DLL针对不同的架构编译");
            case 138 -> new XLstatus(138, "XL_ERR_NO_SYSTEM_RESOURCES", "操作系统没有足够的资源（如内存）执行请求的操作");
            case 139 -> new XLstatus(139, "XL_ERR_NOT_FOUND", "驱动找不到指定的对象");
            case 140 ->
                    new XLstatus(140, "XL_ERR_INVALID_ACCESS_140", "驱动不允许指定的访问，例如索引错误、接口版本错误或权限不足");
            case 141 -> new XLstatus(141, "XL_ERR_REQ_NOT_ACCEP", "驱动因未指定原因拒绝操作");
            case 142 -> new XLstatus(142, "XL_ERR_INVALID_LEVEL", "发生了与队列级别相关的内部错误");
            case 143 -> new XLstatus(143, "XL_ERR_NO_DATA_DETECTED", "驱动期望的数据不可用");
            case 144 -> new XLstatus(144, "XL_ERR_INTERNAL_ERROR", "发生了未指定的内部错误");
            case 145 -> new XLstatus(145, "XL_ERR_UNEXP_NET_ERROR", "发生了意外的网络错误");
            case 146 -> new XLstatus(146, "XL_ERR_INVALID_USER_BUFFER", "驱动拒绝用户空间提供的缓冲区");
            case 147 -> new XLstatus(147, "XL_ERR_INVALID_PORT_ACCESS_TYPE", "无效的端口访问类型");
            case 152 -> new XLstatus(152, "XL_ERR_NO_RESOURCES", "驱动没有足够的资源（如内存）执行请求的操作");
            case 153 -> new XLstatus(153, "XL_ERR_WRONG_CHIP_TYPE", "设备上的相关芯片不支持请求的操作");
            case 154 -> new XLstatus(154, "XL_ERR_WRONG_COMMAND", "用户发出的命令被拒绝");
            case 155 -> new XLstatus(155, "XL_ERR_INVALID_HANDLE", "用户传递了无效的句柄");
            case 157 -> new XLstatus(157, "XL_ERR_RESERVED_NOT_ZERO", "保留字段不为零");
            case 158 -> new XLstatus(158, "XL_ERR_INIT_ACCESS_MISSING", "函数调用需要初始化访问权限");
            case 160 -> new XLstatus(160, "XL_ERR_WRONG_VERSION", "检测到版本不匹配");
            case 201 ->
                    new XLstatus(201, "XL_ERR_CANNOT_OPEN_DRIVER", "加载或打开驱动程序失败，原因可能是找不到驱动文件、已加载或是先前卸载的驱动的一部分");
            case 202 ->
                    new XLstatus(202, "XL_ERR_WRONG_BUS_TYPE", "用户使用了错误的总线类型调用函数（例如尝试为CAN激活LIN通道）");
            case 203 -> new XLstatus(203, "XL_ERR_DLL_NOT_FOUND", "找不到XL API动态链接库");
            case 204 -> new XLstatus(204, "XL_ERR_INVALID_CHANNEL_MASK", "无效的通道掩码");
            case 205 -> new XLstatus(205, "XL_ERR_NOT_SUPPORTED", "不支持的函数调用");
            case 210 -> new XLstatus(210, "XL_ERR_CONNECTION_BROKEN", "驱动与远程设备失去连接");
            case 211 -> new XLstatus(211, "XL_ERR_CONNECTION_CLOSED", "连接已关闭");
            case 212 -> new XLstatus(212, "XL_ERR_INVALID_STREAM_NAME", "发生了与远程设备有关的内部错误");
            case 213 -> new XLstatus(213, "XL_ERR_CONNECTION_FAILED", "发生了与远程设备有关的内部错误");
            case 214 -> new XLstatus(214, "XL_ERR_STREAM_NOT_FOUND", "发生了与远程设备有关的内部错误");
            case 215 -> new XLstatus(215, "XL_ERR_STREAM_NOT_CONNECTED", "发生了与远程设备有关的内部错误");
            case 216 -> new XLstatus(216, "XL_ERR_QUEUE_OVERRUN", "发生了队列溢出");
            case 513 -> new XLstatus(513, "XL_ERR_INVALID_DLC", "DLC值无效");
            case 514 -> new XLstatus(514, "XL_ERR_INVALID_CANID", "CAN ID设置了无效的位");
            case 515 -> new XLstatus(515, "XL_ERR_INVALID_FDFLAG_MODE20", "为CAN20配置时不应设置的标志（如EDL）");
            case 516 -> new XLstatus(516, "XL_ERR_EDL_RTR", "RTR不应与EDL一起设置");
            case 517 -> new XLstatus(517, "XL_ERR_EDL_NOT_SET", "EDL未设置但BRS和/或ESICTRL已设置");
            case 518 -> new XLstatus(518, "XL_ERR_UNKNOWN_FLAG", "标志字段中设置了未知位");
            default -> new XLstatus(code, "UNKNOWN", "未知状态码: " + code);
        };
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return code == 0;
    }

    /**
     * 获取状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取状态消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 获取详细描述
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return message + " (" + code + "): " + description;
    }
}
