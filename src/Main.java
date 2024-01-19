import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nLibrary Management System Menu:");
            System.out.println("1. Add a Book");
            System.out.println("2. Update Book Details");
            System.out.println("3. Remove a Book");
            System.out.println("4. Display All Books");
            System.out.println("5. Add a Borrower");
            System.out.println("6. Borrow a Book");
            System.out.println("7. Return a Book");
            System.out.println("8. Display All Borrowers");
            System.out.println("0. Exit");

            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    updateBook();
                    break;
                case 3:
                    removeBook();
                    break;
                case 4:
                    displayAllBooks();
                    break;
                case 5:
                    addBorrower();
                    break;
                case 6:
                    borrowBook();
                    break;
                case 7:
                    returnBook();
                    break;
                case 8:
                    displayAllBorrowers();
                    break;
                case 0:
                    System.out.println("Exiting Library Management System. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void addBook() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.print("Enter book_id: ");
            int bookId = scanner.nextInt();

            System.out.print("Enter title: ");
            String title = scanner.next();

            System.out.print("Enter author: ");
            String author = scanner.next();

            System.out.print("Enter genre: ");
            String genre = scanner.next();

            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();

            String query = "INSERT INTO books (book_id, title, author, genre, quantity) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, bookId);
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, author);
                preparedStatement.setString(4, genre);
                preparedStatement.setInt(5, quantity);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Book added successfully!");
    }


    private static void updateBook() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.print("Enter book_id to update: ");
            int bookId = scanner.nextInt();

            System.out.print("Enter new title: ");
            String newTitle = scanner.next();

            System.out.print("Enter new author: ");
            String newAuthor = scanner.next();

            System.out.print("Enter new genre: ");
            String newGenre = scanner.next();

            System.out.print("Enter new quantity: ");
            int newQuantity = scanner.nextInt();

            String query = "UPDATE books SET title=?, author=?, genre=?, quantity=? WHERE book_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newTitle);
                preparedStatement.setString(2, newAuthor);
                preparedStatement.setString(3, newGenre);
                preparedStatement.setInt(4, newQuantity);
                preparedStatement.setInt(5, bookId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Book details updated successfully!");
    }

    private static void removeBook() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.print("Enter book_id to remove: ");
            int bookId = scanner.nextInt();

            String query = "DELETE FROM books WHERE book_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, bookId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Book removed successfully!");
    }

    private static void displayAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM books";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Book book = new Book(
                                resultSet.getInt("book_id"),
                                resultSet.getString("title"),
                                resultSet.getString("author"),
                                resultSet.getString("genre"),
                                resultSet.getInt("quantity")
                        );
                        books.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\nList of all books:");
        for (Book book : books) {
            System.out.println("Book ID: " + book.getBookId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Genre: " + book.getGenre());
            System.out.println("Quantity: " + book.getQuantity());
            System.out.println("-----------------------");
        }
    }

    private static void addBorrower() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.print("Enter borrowerId: ");
            int borrowerId = scanner.nextInt();

            System.out.print("Enter name: ");
            String name = scanner.next();

            System.out.print("Enter email: ");
            String email = scanner.next();

            System.out.print("Enter borrowedBooks (comma-separated list of book_ids): ");
            String borrowedBooks = scanner.next();

            String query = "INSERT INTO borrowers (borrower_id, name, email, borrowed_books) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, borrowerId);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, borrowedBooks);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Borrower added successfully!");
    }

    private static void borrowBook() {
        displayAllBooks();

        System.out.print("\nEnter the book_id to borrow: ");
        int bookId = scanner.nextInt();

        if (isBookAvailable(bookId)) {
            System.out.print("Enter borrower_id: ");
            int borrowerId = scanner.nextInt();

            if (isBorrowerExists(borrowerId)) {
                try (Connection connection = DatabaseConnection.getConnection()) {
                    String updateQuery = "UPDATE borrowers SET borrowed_books = CONCAT(borrowed_books, ?) WHERE borrower_id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, bookId + ",");
                        updateStatement.setInt(2, borrowerId);

                        updateStatement.executeUpdate();
                    }

                    String decreaseQuantityQuery = "UPDATE books SET quantity = quantity - 1 WHERE book_id = ?";
                    try (PreparedStatement quantityStatement = connection.prepareStatement(decreaseQuantityQuery)) {
                        quantityStatement.setInt(1, bookId);
                        quantityStatement.executeUpdate();
                    }

                    System.out.println("Book borrowed successfully!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Borrower with ID " + borrowerId + " does not exist.");
            }
        } else {
            System.out.println("Book with ID " + bookId + " is not available for borrowing.");
        }
    }

    private static boolean isBookAvailable(int bookId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT quantity FROM books WHERE book_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, bookId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next() && resultSet.getInt("quantity") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isBorrowerExists(int borrowerId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM borrowers WHERE borrower_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, borrowerId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void returnBook() {
        System.out.print("\nEnter the book_id to return: ");
        int bookId = scanner.nextInt();

        System.out.print("Enter borrower_id: ");
        int borrowerId = scanner.nextInt();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE borrowers SET borrowed_books = REPLACE(borrowed_books, ?, '') WHERE borrower_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, bookId + ",");
                updateStatement.setInt(2, borrowerId);

                updateStatement.executeUpdate();
            }

            String increaseQuantityQuery = "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?";
            try (PreparedStatement quantityStatement = connection.prepareStatement(increaseQuantityQuery)) {
                quantityStatement.setInt(1, bookId);
                quantityStatement.executeUpdate();
            }

            System.out.println("Book returned successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllBorrowers() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM borrowers";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("\nList of all borrowers:");
                    while (resultSet.next()) {
                        int borrowerId = resultSet.getInt("borrower_id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String borrowedBooks = resultSet.getString("borrowed_books");

                        System.out.println("Borrower ID: " + borrowerId);
                        System.out.println("Name: " + name);
                        System.out.println("Email: " + email);
                        System.out.println("Borrowed Books: " + borrowedBooks);
                        System.out.println("-----------------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

