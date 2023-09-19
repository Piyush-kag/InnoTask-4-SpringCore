package com.springcore.Inno_spring1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan
public class Bookstore {
    @Autowired
    private InventoryManager inventoryManager;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Bookstore.class);
        Bookstore bookstore = context.getBean(Bookstore.class);

        // Add books to inventory
        bookstore.addBook(new Book("ISBN123", "The Innogent Factory", "F. Scott Fitzgerald", 10));
        bookstore.addBook(new Book("ISBN456", "The D-Mart Theory", "Harper Lee", 15));

        // Sell a book
        bookstore.sellBook("ISBN123", 2);

        // Check inventory
        bookstore.checkInventory();

        context.close();
    }

    public void addBook(Book book) {
        inventoryManager.addToInventory(book);
        System.out.println("Added book: " + book.getTitle());
    }

    public void sellBook(String isbn, int quantity) {
        boolean sold = inventoryManager.sellBook(isbn, quantity);
        if (sold) {
            System.out.println("Sold " + quantity + " book(s) with ISBN " + isbn);
        } else {
            System.out.println("Book with ISBN " + isbn + " is not available in sufficient quantity.");
        }
    }

    public void checkInventory() {
        inventoryManager.listInventory();
    }
}

interface InventoryManager {
    void addToInventory(Book book);

    boolean sellBook(String isbn, int quantity);

    void listInventory();
}

@Component
class BookInventoryManager implements InventoryManager {
    private Book[] inventory = new Book[100]; // Assuming a maximum of 100 books in the inventory
    private int itemCount = 0;

    public void addToInventory(Book book) {
        if (itemCount < inventory.length) {
            inventory[itemCount] = book;
            itemCount++;
        } else {
            System.out.println("Inventory is full. Cannot add more books.");
        }
    }

    public boolean sellBook(String isbn, int quantity) {
        for (int i = 0; i < itemCount; i++) {
            if (inventory[i].getIsbn().equals(isbn) && inventory[i].getQuantity() >= quantity) {
                inventory[i].setQuantity(inventory[i].getQuantity() - quantity);
                return true;
            }
        }
        return false;
    }

    public void listInventory() {
        System.out.println("Current Inventory:");
        for (int i = 0; i < itemCount; i++) {
            System.out.println(inventory[i]);
        }
    }
}

class Book {
    private String isbn;
    private String title;
    private String author;
    private int quantity;

    public Book(String isbn, String title, String author, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }



    public String getIsbn() {
		return isbn;
	}



	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getAuthor() {
		return author;
	}



	public void setAuthor(String author) {
		this.author = author;
	}



	public int getQuantity() {
		return quantity;
	}



	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	@Override
    public String toString() {
        return "Book [ISBN=" + isbn + ", Title=" + title + ", Author=" + author + ", Quantity=" + quantity + "]";
    }
}