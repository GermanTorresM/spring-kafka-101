package com.lab.kafka.producers;

import com.lab.kafka.Book;
import com.lab.kafka.Utils;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Profile("book-producer")
@Service
public class BookProducer {

    private final KafkaTemplate<String, Book> bookProducer;

    public BookProducer(KafkaTemplate<String, Book> bookProducer) {
        this.bookProducer = bookProducer;
        produceMessages();
    }

    private void produceMessages() {
        new Thread(() -> {
            Stream.generate(this::getRandomBook)
                    .forEach((book) -> {
                        Utils.sleep(1_000);
                        this.send(book);
                    });
        }).start();
    }

    private Book getRandomBook() {
        int randomInt = Utils.random(0, books.size());
        return books.get(randomInt);
    }

    public void send(Book book) {
        System.out.println("[BOOK-PRODUCER] Sending " + book.toString());
        bookProducer.send("book-topic", book);
    }

    private final List<Book> books = List.of(
            Book.create("Die Verwandlung", "Franz", "Kafka"),
            Book.create("Foundation", "Isaac", "Asimov"),
            Book.create("Lord of the Rings", "J.R.R.", "Tolkien"),
            Book.create("The Odyssey", "Homer", ""),
            Book.create("Hamlet", "William", "Shakespeare"),
            Book.create("Madame Bovary", "Gustave", "Flaubert"),
            Book.create("Candide", "Voltaire", ""),
            Book.create("How to Win Friends & Influence People", "Dale", "Carnegie"),
            Book.create("Le Comte de Monte-Cristo", "Alexandre", "Dumas")
    );
}