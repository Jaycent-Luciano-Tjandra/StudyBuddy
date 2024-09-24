package application;
public class User {
    private Integer userId;
    private String firstName;
    private String email;

    public User(Integer userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getEmail() {
        return email;
    }

	public void setUserId(int userId) {
		this.userId = userId;
	}

    // You may need additional methods and fields based on your requirements
}
