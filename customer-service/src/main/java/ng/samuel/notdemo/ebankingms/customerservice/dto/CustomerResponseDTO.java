package ng.samuel.notdemo.ebankingms.customerservice.dto;

import lombok.*;
import ng.samuel.notdemo.ebankingms.customerservice.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerResponseDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String placeOfBirth;
    private LocalDate dateOfBirth;
    private String nationality;
    private Gender gender;
    private String cin;
    private String email;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
}