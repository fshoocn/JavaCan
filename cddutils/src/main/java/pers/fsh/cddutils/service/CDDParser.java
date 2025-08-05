package pers.fsh.cddutils.service;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import pers.fsh.cddutils.model.po.Candela;

import javax.xml.bind.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * CDD解析器
 * 用于从CDD文件中提取诊断服务信息，处理诊断实例与诊断模板的关联，并生成测试用例
 * <p>
 * 当前为基础实现，供子类继承使用
 */
@Log4j2
@Getter
public class CDDParser {
    private Candela candela;        // CDD文件的根元素
    private Map<String, Object> idMap;    // ID到对象的映射，用于快速查找元素

    /**
     * 构造函数，使用指定的XML文件路径创建CDD解析器
     *
     * @param xmlFile XML文件路径
     * @throws JAXBException         XML绑定异常
     * @throws XMLStreamException    XML流异常
     * @throws FileNotFoundException 文件未找到异常
     */
    public CDDParser(String xmlFile) throws JAXBException, XMLStreamException, FileNotFoundException {
        parseCDD(xmlFile);
    }

    /**
     * 解析CDD文件
     *
     * @param xmlFile CDD文件路径
     * @throws JAXBException         XML绑定异常
     * @throws XMLStreamException    XML流异常
     * @throws FileNotFoundException 文件未找到异常
     */
    private void parseCDD(String xmlFile) throws JAXBException, XMLStreamException, FileNotFoundException {
        // 如果文件名为空，则初始化为空值
        if (xmlFile == null || xmlFile.isEmpty()) {
            log.error("CDD文件路径为空");
            throw new RuntimeException("未找到CDD文件，路径为空");
        }

        log.info("开始解析CDD文件: {}", xmlFile);

        // 获取资源文件的输入流
        InputStream inputStream;
        try {
            // 首先尝试从文件系统加载
            inputStream = new FileInputStream(xmlFile);
            log.info("从文件系统加载CDD文件: {}", xmlFile);
        } catch (FileNotFoundException e) {
            // 如果文件系统中不存在，尝试从类路径中加载
            inputStream = CDDParser.class.getClassLoader().getResourceAsStream(xmlFile);
            if (inputStream == null) {
                log.error("无法从类路径加载CDD文件: {}", xmlFile);
                throw new FileNotFoundException("无法加载CDD文件: " + xmlFile);
            }
            log.info("从类路径加载CDD文件: {}", xmlFile);
        }

        // 创建XMLInputFactory并禁用外部实体支持和DTD支持（安全考虑）
        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        // 使用InputStreamReader包装输入流
        XMLStreamReader xsr = xif.createXMLStreamReader(new InputStreamReader(inputStream));

        // 创建JAXB上下文并进行解组操作
        JAXBContext context = JAXBContext.newInstance(Candela.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // 创建监听器，用于收集ID映射
        XmlParserListener xmlParserListener = new XmlParserListener();
        unmarshaller.setListener(xmlParserListener);

        // 可选：设置验证事件处理器
        // unmarshaller.setEventHandler(new XmlValidationEventHandler());

        // 解组XML数据到Candela对象，并记录解析时间
        long startTime = System.currentTimeMillis();
        this.candela = unmarshaller.unmarshal(xsr, Candela.class).getValue();
        long endTime = System.currentTimeMillis();

        log.info("CDD文件解析耗时: {} 毫秒", (endTime - startTime));
        log.info("从CDD文件中解析了 {} 个元素", xmlParserListener.getIdMap().size());

        this.idMap = xmlParserListener.getIdMap();
    }

    /**
     * XML解析监听器
     * 用于在解组过程中收集ID映射
     */
    @Getter
    private static class XmlParserListener extends Unmarshaller.Listener {
        /**
         * -- GETTER --
         * 获取ID映射
         *
         * @return ID到对象的映射
         */
        private final Map<String, Object> idMap = new HashMap<>();

        @Override
        public void beforeUnmarshal(Object target, Object parent) {
            // 在解组前调用
        }

        /**
         * 解组后处理方法
         * 在对象解组后被调用，用于提取对象的ID并将其存储在映射中
         *
         * @param target 解组后的目标对象
         * @param parent 父对象
         */
        @SneakyThrows
        @Override
        public void afterUnmarshal(Object target, Object parent) {

            // 通过反射判断该类中是否包含String类型的id变量，如果存在则保存到一个map中
            if (target == null) {
                return;
            }
            Class<?> clazz = target.getClass();
            Field idField = hasIdField(clazz);
            if (idField == null) {
                return;
            }
            idField.setAccessible(true);
            String idValue;
            idValue = (String) idField.get(target);
            if (idMap.containsKey(idValue)) {
                log.warn("发现重复ID: {}, 类型: {}", idValue, target.getClass().getSimpleName());
                throw new RuntimeException("ID重复: " + idValue);
            }
            if (idValue != null && !idValue.isEmpty()) {
                idMap.put(idValue, target);
//                log.debug("添加对象到ID映射: {}, 类型: {}", idValue, target.getClass().getSimpleName());
            }
        }

        /**
         * 检查类是否包含ID字段
         * 通过反射判断该类中是否包含String类型的id变量，递归检查父类
         *
         * @param clazz 要检查的类
         * @return ID字段，如果不存在则返回null
         */
        private Field hasIdField(Class<?> clazz) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getName().equals("id") && field.getType() == String.class) {
                    return field;
                }
            }
            Class<?> superclass = clazz.getSuperclass();
            if (superclass == Object.class || superclass == null) {
                return null;
            }
            return hasIdField(superclass);
        }
    }

    /**
     * XML验证事件处理器
     * 用于处理XML解析过程中的验证事件，记录未处理的标签信息
     *
     * @author fanshuhua
     * @date 2025/2/8 15:27
     */
    public static class XmlValidationEventHandler implements ValidationEventHandler {
        /**
         * 处理验证事件
         * 记录未处理的标签信息，但允许解析继续进行
         *
         * @param validationEvent 验证事件
         * @return true表示继续解析，false表示中止解析
         */
        @Override
        public boolean handleEvent(ValidationEvent validationEvent) {
            // 打印未处理的标签信息
            log.warn("未处理的标签: {}", validationEvent.getMessage());
            log.warn("严重级别: {}", validationEvent.getSeverity());
            log.warn("位置: {}:{}", validationEvent.getLocator().getLineNumber(),
                    validationEvent.getLocator().getColumnNumber());
            return true; // 返回true表示继续解析，即使有错误
        }
    }
} 