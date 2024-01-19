public class Borrower {
    private int borrowerId;
    private String name;
    private String email;
    private String borrowedBooks;

    // Constructors, getters, and setters

    // Constructor for creating a Borrower instance with all fields
    public Borrower(int borrowerId, String name, String email, String borrowedBooks) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.email = email;
        this.borrowedBooks = borrowedBooks;
    }

    // Getter and Setter methods for all fields

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(String borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
