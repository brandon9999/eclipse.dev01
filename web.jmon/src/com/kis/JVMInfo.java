package com.kis;

import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.j2ee.statistics.BoundedRangeStatistic;
import javax.management.j2ee.statistics.CountStatistic;
import javax.management.j2ee.statistics.RangeStatistic;

import jeus.management.j2ee.JVMMBean;
import jeus.management.j2ee.statistics.JVMStatsImpl;


public class JVMInfo {
    
    public void showInfo(MBeanServerConnection mBeanServer, String targetName)
    throws Exception {
		System.out.println( "=================================================");
		System.out.println( "[JVM Info]" + targetName + " : ");
		System.out.println( "=================================================");


        ObjectName objectName 
            = new ObjectName("JEUS:j2eeType=JVM,name=" + targetName + ",*");
        Set jvmMBeans = mBeanServer.queryMBeans(objectName, null);

        for (Iterator i = jvmMBeans.iterator(); i.hasNext();) {
            ObjectName objName = ((ObjectInstance) i.next()).getObjectName();
            System.out.println("[MBean] " + objName);
            
            // JVMMBean Stats
            JVMMBean jvm = (JVMMBean) MBeanServerInvocationHandler
                .newProxyInstance(mBeanServer, objName, JVMMBean.class, false);

            JVMStatsImpl jvmstatsimpl = (JVMStatsImpl) jvm.getstats();
            RangeStatistic totalSize = jvmstatsimpl.getTotalSize();
            BoundedRangeStatistic heapSize = jvmstatsimpl.getHeapSize();
            CountStatistic upTime = jvmstatsimpl.getUpTime();

            // JVM Total Size
            System.out.println("[Total Size]");
            System.out.println("-unit : " + totalSize.getUnit());
            System.out.println("-current : " + totalSize.getCurrent());
            System.out.println("-min size : " + totalSize.getLowWaterMark());
            System.out.println("-max size : " + totalSize.getHighWaterMark());

            // JVM Heap Size
            System.out.println("[Heap Size]");
            System.out.println("-unit : " + heapSize.getUnit());
            System.out.println("-current : " + heapSize.getCurrent());
            System.out.println("-min Size : " + heapSize.getLowWaterMark());
            System.out.println("-max Size : " + heapSize.getHighWaterMark());
            System.out.println("-lower bound : " + heapSize.getLowerBound());
            System.out.println("-upper bound : " + heapSize.getUpperBound());

            // JVM UpTime
            System.out.println("[Up Time]");
            System.out.println("-unit : " + upTime.getUnit());
            System.out.println("-count : " + upTime.getCount());
            System.out.println("-start time : " + upTime.getStartTime());
        }
    }
}

