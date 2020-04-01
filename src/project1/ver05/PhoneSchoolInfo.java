package project1.ver05;

class PhoneSchoolInfo extends PhoneInfo{

	String major;
	int grade;

	public PhoneSchoolInfo(String name, String phone, String major,int grade) {
		super(name, phone);
		this.major=major;
		this.grade=grade;
	}
	@Override
	public void showPhoneInfo() {
		System.out.println("==학교친구==");
		super.showPhoneInfo();
		System.out.println("전공:"+major);
		System.out.println("학년:"+grade);
	}
}