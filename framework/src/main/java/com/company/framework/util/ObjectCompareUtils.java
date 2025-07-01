package com.company.framework.util;

import java.util.List;

import org.javers.common.string.PrettyValuePrinter;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;

import com.google.common.collect.Lists;

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
}
