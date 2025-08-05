import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import pers.fsh.deviceutils.hardware.vectorhardware.VectorDriverImpl;
import pers.fsh.deviceutils.model.Result;
import pers.fsh.deviceutils.model.hardwareconfig.CanFdConfig;
import pers.fsh.deviceutils.model.hardwareconfig.HardwareConfigInterface;

import java.util.List;

import static java.lang.Thread.sleep;

/**
 * @author fanshuhua
 * @date 2025/8/5 11:40
 */

@Log4j2
public class AppTest {

    @Test
    public void testApp() throws InterruptedException {
        VectorDriverImpl vectorDriver = new VectorDriverImpl();
        vectorDriver.init();
        List<HardwareConfigInterface> hardwareList = vectorDriver.getHardwareList();
        hardwareList.forEach(e -> {
            log.info("硬件配置: {}", e);
        });
        List<HardwareConfigInterface> testHardwareList = hardwareList.subList(0, Math.min(2, hardwareList.size()));
        for (HardwareConfigInterface canFdConfig : testHardwareList) {
            if (canFdConfig instanceof CanFdConfig canFdConfig1) {
                canFdConfig1.setArbitrationBitRate(500000)
                        .setSjwAbr(32)
                        .setTseg1Abr(127)
                        .setTseg2Abr(32)
                        .setDataBitRate(2000000)
                        .setSjwDbr(8)
                        .setTseg1Dbr(31)
                        .setTseg2Dbr(8);

            }
        }
        Result<String> connect = vectorDriver.connect(testHardwareList);
        log.info("连接结果: " + connect);
        vectorDriver.setMsgListener(msg -> {
            log.info("接收到消息: " + msg);
        });
        sleep(60000);
    }
}
