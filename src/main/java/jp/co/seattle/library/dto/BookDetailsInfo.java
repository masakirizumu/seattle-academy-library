package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍詳細情報格納DTO
 *
 */
@Configuration
@Data
public class BookDetailsInfo {

    private int bookId;  
    
    private int rentBookId;
    
    private String status;

    private String title;

    private String author;

    private String publisher;
    
    private String publishDate;
    
    private String isbn;
    
    private String explanation;

    private String thumbnailUrl;

    private String thumbnailName;

    public BookDetailsInfo() {

    }

    public BookDetailsInfo(int bookId, String title, String author, String publisher,String isbn,String explanation,String publishDate,
            String thumbnailUrl, String thumbnailName) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.isbn = isbn;
        this.explanation = explanation;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailName = thumbnailName;
    }

}