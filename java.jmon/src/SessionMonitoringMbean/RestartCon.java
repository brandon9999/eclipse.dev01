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
        // JEUS Manager에 대한 PROVIDER URL
        String providerUrl = "127.0.0.1:9736";
        String userName = "administrator";
        String password = "jeusadmin";
        String nodeName = "Tmax-PC";

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "jeus.jndi.JEUSContextFactory");
        env.put(Context.PROVIDER_URL, providerUrl);
        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, password);

        // MBeanServerConnection을 가져온다.
        InitialContext context = new InitialContext(env);
        JMXConnector connector = (JMXConnector) context.lookup("mgmt/rmbs/"
                + nodeName);
        MBeanServerConnection mbsc = connector.getMBeanServerConnection();

        // Container 목록을 얻기 위해 ContainerManagerServiceMBean을 사용한다.
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
		String Nodename = args[0]; // nodename 받기
		String ContainerName1 = args[1]; // Contaienr Name 받기

		containerManager.restartContainer(Nodename, ContainerName1); // restartContainer Container Start
		System.out.println("###############################");
        System.out.println(Nodename + "   downing!!!");
        System.out.println("###############################");
		//Thread.sleep(10000); // 10s 대기중

		System.out.println("###############################");
        System.out.println(ContainerName1 + "   starting!!!");
        System.out.println("###############################");
        Thread.sleep(10000); // 10s 대기중
        
        // JEUSMain.xml에 설정되어 있는 모든 컨테이너의 목록.
        List<String> allContainerList = containerManagerService.getAllContainerList(nodeName);
        System.out.println("### All containers ###");
        for(String containerName : allContainerList)
            System.out.println(containerName);       
          
        // Boot되어 있는 컨테이너의 목록
        List<String> bootedContainerList = containerManagerService.getBootedContainerList();
        System.out.println("### Booted containers ###");
        for(String containerName : bootedContainerList)
            System.out.println(containerName);
        //부팅시  컨테이너 리스트 가져오기
        List<String> startOnBootContainerList = containerManagerService.getStartOnBootContainerList();  
        System.out.println("###############################");
        System.out.println("*** CONTAINER STATUS ***");
        System.out.println("###############################");
        Map<String, String> containerStatus = containerManagerService.getContainerList(); //현재 부팅된  컨테이너 리스트 상태를 위해서  가져오기.
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