package Assignment5;

public class OpinionBean {
	private String username;
	private String assertion;
	private String justification;
	private int agreed;
	private int neutral;
	private int disagreed;
	
	public String getUsername() {
		return this.username;
	}
	public String getAssertion() {
		return this.assertion;
	}
	public String getJustification() {
		return this.justification;
	}
	public int getAgreed() {
		return this.agreed;
	}
	public int getNeutral() {
		return this.neutral;
	}
	public int getDisagreed() {
		return this.disagreed;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public void setAgreed(int agreed) {
		this.agreed = agreed;
	}
	public void setNeutral(int neutral) {
		this.neutral = neutral;
	}
	public void setDisagreed(int disagreed) {
		this.disagreed = disagreed;
	}
} 