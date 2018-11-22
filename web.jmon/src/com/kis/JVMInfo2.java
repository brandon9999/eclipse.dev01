package com.kis;

import java.util.Hashtable;
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


public class JVMInfo2 
{
    public void showInfo(MBeanServerConnection mbCon, String svrName)
    throws Exception 
    {
        System.out.println("=== JVM Info2 Statistics ===");
    	
    	
		JVMMBean jb = null;
		try{
			Hashtable attr = new Hashtable();	
			attr.put("j2eeType","JVM");
			attr.put("JMXManager", svrName);
			attr.put("J2EEServer",svrName);
			attr.put("name",svrName);
			
			ObjectName objNames = new ObjectName("JEUS",attr);
			
			jb = (JVMMBean)MBeanServerInvocationHandler.newProxyInstance(mbCon, objNames, JVMMBean.class, false );
			
            System.out.println( " Status : " + jb.getjavaVendor());
            System.out.println( " Status : " + jb.getjavaVersion());
            System.out.println( " Status : " + jb.getOSName());
            System.out.println( " Status : " + jb.getOSVersion());
            System.out.println( " Status : " + jb.getAllProperties());
            System.out.println( " Status : " + jb.getSystemInfo().toString());
            System.out.println( " Status : " + jb.getstats());			
            System.out.println( " Status : " + jb.getStats());		
            
            JVMStatsImpl jvmstatsimpl = (JVMStatsImpl) jb.getstats();
            javax.management.j2ee.statistics.RangeStatistic totalSize = jvmstatsimpl.getTotalSize();
            javax.management.j2ee.statistics.BoundedRangeStatistic heapSize = jvmstatsimpl.getHeapSize();
            javax.management.j2ee.statistics.CountStatistic upTime = jvmstatsimpl.getUpTime();
            
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
            
		}catch(Exception ex){
			ex.printStackTrace();
		}
    }
}

