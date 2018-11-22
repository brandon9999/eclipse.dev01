package com.kis;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.j2ee.statistics.TimeStatistic;
import javax.management.j2ee.statistics.RangeStatistic;
import javax.naming.*;

import jeus.jndi.JNSConstants;
import jeus.management.JMXConstants;
import jeus.management.RemoteMBeanServerFactory;
import jeus.management.j2ee.thread.ThreadPoolMBean;
import jeus.management.j2ee.thread.stats.ThreadPoolStats;


public class ThreadPoolInfo {
    public void showInfo(MBeanServerConnection mBeanServer, String name)
    throws Exception {
        System.out.println("=== ThreadPool Info ===");

        // Get the object names of the thread pools. 
        // Please see JEUS MBean API javadoc for more concrete name, key 
        // properties.
        ObjectName objectNames = new ObjectName("JEUS:jeusType=ThreadPool,*");
        Set tpMBeans = mBeanServer.queryMBeans(objectNames, null);

        for (Iterator i = tpMBeans.iterator(); i.hasNext();) {
            ObjectName mbeanName = ((ObjectInstance) i.next()).getObjectName();

            // thread pool name
            System.out.println("[Thread-pool : " 
                + mbeanName.getKeyProperty("name") + "]");

            System.out.println("[MBean] " + mbeanName);

            ThreadPoolMBean pool = (ThreadPoolMBean)
                MBeanServerInvocationHandler.newProxyInstance(mBeanServer,
                mbeanName, ThreadPoolMBean.class, false);

            // ThreadPool Size
            System.out.println("-size : " + pool.getPoolSize());
            System.out.println("-core size : " + pool.getCorePoolSize());
            System.out.println("-largest Size : " + pool.getLargestPoolSize());
            System.out.println("-max size : " + pool.getMaximumPoolSize());
            System.out.println("-queue size : " + pool.getWorkQueueSize());

            // ThreadPool Stats
            ThreadPoolStats stats = (ThreadPoolStats) pool.getstats();
            TimeStatistic executionTimeStats = stats.getThreadExecutionTime();
            TimeStatistic waitingTimeStats = stats.getQueueWaitingTime();
            //RangeStatistic queueSizeStas = stats.getWaitingQueueSize();

            System.out.println("# Thread Execution Time Stats");
            System.out.println("--unit : " + executionTimeStats.getUnit());
            System.out.println("--count : " + executionTimeStats.getCount());
            System.out.println("--min time : " + executionTimeStats.getMinTime());
            System.out.println("--max time : " + executionTimeStats.getMaxTime());

            System.out.println("# Queue Waiting Time Stats");
            System.out.println("--unit : " + waitingTimeStats.getUnit());
            System.out.println("--count : " + waitingTimeStats.getCount());
            System.out.println("--min time : " + waitingTimeStats.getMinTime());
            System.out.println("--max time : " + waitingTimeStats.getMaxTime());
        }
   }
}

