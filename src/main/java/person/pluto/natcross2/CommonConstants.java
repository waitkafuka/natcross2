package person.pluto.natcross2;

/**
 * <p>
 * 公共参数
 * </p>
 *
 * @author Pluto
 * @since 2020-04-10 12:29:01
 */
public final class CommonConstants {

	// 映射对
	public static ListenDest[] listenDestArray = new ListenDest[] {
			//
			ListenDest.of(8082, "127.0.0.1", 5500),
			//
	};

	// 交互密钥 AES
	public static final String aesKey = "8AUWlb+IWD+Fhbs0xnXCCg==";
	// 交互签名key
	public static final String tokenKey = "selfdefinetokenkey";

	/**
	 * <p>
	 * 监听、映射对
	 * </p>
	 */
	static class ListenDest {
		public static ListenDest of(int listenPort, String destIp, int destPort) {
			ListenDest model = new ListenDest();
			model.listenPort = listenPort;
			model.destIp = destIp;
			model.destPort = destPort;
			return model;
		}

		// 服务端监听的端口，外网访问服务端IP:listengPort即可进行穿透
		public int listenPort;
		// 穿透的目标，即要暴露在外网的内网IP
		public String destIp;
		// 要暴露的内网端口
		public int destPort;
	}

}
