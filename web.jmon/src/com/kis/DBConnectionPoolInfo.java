package com.kis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.j2ee.statistics.JDBCConnectionPoolStats;
import javax.management.j2ee.statistics.JDBCStats;
import javax.management.j2ee.statistics.RangeStatistic;
import javax.management.remote.JMXConnector;
import javax.naming.Context;
import javax.naming.InitialContext;

import jeus.management.JMXConstants;
import jeus.management.j2ee.JDBCResourceMBean;

public class DBConnectionPoolInfo {
    public void showInfo(MBeanServerConnection mBeanServer, String targetName) throws Exception {
        ObjectName dbstats = new ObjectName("JEUS:j2eeType=JDBCResource,*");
        Set mbeans = mBeanServer.queryMBeans(dbstats, null);

        for (Iterator iter = mbeans.iterator(); iter.hasNext();) {
            ObjectName jdbcResourceMBeanName = ((ObjectInstance) iter.next()).getObjectName();
            JDBCResourceMBean jdbcResource = (JDBCResourceMBean) MBeanServerInvocationHandler.newProxyInstance(
                    mBeanServer,
                    jdbcResourceMBeanName,
                    JDBCResourceMBean.class,
                    false);
            JDBCStats jdbcStats = (JDBCStats) jdbcResource.getstats();
            SimpleDateFormat format = new SimpleDateFormat("[MM-dd]HH:mm:ss");
            StringBuilder builder = new StringBuilder();

            JDBCConnectionPoolStats[] cpStatsArray = jdbcStats.getConnectionPools();
            if (cpStatsArray != null && cpStatsArray.length > 0) {
                builder.append("[STA] ");
                for (JDBCConnectionPoolStats cpStats : cpStatsArray) {
                    RangeStatistic disposableStat = (RangeStatistic) cpStats.getStatistic("DisposableConnectionSize");
                    String output =format.format(new Date(System.currentTimeMillis()))
                        + " name:[" +cpStats.getJdbcDataSource() + "]"
                        + " total:[" +cpStats.getPoolSize().getCurrent() +"]"
                        + " use:[" + (cpStats.getPoolSize().getCurrent()- cpStats.getFreePoolSize().getCurrent()) +"]"
                        + " disposable:[" + disposableStat.getCurrent() +"]";
                    builder.append(output);
                }
                builder.append("\n");

                System.out.println(builder.toString());
            } else {
                System.out.println("no created connection pool");
            }
        }
    }
}
