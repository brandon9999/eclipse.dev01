package com.kis;

import java.util.*;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ObjectInstance;
import javax.management.MalformedObjectNameException;
import javax.management.MBeanServerInvocationHandler;

import jeus.management.j2ee.servlet.*;

public class ServletThreadInfo 
{
    public long activeThreadCnt = 0;
	
	
    public void showInfo(MBeanServerConnection mBeanServer, String targetName)
    throws Exception {
        System.out.println("=== Servlet Thread Info ===");

        // Get the object name of the servlet engine using JMX Standard API 
        // Otherwise, the object name could be queried through MBeanServer.
        // Please see JEUS MBean API javadoc for more concrete name, key properties.
        ObjectName name = new ObjectName("JEUS:jeusType=WebEngine,J2EEServer=" + targetName + ",*");
        Set names = mBeanServer.queryMBeans(name, null);
        if (names == null || names.size() == 0) {
            System.out.println("there is no servlet engine");
            return;
        }

        // Choose one servlet engine from the returned object names
        Iterator it = names.iterator();
        ObjectName fullName = null;
        while (it.hasNext()) {
            fullName = ((ObjectInstance)it.next()).getObjectName();
            break;
        }

        assert fullName != null;

        WebEngineMoMBean engineMBean = (WebEngineMoMBean)
            MBeanServerInvocationHandler.newProxyInstance(mBeanServer,
                    fullName, WebEngineMoMBean.class, false);

        // Get listeners from WebEngine
        String[] listeners = engineMBean.getWebListeners();
        if(listeners == null || listeners.length == 0){
        	return;
        }
        
        String webEngineName = engineMBean.getObjectName().getKeyProperty("name");
        for(int i = 0; i < listeners.length; i++) {
        	ObjectName listener = new ObjectName(listeners[i]);
        	
            WebListenerMoMBean listenerMBean
            = (WebListenerMoMBean) 
            MBeanServerInvocationHandler.newProxyInstance(mBeanServer,
            listener, WebListenerMoMBean.class, false);
            String[] tpoolNames = listenerMBean.getThreadPools();

            // Get stats from thread pools
            if (tpoolNames != null) {
            	showThreadPoolStats(mBeanServer, webEngineName, tpoolNames);
            }        	
        }
        System.out.println();
        // Get context groups of the servlet engine
//        String[] groups = engineMBean.getContextGroups();
//        if (groups != null && groups.length > 0) {
//            for (int i = 0; i < groups.length; i++) {
//                ObjectName group = new ObjectName(groups[i]);
//                ContextGroupMoMBean groupMBean
//                    = (ContextGroupMoMBean) MBeanServerInvocationHandler.newProxyInstance(mBeanServer,
//                    group, ContextGroupMoMBean.class, false);
//
//                String contextGroupName
//                    = groupMBean.getObjectName().getKeyProperty("name");
//
//                // Get listeners from the context group
//                String[] listeners = groupMBean.getWebListeners();
//                if (listeners == null || listeners.length == 0) {
//                    return;
//                }
//
//                // Get thread pools from the listeners
//                for (int j = 0; j < listeners.length; j++) {
//                    ObjectName listener = new ObjectName(listeners[j]);
//                    WebListenerMoMBean listenerMBean
//                        = (WebListenerMoMBean) 
//                        MBeanServerInvocationHandler.newProxyInstance(mBeanServer,
//                        listener, WebListenerMoMBean.class, false);
//                    String[] tpoolNames = listenerMBean.getThreadPools();
//
//                    // Get stats from thread pools
//                    if (tpoolNames != null) {
//                        showThreadPoolStats(mBeanServer, contextGroupName, 
//                            tpoolNames);
//                    }
//                }
//                System.out.println("");
//            }
//        }
    }

    private void showThreadPoolStats(MBeanServerConnection mBeanServer, 
                                     String contextGroupName, 
                                     String[] tpoolNames) 
    throws MalformedObjectNameException {
        for (int k = 0; k < tpoolNames.length; k++) {
            ObjectName tpool = new ObjectName(tpoolNames[k]);
            System.out.println("[MBean] " + tpool);
            ThreadPoolMoMBean tpoolMBean 
                = (ThreadPoolMoMBean)
                MBeanServerInvocationHandler.newProxyInstance(mBeanServer,
                        tpool, ThreadPoolMoMBean.class, false);
            ThreadPoolStatsImpl stats 
                = (ThreadPoolStatsImpl) tpoolMBean.getstats();
            System.out.println("Listener: " + contextGroupName 
                + "/" + tpool.getKeyProperty("name"));
            System.out.println("- current thread count      : " 
                + stats.getAllThreadCount().getCount());
            System.out.println("- max thread count          : " 
                + stats.getMaxThreadCount().getCount());

            //////////////////////////////////////////////////////////////
            if (tpool.getKeyProperty("name").equals("http1"))
            {
	            this.activeThreadCnt = stats.getActiveThreadCount().getCount();
	            //System.out.println("- active thread count          : " 
	            //        + this.activeThreadCnt);
            }
            //////////////////////////////////////////////////////////////
            
            
            
            if (stats.getStatisticVersion() == ThreadPoolStatsImpl.NIO_VERSION) {
                // pipeline
                System.out.println("- total connection count    : " 
                    + stats.getTotalConnectionCount().getCount());
                System.out.println("- max queue count           : " 
                    + stats.getMaxQueueCount().getCount());
                System.out.println("- current queue count       : " 
                    + stats.getCurrentQueueCount().getCount());
                System.out.println("- remain queue count        : " 
                    + stats.getRemainQueueCount().getCount());
                System.out.println("- peak queue count          : " 
                    + stats.getPeakQueueCount().getCount());
                System.out.println("- total queue count         : " 
                    + stats.getTotalQueueCount().getCount());
                System.out.println("- difference queue 1m count  : " 
                    + stats.getDifferenceQueue1MCount().getCount());
                System.out.println("- difference queue 5m count  : " 
                    + stats.getDifferenceQueue5MCount().getCount());
                System.out.println("- difference queue 15m count : " 
                    + stats.getDifferenceQueue15MCount().getCount());
                System.out.println("- overflow queue count      : " 
                    + stats.getOverflowCount().getCount());
                System.out.println("- average queue time        : " 
                    + stats.getQueueWaitTimeAverage().getCount() + "(ms)");
            } else {
                System.out.println("- wait queue count          : " 
                    + stats.getWaitQueueCount().getCount());
            }

            System.out.println();
        }
    }
}
