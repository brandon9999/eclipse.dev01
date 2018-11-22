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

public class RestartCon {
	
    public static void main(String[] args) throws Exception {
        // JEUS Manager�� ���� PROVIDER URL
        String providerUrl = "127.0.0.1:9736";
        String userName = "administrator";
        String password = "jeusadmin";
        String nodeName = "Tmax-PC";

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "jeus.jndi.JEUSContextFactory");
        env.put(Context.PROVIDER_URL, providerUrl);
        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, password);

        // MBeanServerConnection�� �����´�.
        InitialContext context = new InitialContext(env);
        JMXConnector connector = (JMXConnector) context.lookup("mgmt/rmbs/"
                + nodeName);
        MBeanServerConnection mbsc = connector.getMBeanServerConnection();

        // Container ����� ��� ���� ContainerManagerServiceMBean�� ����Ѵ�.
        ObjectName objectName = new ObjectName(
                "JEUS:j2eeType=JeusService,jeusType=ContainerManagerService,JMXManager="
                        + nodeName + ",JeusManager=" + nodeName + ",name="
                        + nodeName);

        ContainerManagerServiceMBean containerManagerService = (ContainerManagerServiceMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbsc, objectName,
                        ContainerManagerServiceMBean.class, false);

		Set objectNames = JMXUtility.queryContainerManager(mbsc, nodeName);
		ContainerManagerServiceMBean containerManager =  (ContainerManagerServiceMBean)JMXUtility.getProxy(mbsc, (ObjectName)objectNames.iterator().next(), ContainerManagerServiceMBean.class, false);
		System.out.println("###############################" + args[0]);
		String Nodename = args[0]; // nodename �ޱ�
		String ContainerName1 = args[1]; // Contaienr Name �ޱ�

		containerManager.restartContainer(Nodename, ContainerName1); // restartContainer Container Start
		System.out.println("###############################");
        System.out.println(Nodename + "   downing!!!");
        System.out.println("###############################");
		//Thread.sleep(10000); // 10s �����

		System.out.println("###############################");
        System.out.println(ContainerName1 + "   starting!!!");
        System.out.println("###############################");
        Thread.sleep(10000); // 10s �����
        
        // JEUSMain.xml�� �����Ǿ� �ִ� ��� �����̳��� ���.
        List<String> allContainerList = containerManagerService.getAllContainerList(nodeName);
        System.out.println("### All containers ###");
        for(String containerName : allContainerList)
            System.out.println(containerName);       
          
        // Boot�Ǿ� �ִ� �����̳��� ���
        List<String> bootedContainerList = containerManagerService.getBootedContainerList();
        System.out.println("### Booted containers ###");
        for(String containerName : bootedContainerList)
            System.out.println(containerName);
        //���ý�  �����̳� ����Ʈ ��������
        List<String> startOnBootContainerList = containerManagerService.getStartOnBootContainerList();  
        System.out.println("###############################");
        System.out.println("*** CONTAINER STATUS ***");
        System.out.println("###############################");
        Map<String, String> containerStatus = containerManagerService.getContainerList(); //���� ���õ�  �����̳� ����Ʈ ���¸� ���ؼ�  ��������.
        for (String container : startOnBootContainerList)
        {
        
                if(containerStatus.containsKey(container) == true)
                {
                                System.out.println(container+" ====>"+containerStatus.get(container));
        
                }
                else
                {
                        System.out.println(container+" ====>"+"NOT_READY");
                }
        }
        
    }
}