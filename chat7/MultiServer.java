package chat7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultiServer {

	static ServerSocket serverSocket = null;
	static Socket socket = null;
	// 클라이언트 정보 저장을 위한 map컬렉션 정의
	Map<String, PrintWriter> clientMap;

	// 생성자
	public MultiServer() {

		// 클라이언트의 출력스트림을 저장할 해쉬맵 생성
		clientMap = new HashMap<String, PrintWriter>();
		// 해쉬맵 동기화 설정. 쓰레드가 사용자 정보에 동시접근을 막아주는 역할을 한다.
		Collections.synchronizedMap(clientMap);
	}

	public void init() {

		try {
			// 9999번 port로 설정해 서버를 생성후 클라이언트의 접속을 대기...
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");

			while (true) {
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + ":" + socket.getPort());

				// 클라이언트의 메세지를 모든 클라이언트에게 전달하기위한 쓰레드 생성 및 시동
				Thread mst = new MultiServerT(socket);
				mst.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 접속된 모든 클라이언트들에게 메시지를 전달.
	public static void main(String[] args) {
		// 서버객체생성
		MultiServer ms = new MultiServer();
		// 채팅을 위한 초기화 부분
		ms.init();
	}

	public void sendAllMsg(String name, String msg) {
		// Map에 저장된 객체들의 키값(이름)을 가져온다.
		Iterator<String> it = clientMap.keySet().iterator();
		// 저장된 객체(클라이언트)의 갯수만큼 반복.
		while (it.hasNext()) {

			try {
				// 각 클라이언트의 PrintWriter객체를 얻어온다.
				String n = it.next();

				PrintWriter it_out = (PrintWriter) clientMap.get(n);

				/*
				 * 매개변수 name이 있는 경우에는 이름+메세지 없는 경우 메세지만 클라이언트에게 전달.
				 */
				if (name.equals(n)) {
					it_out.println(URLEncoder.encode(msg, "UTF-8"));
				}
				else if (name.equals(n)) {
					it_out.println(msg);
				}
				else {
					it_out.println("[" + name + "] " + msg);
				}
			} 
			catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}
	}

	// 내부클래스
	/*
	 * 클라이언트로부터 읽어온 메시지를 다른 클라이언트에게 보내는 역할을 하는 메소드
	 */
	class MultiServerT extends Thread {
		// 맴버변수
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;

		// 생성자 : Socket을 기반으로 입출력을 기반으로 출력한다.
		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
			} catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}

		@Override
		public void run() {

			// 클라이언트로부터 받은 "대화명"을 저장할 변수
			String name = "";
			// 메세지 저장용 변수
			String s = "";

			try {
				// 클라이언트의 이름을 읽어와서 저장.
				name = in.readLine();

				// 접속한 클라이언트에게 새로운 사용자의 입장을 알림.
				// 접속자를 제외한 나머지 클라이언트만 입장메세지를 받음.
				name = URLDecoder.decode(name, "UTF-8");
				sendAllMsg("", name + "님이 입장하셨습니다.");// 첫번째 인자없이 메소드호출

				// 현재 접속한 클라이언트를 해시맵에 저장한다.
				clientMap.put(name, out);

				// HashMap에 저장된 객체의 접속자수를 파악할 수 있다.
				System.out.println(name + " 접속");
				System.out.println("현재 접속자 수는 " + clientMap.size() + "명 입니다.");
				System.out.println(clientMap.keySet());

				// 입력한 메세지는 모든 클라이언트에게 Echo된다.
				while (in != null) {
					
						s = in.readLine();
						s = URLDecoder.decode(s, "UTF-8");
						System.out.println(s);
						
						if (s == null)
							break;

						System.out.println(name + " >> " + s);
						sendAllMsg(name, s);// 첫번째 인자 포함해서 메소드호출
				}
			}
		catch(Exception e){
			System.out.println("예외:" + e);
		}
			finally {
			/*
			 * 클라이언트가 접속을종료를 하면 예외가 발생되어 finally로 넘어오게 됨. 이때 "대화명"을 통해 remove()시켜줌.
			 */
			clientMap.remove(name);
			sendAllMsg("", name + "님이 퇴장하셨습니다.");
			// 퇴장하는 클라이언트의 쓰레드 명을 보여줌.
			System.out.println(name + " [" + Thread.currentThread().getName() + "] 퇴장");
			System.out.println("현재 접속자 수는 " + clientMap.size() + "명 입니다.");

			try {
				in.close();
				out.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}
	}
}