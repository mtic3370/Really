package project1.ver05;

class PhoneCompanyInfo extends PhoneInfo {
	
	String company;
	
	public PhoneCompanyInfo(String name, String phone,String company) {
		super(name, phone);
		this.company=company;
	}
	@Override
	public void showPhoneInfo() {
		System.out.println("==회사동료==");
		super.showPhoneInfo();
		System.out.println("회사명:"+company);
	}
}