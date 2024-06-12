import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Library library = new Library();

        // insert books
        // Title, Author, ISBN, Genre, Copies, Shelf # 
        library.insert(new Book("Man's Search for Meaning", "Viktor E. Frankl", "978-0807014264", "Autobiography", 5, 101));
        library.insert(new Book("To Kill a Mockingbird", "Harper Lee", "978-0446310789", "Southern Gothic", 3, 103));
        library.insert(new Book("Othello", "William Shakespeare", "978-0743477550", "Shakespearean Tragedy", 7, 103));

        // details of books
        System.out.println("Details of books");
        library.showDetails();

        //updating books
        library.update("978-0743477550", "Shakespeare", "Tragedy", 10);
        System.out.println("---------------------------");


        // details of book after update
        System.out.println("Details after update");
        library.showDetails();

        // deleting book
        library.delete("978-0807014264");

        System.out.println("---------------------------");
        // details of book after delete
        System.out.println("Details after deleting book");
        library.showDetails();

        // searching for book
        Scanner scan = new Scanner(System.in);

        System.out.println("Search book using ISBN:");
        String isbn = scan.nextLine();
        Book searchedBook = library.search(isbn);
        if (searchedBook != null) {
            System.out.println("Book found:\n");
            System.out.println("Title: " + searchedBook.title);
            System.out.println("Author: " + searchedBook.author);
            System.out.println("ISBN: " + searchedBook.isbn);
            System.out.println("Genre: " + searchedBook.genre);
            System.out.println("Copies Available: " + searchedBook.copiesAvailable);
            System.out.println("Shelf Number: " + searchedBook.shelfNumber);
        } else {
            System.out.println("Book not found.");
        }
    }
}
