package chat7;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Scanner;

public class MultiClient {

	public static void main(String[] args) {
		
		System.out.print("이름을 입력하세요:");
		Scanner scanner = new Scanner (System.in);
		String s_name = scanner.nextLine();
		
		PrintWriter out = null;
		//리시버가 기능을 가져가므로 여기서는 필요없음
		//BufferedReader in = null;
		
		try {
			String ServerIP = "localhost";
			//클라이언트 실행시 매개변수가 있는경우 아이피 설정
			if(args.length>0) {
				ServerIP = args[0];
			}
			//소켓 객체 생성
			Socket socket = new Socket(ServerIP, 9999);
			System.out.println("서버와 연결 되었습니다.");
			//서버에서 보내는 메시지를 클라이언트의 콘솔에 출력하는 쓰레드
			
			Thread receiver = new Receiver(socket);
			receiver.start();
			
			//클라이언트로부터 얻은 문자열을 서버로 전송해주는 쓰레드
			
			Thread sender = new Sender(socket, s_name);
			sender.start();
		}
		catch (Exception e) {
			System.out.println("예외발생 [MultiClient]"+e);
		}
	}

}
      