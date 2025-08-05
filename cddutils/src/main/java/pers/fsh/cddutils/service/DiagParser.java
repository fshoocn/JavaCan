package pers.fsh.cddutils.service;

import lombok.extern.log4j.Log4j2;
import pers.fsh.cddutils.model.po.ecudoc.Did;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.Ident;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.TextTbl;
import pers.fsh.cddutils.model.po.ecudoc.dcltmpls.DclProxyBase;
import pers.fsh.cddutils.model.po.ecudoc.dcltmpls.DclSrvTmpl;
import pers.fsh.cddutils.model.po.ecudoc.dcltmpls.Dcltmpl;
import pers.fsh.cddutils.model.po.ecudoc.ecu.DiagInst;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.DiagInstBaseComp;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.SimpleCompCont;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.StaticValue;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont.*;
import pers.fsh.cddutils.model.po.ecudoc.negrescodes.NegResCode;
import pers.fsh.cddutils.model.po.ecudoc.protocolservices.*;
import pers.fsh.cddutils.model.po.ecudoc.recorddtpool.Record;
import pers.fsh.cddutils.model.po.ecudoc.recorddtpool.Recorddt;
import pers.fsh.cddutils.model.vo.DiagInstVo;
import pers.fsh.cddutils.model.vo.DiagVo;
import pers.fsh.cddutils.model.vo.parameter.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DiagParser 类用于解析诊断实例 (DiagInst)
 * 它利用 CDDParser 提供的数据将ECU-DOC中的诊断定义转换为更易于使用的视图模型 (DiagInstVo)。
 *
 * @author fanshuhua
 * @date 2025/7/19 14:05
 */

@Log4j2
public class DiagParser {
    private final CDDParser cdd;

    /**
     * DiagParser 的构造函数。
     *
     * @param cdd 一个已初始化的 CDDParser 实例，用于从CDD文件中查找ID对应的对象。
     */
    public DiagParser(CDDParser cdd) {
        this.cdd = cdd;
    }

    public List<DiagInstVo> getDiagInstList() {
        List<DiagInstVo> diagInstVoList = new ArrayList<>();
        cdd.getCandela().getEcudoc().getEcu().getVar().getDiagInstList().forEach(d -> {
            if (d.getServiceList() == null || d.getServiceList().isEmpty()) {
                log.warn("DiagInst {} 中没有Service实例", d);
                return;
            }
            try {
                DiagInstVo diagInstVo = getDiagInstVo(d);
                diagInstVoList.add(diagInstVo);
            } catch (Exception e) {
                log.error("解析DiagInst失败: {}", d, e);
            }
        });
        cdd.getCandela().getEcudoc().getEcu().getVar().getDiagClassList().forEach(dc -> {
            if (dc.getDiagInstList() == null || dc.getDiagInstList().isEmpty()) {
                log.warn("DiagClass {} 中没有DiagInst实例", dc);
                return;
            }
            dc.getDiagInstList().forEach(d -> {
                if (d.getServiceList() == null || d.getServiceList().isEmpty()) {
                    log.warn("DiagInst {} 中没有Service实例", d);
                    return;
                }
                try {
                    DiagInstVo diagInstVo = getDiagInstVo(d);
                    diagInstVoList.add(diagInstVo);
                } catch (Exception e) {
                    log.error("解析DiagClass中的DiagInst失败: {}", d, e);
                }
            });
        });
        return diagInstVoList;
    }


    /**
     * 将 DiagInst 对象转换为 DiagInstVo 视图对象。
     * 这个方法会处理模板引用、代理参数替换等逻辑。
     *
     * @param diagInst 从ECU-DOC解析出的诊断实例对象。
     * @return 一个填充了诊断服务信息的 DiagInstVo 对象。
     */
    private DiagInstVo getDiagInstVo(DiagInst diagInst) {
        // 1. 提取模板，将DCLTMP中的所有DclSrvTmpl转换为DiagVo
        List<DiagVo> diagVoList = getIdFromCdd(diagInst.getTmplref(), Dcltmpl.class).getDclSrvTmplList().stream()
                .map(this::ConverProtocolservices2DiagVo)
                .collect(Collectors.toList());

        // 2. 提取所有使用到的参数，并用Map进行管理，以提高查找效率
        Map<String, ParameterInterface> parameterMap = diagVoList.stream()
                .flatMap(vo -> Stream.of(vo.getReq(), vo.getPos(), vo.getNeg()).flatMap(List::stream))
                .distinct()
                .collect(Collectors.toMap(ParameterInterface::getId, Function.identity(), (existing, replacement) -> existing));

        // 3. 根据DCLTMPL中的代理映射关系，修改DiagVoList中的参数
        applyProxyMappings(diagInst, diagVoList, parameterMap);

        // 4. 遍历compList，然后根据映射关系填充DiagVoList内的数据
        applyComponentValues(diagInst, parameterMap);

        // 5. 根据diagInst中的SERVICE判断DiagVo是否被激活
        Map<String, DiagVo> diagVoMap = diagVoList.stream()
                .collect(Collectors.toMap(DiagVo::getId, Function.identity()));
        diagInst.getServiceList().forEach(s -> {
            DiagVo dv = diagVoMap.get(s.getTmplref());
            if (dv != null) {
                dv.setActive(true);
            }
        });
        return new DiagInstVo().setDiagVoList(diagVoList).setName(diagInst.getName());
    }

    /**
     * 应用代理参数映射
     *
     * @param diagInst     诊断实例
     * @param diagVoList   DiagVo列表
     * @param parameterMap 参数ID到参数对象的映射
     */
    private void applyProxyMappings(DiagInst diagInst, List<DiagVo> diagVoList, Map<String, ParameterInterface> parameterMap) {
        Dcltmpl dcltmpl = getIdFromCdd(diagInst.getTmplref(), Dcltmpl.class);
        List<DclProxyBase> proxyList = dcltmpl.getProxyList();

        proxyList.forEach(proxy -> {
            // 查找被代理的原始参数
            ParameterInterface originalParam = proxy.getRef().stream()
                    .map(parameterMap::get)
                    .filter(java.util.Objects::nonNull).findFirst()
                    .orElseThrow(() -> new RuntimeException("在parameterMap中未找到代理 " + proxy.getId() + " 引用的参数: " + proxy.getRef()));

            if (originalParam != null) {
                // 更新参数ID为代理ID
                originalParam.setId(proxy.getId());
                // 建立所有旧ID到新参数实例的映射
                Map<String, ParameterInterface> replacements = proxy.getRef().stream()
                        .collect(Collectors.toMap(Function.identity(), id -> originalParam));

                // 在所有服务中进行替换
                diagVoList.forEach(vo -> {
                    replaceParametersInList(vo.getReq(), replacements);
                    replaceParametersInList(vo.getPos(), replacements);
                    replaceParametersInList(vo.getNeg(), replacements);
                });
                // 更新主参数Map
                parameterMap.put(proxy.getId(), originalParam);
                proxy.getRef().forEach(parameterMap::remove);
            }
        });
    }

    /**
     * 应用组件值到参数上
     *
     * @param diagInst     诊断实例
     * @param parameterMap 参数ID到参数对象的映射
     */
    private void applyComponentValues(DiagInst diagInst, Map<String, ParameterInterface> parameterMap) {
        diagInst.getCompList().forEach(comp -> applyComponentValue(comp, parameterMap));
    }


    /**
     * 在参数列表中，将所有ID匹配的参数替换为指定的parameter实例。
     *
     * @param list         参数列表
     * @param replacements 要替换的ID及对应参数的Map
     */
    private void replaceParametersInList(List<ParameterInterface> list, Map<String, ParameterInterface> replacements) {
        for (int i = 0; i < list.size(); i++) {
            ParameterInterface current = list.get(i);
            if (replacements.containsKey(current.getId())) {
                list.set(i, replacements.get(current.getId()));
            }
        }
    }

    /**
     * 将ECU-DOC中的Service对象转换为自定义的DiagVo视图对象。
     *
     * @param dclSrvTmpl 诊断服务映射实例
     * @return 转换后的DiagVo对象
     */
    private DiagVo ConverProtocolservices2DiagVo(DclSrvTmpl dclSrvTmpl) {
        //1. 根据Service中的模板引用，从CDD中查找对应的ProtocolService模板
        ProtocolService protocolservice = getIdFromCdd(
                dclSrvTmpl.getTmplref(),
                ProtocolService.class);
        //2. 生成请求、肯定响应、否定响应的参数列表
        List<ParameterInterface> reqParameterlist = protocolservice.getReq().getComponents().stream().map(this::Object2ParameterInterface).collect(Collectors.toList());
        List<ParameterInterface> posParameterlist = protocolservice.getPos().getComponents().stream().map(this::Object2ParameterInterface).collect(Collectors.toList());
        List<ParameterInterface> negParameterlist = protocolservice.getNeg().getComponents().stream().map(this::Object2ParameterInterface).collect(Collectors.toList());

        DiagVo diagVo = new DiagVo();
        diagVo.setName(protocolservice.getName())
                .setId(dclSrvTmpl.getId())
                .setQualifiers(protocolservice.getQual())
                .setPhys(protocolservice.getPhys() > 0)
                .setFunc(protocolservice.getFunc() > 0)
                .setReq(reqParameterlist)
                .setPos(posParameterlist)
                .setNeg(negParameterlist);
        return diagVo;
    }

    /**
     * 根据DiagInstBaseComp更新parameterMap中对应的ParameterInterface
     *
     * @param comp         DiagInst中的组件
     * @param parameterMap 所有参数的Map
     */
    private void applyComponentValue(DiagInstBaseComp comp, Map<String, ParameterInterface> parameterMap) {
        if (comp == null) {
            throw new RuntimeException("DiagInstBaseComp 不能为空");
        }

        String refId = comp.getRef();
        if (refId == null)
            log.error("DiagInstBaseComp 的 refId 不能为空: " + comp.getClass().getName());
        if (!parameterMap.containsKey(refId))
            log.error("DiagInstBaseComp 的 refId 在 parameterMap 中未找到: " + refId);
        ParameterInterface p = parameterMap.get(refId);
        if (comp instanceof StaticValue staticValue) {
            if (p instanceof ValueInterface<?> valueInterface) {
                valueInterface.setValue(staticValue.getV());
            }
        } else {
            SimpleCompCont simpleCompCont = (SimpleCompCont) comp;
            if (simpleCompCont.getObjList() == null || simpleCompCont.getObjList().isEmpty()) {
                log.warn("SimpleCompCont 中没有对象列表: {}", simpleCompCont);
                return;
            }
            if (p instanceof ParameterProxy parameterProxy) {
                simpleCompCont.getObjList().forEach(o -> {
                    parameterProxy.addParameter(Object2ParameterInterface(o));
                });
            }
        }
    }

    private ParameterInterface Object2ParameterInterface(Object o) {
        if (o instanceof ConstComp constComp) {
            return new ParameterConstant()
                    .setId(constComp.getId())
                    .setBitLength(constComp.getBl())
                    .setValue(constComp.getV())
                    .setName(constComp.getName())
                    .setQualifiers(constComp.getQual());
        } else if (o instanceof SimpleProxyComp simpleProxyComp) {
            return new ParameterProxy(simpleProxyComp.getName(), simpleProxyComp.getQual())
                    .setId(simpleProxyComp.getId())
                    .setMinBitLength(simpleProxyComp.getMinbl() == null ? 0 : simpleProxyComp.getMinbl())
                    .setMaxBitLength(simpleProxyComp.getMaxbl() == null ? 0 : simpleProxyComp.getMaxbl());
        } else if (o instanceof StaticComp staticComp) {
            ParameterInterface parameterInterface = Object2ParameterInterface(getIdFromCdd(staticComp.getDtref(), Ident.class));
            parameterInterface.setId(staticComp.getId())
                    .setName(staticComp.getName())
                    .setQualifiers(staticComp.getQual());
            return parameterInterface;
        } else if (o instanceof GroupOfDtcProxyComp groupOfDtcProxyComp) {
            String ref = groupOfDtcProxyComp.getRef();
            Ident idFromCdd = getIdFromCdd(ref, Ident.class);
            ParameterInterface parameterInterface = Object2ParameterInterface(idFromCdd);
            parameterInterface.setId(groupOfDtcProxyComp.getId())
                    .setName(groupOfDtcProxyComp.getName())
                    .setQualifiers(groupOfDtcProxyComp.getQual());
            return parameterInterface;
        } else if (o instanceof EosIterComp eosIterComp) {
            ParameterProxy parameterProxy = new ParameterProxy(eosIterComp.getName(), eosIterComp.getQual())
                    .setId(eosIterComp.getId())
                    .setMinSize(eosIterComp.getMinNumOfItems());
            eosIterComp.getChildrenList().forEach(comp -> {
                parameterProxy.addParameter(Object2ParameterInterface(comp));
            });
            return parameterProxy;
        }
//        else if (o instanceof DomainDataProxyComp domainDataProxyComp){
//            // DomainDataProxyComp可能使用FAULTMEMORY里面的值，而不是直接使用id关联？
//            return
//        }
        // ================DiagInst 里面的对象
        else if (o instanceof DidDataRef didDataRef) {
            Did did = getIdFromCdd(didDataRef.getDidRef(), Did.class);
            ParameterDid parameterDid = new ParameterDid();
            parameterDid
                    .setDid(did.getN())
                    .setId(did.getId())
                    .setName(did.getName())
                    .setQualifiers(did.getQual());
            did.getStructure().forEach(e -> {
                parameterDid.addParameter(Object2ParameterInterface(e));
            });
            return parameterDid;
        } else if (o instanceof SpecDataObj specDataObj) {
            ParameterProxy parameterProxy = new ParameterProxy();
            parameterProxy.setName(specDataObj.getName())
                    .setQualifiers(specDataObj.getQual())
                    .setSerial(false);
            specDataObj.getRef().forEach(p -> {
                NegResCode nrcCode = getIdFromCdd(p.getIdref(), NegResCode.class);
                parameterProxy.addParameter(Object2ParameterInterface(nrcCode));
            });
            return parameterProxy;
        } else if (o instanceof Struct struct) {
            log.debug(struct);
            Ident idFromCdd = getIdFromCdd(struct.getRef(), Ident.class);
            ParameterStruct parameterStruct = new ParameterStruct(idFromCdd.getCValueType(), idFromCdd.getPValueType(), struct.getName(), struct.getQual());
            struct.getObjList().forEach(p -> {
                parameterStruct.addParameter(Object2ParameterInterface(p));
            });
            return parameterStruct;
        } else if (o instanceof DataObj dataObj) {
            Ident idFromCdd = getIdFromCdd(dataObj.getDtref(), Ident.class);
            ParameterInterface parameterInterface = Object2ParameterInterface(idFromCdd);
            if (parameterInterface instanceof ValueInterface<?> valueInterface) {
                valueInterface.setValue(dataObj.getV());
            }
            return parameterInterface;
        } else if (o instanceof RecordDataObj recordDataObj) {
            ParameterProxy parameterProxy = new ParameterProxy(recordDataObj.getName(), recordDataObj.getQual());
            recordDataObj.getRecorddtPool().forEach(recorddt -> {
                parameterProxy.addParameter(Object2ParameterInterface(recorddt));
            });
            return parameterProxy;
        } else if (o instanceof Recorddt recorddt) {
            ParameterDtc parameterDtc = new ParameterDtc(recorddt.getCValueType(), recorddt.getPValueType(), recorddt.getName(), recorddt.getQual());
            recorddt.getRecords().forEach(record -> {
                parameterDtc.addParameter(Object2ParameterInterface(record));
            });
            return parameterDtc;
        } else if (o instanceof Record record) {
            return new ParameterConstant()
                    .setName(record.getName())
                    .setId(record.getId())
                    .setValue(record.getV());
        } else if (o instanceof NegResCode negResCode) {
            return new ParameterConstant(negResCode.getName(), negResCode.getQual())
                    .setId(negResCode.getId())
                    .setBitLength(8)
                    .setValue(negResCode.getV());
        }
        // ================DataType 里面的对象
        else if (o instanceof TextTbl textTbl) {
            return new ParameterTextTable(
                    textTbl.getCValueType(),
                    textTbl.getPValueType(),
                    textTbl.getTextMapList()
            ).setExclList(textTbl.getExclList())
                    .setBm(textTbl.getBm())
                    .setId(textTbl.getId())
                    .setName(textTbl.getName())
                    .setQualifiers(textTbl.getQual());
        } else if (o instanceof Ident ident) {
            return new ParameterIdent(ident.getCValueType(), ident.getPValueType())
                    .setExclList(ident.getExclList())
                    .setBm(ident.getBm())
                    .setId(ident.getId())
                    .setName(ident.getName())
                    .setQualifiers(ident.getQual());
        } else {
            throw new RuntimeException("不支持的对象类型: " + o.getClass().getName() + "数据：" + o);
        }
    }


    /**
     * 从CDD解析器中根据ID获取对象，并进行类型检查和转换。
     *
     * @param id    要查找的对象的ID
     * @param clazz 期望的对象类型
     * @param <T>   对象的泛型类型
     * @return 查找到并成功转换的对象
     * @throws RuntimeException 如果CDD未初始化、ID未找到或类型不匹配
     */
    private <T> T getIdFromCdd(String id, Class<T> clazz) {
        if (cdd == null || cdd.getIdMap() == null) {
            throw new RuntimeException("CDD或ID映射表未初始化");
        }
        if (!cdd.getIdMap().containsKey(id)) {
            throw new RuntimeException("未在ID映射表中找到对应数据 id: " + id);
        }
        Object o = cdd.getIdMap().get(id);
        if (!clazz.isInstance(o)) {
            throw new RuntimeException("ID: " + id + ", 对应数据类型错误, 预期是: " + clazz.getName() + ", 实际是: " + o.getClass().getName());
        }
        return clazz.cast(o);
    }
}
