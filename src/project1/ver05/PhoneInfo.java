package project1.ver05;

public class PhoneInfo {
   //멤버변수
   public String name;
   public String phone ;
   //생성자 
   public PhoneInfo(String name, String phone) {
		this.name = name;
		this.phone = phone;
		
	}
   //정보 출력용 메소드 
   public void  showPhoneInfo() {
      System.out.println("이름:"+ name);
      System.out.println("전화번호:"+ phone);

   }
}