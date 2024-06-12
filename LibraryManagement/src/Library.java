public class Library {
    Node head;

    public void insert(Book data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    public int delete(String isbn) {
        if (head == null) {
            return -1;
        }
        if (head.data.isbn.equals(isbn)) {
            head = head.next;
            return 0;
        }
        Node prev = null;
        Node current = head;
        while (current != null && !current.data.isbn.equals(isbn)) {
            prev = current;
            current = current.next;
        }
        if (current == null) {
            return -1;
        }
        prev.next = current.next;
        return 0;
    }

    public void update(String isbn, String author, String genre, int copiesAvailable) {
        Node temp = head;
        while(temp != null) {
            if (temp.data.isbn.equals(isbn)) {
                temp.data.author = author;
                temp.data.genre = genre;
                temp.data.copiesAvailable = copiesAvailable;
                break;
            }
            temp = temp.next;
        }
    }

    public void showDetails() {
        Node temp = head;
        while (temp != null) {
            System.out.println("Title: " + temp.data.title);
            System.out.println("Author: " + temp.data.author);
            System.out.println("ISBN: " + temp.data.isbn);
            System.out.println("Genre: " + temp.data.genre);
            System.out.println("Copies Available: " + temp.data.copiesAvailable);
            System.out.println("Shelf Number: " + temp.data.shelfNumber);
            System.out.println();
            temp = temp.next;
        }
    }

    public Book search(String isbn) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.isbn.equals(isbn)) {
                return temp.data;
            }
            temp = temp.next;
        }
        return null;
    }
}
