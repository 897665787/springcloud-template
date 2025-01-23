package com.company.common.util;

import com.company.common.response.SelectResp;
import com.google.common.collect.Lists;
import org.javers.common.string.PrettyValuePrinter;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;

import java.util.List;

public class ObjectCompareUtils {

    /**
     * 属性差异说明
     *
     * @param oldVersion     对象旧版本
     * @param currentVersion 对象新版本
     * @return 属性差异说明
     */
    public static String diffFieldDesc(Object oldVersion, Object currentVersion) {
        Javers javers = JaversBuilder.javers().build();
        Diff diff = javers.compare(oldVersion, currentVersion);

        PrettyValuePrinter valuePrinter = javers.getCoreConfiguration().getPrettyValuePrinter();

        StringBuilder b = new StringBuilder();
        diff.groupByObject().forEach(it -> {
            List<String> appendList = Lists.newArrayList();
            it.getNewObjects().forEach(c ->
                    appendList.add(c.prettyPrint(valuePrinter))
            );

            it.getObjectsRemoved().forEach(c ->
                    appendList.add(c.prettyPrint(valuePrinter))
            );

            it.getPropertyChanges().forEach(c ->
                    appendList.add(c.prettyPrint(valuePrinter).replace("\n", "\n  "))
            );
            b.append(String.join("|", appendList));
        });

        return b.toString();
    }

    public static void main(String[] args) {
        SelectResp<String> oldVersion = new SelectResp<String>().setCode("dddd").setName("22");
//        SelectResp<String> oldVersion = null;
        SelectResp<String> currentVersion = new SelectResp<String>().setCode("33").setName("44");
//        SelectResp<String> currentVersion = null;
        System.out.println(ObjectCompareUtils.diffFieldDesc(oldVersion, currentVersion));

        // 暂不支持
//        List<SelectResp<String>> oldVersionList = Lists.newArrayList(oldVersion, currentVersion);
//        List<SelectResp<String>> currentVersionList = Lists.newArrayList(currentVersion);
//        System.out.println(ObjectCompareUtils.diffFieldDesc(oldVersionList, currentVersionList));

    }
}