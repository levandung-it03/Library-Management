package com.homework.library_management.config;

import com.homework.library_management.entities.*;
import com.homework.library_management.enums.RoleEnum;
import com.homework.library_management.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Initialization implements CommandLineRunner {
    AccountRepository accountRepository;
    LibrarianRepository librarianRepository;
    GenreRepository genreRepository;
    BookRepository bookRepository;
    BookGenreRepository bookGenreRepository;
    MembershipCardRepository membershipCardRepository;
    BorrowingRequestRepository borrowingRequestRepository;
    private final BookBorrowingRequestRepository bookBorrowingRequestRepository;

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            Account account = accountRepository.save(Account.builder()
                .email("perfecman1107@gmail.com")
                .password("useruser")
                .createdTime(LocalDateTime.now())
                .role(RoleEnum.LIBRARIAN)
                .active(true)
                .build());
            Librarian librarian = librarianRepository.save(Librarian.builder()
                .fullName("Lê Văn Dũng")
                .employeeId("LB13147")
                .account(account)
                .build());
            List<Genre> genres = genreRepository.saveAll(List.of(
                Genre.builder().genreName("Trinh thám").build(),
                Genre.builder().genreName("Mưu trí").build(),
                Genre.builder().genreName("Hành động").build(),
                Genre.builder().genreName("Cảm xúc").build(),
                Genre.builder().genreName("Trẻ em").build()
            ));
            List<Book> books = bookRepository.saveAll(List.of(
                Book.builder().bookName("Cuộc đời của Henri")
                    .authors("Trí Dũng, Trần Hải")
                    .availableQuantity(5 - 2)
                    .description("Vị trí: Kệ 2, Tầng 5")
                    .imgUrl("https://baokhanhhoa.vn/file/e7837c02857c8ca30185a8c39b582c03/052023/rar_24_20230525114955.jpeg")
                    .status(1)
                    .build(),
                Book.builder().bookName("Đỉnh cao cuộc đời")
                    .authors("Xuân Tú, Trần Hải")
                    .availableQuantity(10 - 2)
                    .description("Vị trí: Kệ 3, Tầng 1")
                    .imgUrl("https://baokhanhhoa.vn/file/e7837c02857c8ca30185a8c39b582c03/052023/rar_24_20230525114955.jpeg")
                    .status(1)
                    .build(),
                Book.builder().bookName("Đỉnh thấp cuộc đời")
                    .authors("Xuân Tú, Trần Hải")
                    .availableQuantity(5 - 2)
                    .description("Vị trí: Kệ 3, Tầng 1")
                    .imgUrl("https://baokhanhhoa.vn/file/e7837c02857c8ca30185a8c39b582c03/052023/rar_24_20230525114955.jpeg")
                    .status(1)
                    .build()
            ));
            bookGenreRepository.saveAll(List.of(
                BookGenre.builder().book(books.get(0)).genre(genres.get(0)).build(),
                BookGenre.builder().book(books.get(0)).genre(genres.get(3)).build(),
                BookGenre.builder().book(books.get(1)).genre(genres.get(0)).build(),
                BookGenre.builder().book(books.get(1)).genre(genres.get(1)).build(),
                BookGenre.builder().book(books.get(2)).genre(genres.get(4)).build()
            ));
            membershipCardRepository.saveAll(List.of(
                MembershipCard.builder().membershipCard("ABC100").prohibited(0).build(),
                MembershipCard.builder().membershipCard("ABC110").prohibited(0).build(),
                MembershipCard.builder().membershipCard("ABC120").prohibited(0).build(),
                MembershipCard.builder().membershipCard("ABC130").prohibited(0).build(),
                MembershipCard.builder().membershipCard("ABC140").prohibited(0).build()
            ));
            BorrowingRequest br = borrowingRequestRepository.save(BorrowingRequest.builder()
                .librarian(librarian)
                .membershipCard(membershipCardRepository.findById("ABC100").get())
                .borrowingTime(LocalDateTime.now())
                .returnedTime(null)
                .returningStatus(0)
                .build());
            bookBorrowingRequestRepository.saveAll(List.of(
                BookBorrowingRequest.builder().book(books.get(0)).borrowingRequest(br).quantity(2).build(),
                BookBorrowingRequest.builder().book(books.get(1)).borrowingRequest(br).quantity(2).build(),
                BookBorrowingRequest.builder().book(books.get(2)).borrowingRequest(br).quantity(2).build()
            ));
        }
    }
}
