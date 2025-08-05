package pers.fsh.cddutils;

import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import pers.fsh.cddutils.model.vo.DiagInstVo;
import pers.fsh.cddutils.service.CDDParser;
import pers.fsh.cddutils.service.DiagParser;

import java.util.List;

/**
 * CDD助手应用
 * 用于解析CDD文件中的诊断数据
 */
@Log4j2
public class App {

    @SneakyThrows
    public static void main(String[] args) {

        // 示例CDD文件路径，可以通过命令行参数传入
//        String cddFilePath = args.length > 0 ? args[0] : "E:\\idea_workspce\\JavaCanDemo2\\cddHelper2\\src\\main\\resources\\1.xml";
        String cddFilePath = args.length > 0 ? args[0] : "E:\\idea_workspce\\JavaCanDemo2\\cddHelper2\\src\\main\\resources\\BLE.app_20250318.cdd";
//        String cddFilePath = "E:\\CANoe_workspace\\CB-IHU12-D5\\cdd\\IHU12-D5.cdd";
        // 创建CDD解析器
        CDDParser cddParser = new CDDParser(cddFilePath);
        log.info("成功加载CDD文件: {}", cddFilePath);

//        log.info(cddParser.getCandela().getEcudoc().getDids().get(6));

        DiagParser diagParser = new DiagParser(cddParser);
        List<DiagInstVo> diagInstList = diagParser.getDiagInstList();
        log.info("诊断实例解析完成: {}", JSON.toJSONString(diagInstList.get(4)));
    }
}
