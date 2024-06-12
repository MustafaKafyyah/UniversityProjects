public class Book {
    String title, author, isbn, genre;
    int copiesAvailable, shelfNumber;

    public Book(String title, String author, String isbn, String genre, int copiesAvailable, int shelfNumber) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
        this.shelfNumber = shelfNumber;
    }
}
