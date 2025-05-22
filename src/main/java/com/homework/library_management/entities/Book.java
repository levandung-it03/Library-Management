package com.homework.library_management.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "Book", uniqueConstraints = {@UniqueConstraint(columnNames = "book_name")})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
    Long bookId;
    
	@Column(name = "book_name")
    String bookName;

	@Column(name = "authors")
    String authors;
    
	@Column(name = "available_quantity")
    Integer availableQuantity;

	@Column(name = "description")
    String description;
    
	@Column(name = "img_url")
    String imgUrl;

	@Column(name = "status")
    Integer status;

    @OneToMany(
        mappedBy = "book",  //--"FieldName" on Many-Many Relationship Entity
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    List<BookGenre> bookGenres;
}
