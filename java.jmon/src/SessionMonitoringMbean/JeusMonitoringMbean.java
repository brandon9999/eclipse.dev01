package SessionMonitoringMbean;

import java.util.Hashtable;
import java.util.Properties;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.naming.Context;
import javax.naming.InitialContext;

import jeus.tool.console.executor.ConsoleExecutorMBean;
import jeus.util.net.NetworkConstants;

public class JeusMonitoringMbean {
    private static final String JNDI_PREFIX = "mgmt/rmbs/";
    private static final String LOCAL_CLIENT_ADDRESS = "_localClientAddress";
    private static final String LOCAL_USER_NAME = "_localUserName";
   
    public static void main(String[] args) throws Exception {
        String url = "rhel01:9736";
        String user = "administrator";
        String password = "jeusadmin";
        String serverName = "adminServer";
        String domainName = "jeus_domain";
       
        /* MBeanServerConnection ������ */
        Hashtable<String, Object> env = new Hashtable<String, Object>();
       
        env.put(Context.INITIAL_CONTEXT_FACTORY, "jeus.jndi.JEUSContextFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);
      
        InitialContext context = new InitialContext(env);
        JMXConnector connector = (JMXConnector) context.lookup(JNDI_PREFIX + serverName);
        MBeanServerConnection mbsc = connector.getMBeanServerConnection();
       
        /* ConsoleExecutorMBean. DAS���� �����ϴ� MBean���� ��ɾ ������ �� ����. */
        ObjectName consoleMBeanName = new ObjectName(
                "JEUS:j2eeType=Console,"
                + "JMXManager=" + serverName +","
                + "J2EEDomain=" + domainName + ","
                + "name=" + serverName);
       
        ConsoleExecutorMBean consoleExecutor = MBeanServerInvocationHandler.newProxyInstance(mbsc, consoleMBeanName, ConsoleExecutorMBean.class, false);
       
        /* ��ɾ� ���� �� �α׸� ����� ���� �ʿ��� ����. �־����� ������ ������ �߻��ϹǷ� �ݵ�� �־��� ��. */
        Properties properties = new Properties();
        properties.put(LOCAL_CLIENT_ADDRESS, NetworkConstants.LOCAL_HOSTADDRESS);
        properties.put(LOCAL_USER_NAME, System.getProperty("user.name"));

        System.out.println("=========================================");
        //consoleExecutor.runCommand("start-server rhel01.svr01", properties);
        
        System.out.println(consoleExecutor.runCommand("si", properties).toString());
    }
}