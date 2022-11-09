package person.pluto.natcross2;

import java.util.Arrays;

import person.pluto.natcross2.CommonConstants.ListenDest;
import person.pluto.natcross2.clientside.ClientControlThread;
import person.pluto.natcross2.clientside.config.AllSecretInteractiveClientConfig;
import person.pluto.natcross2.clientside.config.HttpRouteClientConfig;
import person.pluto.natcross2.clientside.config.InteractiveClientConfig;
import person.pluto.natcross2.clientside.config.SecretInteractiveClientConfig;
import person.pluto.natcross2.model.HttpRoute;

/**
 * 
 * <p>
 * 客户端，放在内网侧
 * </p>
 *
 * @author Pluto
 * @since 2020-01-09 16:26:44
 */
public class ClientApp {

	// 客户端内网服务IP
	private static String clientIp = "127.0.0.1";
	// 客户端内网服务端口
	private static int clientPort = 5500;
	// 服务端IP
	private static String serverIp = null;
	// 服务端端口
	private static int serverPort = 8082;
	// 服务端控制端口
	private static int serverServicePort = 8193;

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-clientIp")) {
				clientIp = args[i + 1];
			} else if (args[i].equals("-clientPort") || args[i].equals("-p") || args[i].equals("-port")) {
				clientPort = Integer.parseInt(args[i + 1]);
			} else if (args[i].equals("-serverIp")) {
				serverIp = args[i + 1];
			} else if (args[i].equals("-serverPort")) {
				serverPort = Integer.parseInt(args[i + 1]);
			} else if (args[i].equals("-serverServicePort")) {
				serverServicePort = Integer.parseInt(args[i + 1]);
			}
		}
		if (serverIp == null) {
			throw new RuntimeException("必须指定一个serverIp!!!!");
		}
		System.out.println("客户端已启动：");
		System.out.println("====================客户端配置start====================");
		System.out.println("clientIp: " + clientIp);
		System.out.println("clientPort: " + clientPort);
		System.out.println("serverIp: " + serverIp);
		System.out.println("serverPort: " + serverPort);
		System.out.println("serverServicePort: " + serverServicePort);
		System.out.println("====================客户端配置end====================");
		// simple();
		secret();
		// secretAll();
		// secretHttpRoute();
	}

	/**
	 * http路由
	 * 
	 * 默认使用交互加密、数据不加密的策略
	 */
	public static void secretHttpRoute() throws Exception {
		HttpRoute[] routes = new HttpRoute[] {
				//
				HttpRoute.of("localhost", "127.0.0.1", 8080),
				//
				HttpRoute.of(true, "127.0.0.1", "127.0.0.1", 8080),
				//
		};

		for (ListenDest model : CommonConstants.listenDestArray) {
			SecretInteractiveClientConfig baseConfig = new SecretInteractiveClientConfig();

			// 设置服务端IP和端口
			baseConfig.setClientServiceIp(serverIp);
			baseConfig.setClientServicePort(serverServicePort);
			// 设置对外暴露的端口，该端口的启动在服务端，这里只是表明要跟服务端的那个监听服务对接
			baseConfig.setListenServerPort(model.listenPort);

			// 设置交互密钥和签名key
			baseConfig.setBaseAesKey(CommonConstants.aesKey);
			baseConfig.setTokenKey(CommonConstants.tokenKey);

			HttpRouteClientConfig config = new HttpRouteClientConfig(baseConfig);
			config.addRoute(routes);

			new ClientControlThread(config).createControl();
		}
	}

	/**
	 * 交互、隧道都进行加密
	 */
	public static void secretAll() throws Exception {
		for (ListenDest model : CommonConstants.listenDestArray) {
			AllSecretInteractiveClientConfig config = new AllSecretInteractiveClientConfig();

			// 设置服务端IP和端口
			config.setClientServiceIp(serverIp);
			config.setClientServicePort(serverServicePort);
			// 设置对外暴露的端口，该端口的启动在服务端，这里只是表明要跟服务端的那个监听服务对接
			config.setListenServerPort(model.listenPort);
			// 设置要暴露的目标IP和端口
			config.setDestIp(model.destIp);
			config.setDestPort(model.destPort);

			// 设置交互密钥和签名key
			config.setBaseAesKey(CommonConstants.aesKey);
			config.setTokenKey(CommonConstants.tokenKey);
			// 设置隧道交互密钥
			config.setBasePasswayKey(CommonConstants.aesKey);

			new ClientControlThread(config).createControl();
		}
	}

	/**
	 * 交互加密，即交互验证
	 */
	public static void secret() throws Exception {
		// for (ListenDest model : CommonConstants.listenDestArray) {
		SecretInteractiveClientConfig config = new SecretInteractiveClientConfig();

		// 设置服务端IP和端口
		config.setClientServicePort(serverServicePort);
		// 设置对外暴露的端口，该端口的启动在服务端，这里只是表明要跟服务端的那个监听服务对接
		config.setClientServiceIp(serverIp);
		config.setListenServerPort(serverPort);
		// 设置要暴露的目标IP和端口
		config.setDestIp(clientIp);
		config.setDestPort(clientPort);

		// 设置交互密钥和签名key
		config.setBaseAesKey(CommonConstants.aesKey);
		config.setTokenKey(CommonConstants.tokenKey);

		new ClientControlThread(config).createControl();
		// }
	}

	/**
	 * 无加密、无验证
	 */
	public static void simple() throws Exception {
		for (ListenDest model : CommonConstants.listenDestArray) {
			InteractiveClientConfig config = new InteractiveClientConfig();

			// 设置服务端IP和端口
			config.setClientServiceIp(serverIp);
			config.setClientServicePort(serverServicePort);
			// 设置对外暴露的端口，该端口的启动在服务端，这里只是表明要跟服务端的那个监听服务对接
			config.setListenServerPort(model.listenPort);
			// 设置要暴露的目标IP和端口
			config.setDestIp(model.destIp);
			config.setDestPort(model.destPort);

			new ClientControlThread(config).createControl();
		}
	}

}
