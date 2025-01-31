package ng.samuel.notdemo.ebankingms.authenticationservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ng.samuel.notdemo.ebankingms.authenticationservice.enums.Gender;

import java.time.LocalDate;


@NoArgsConstructor
@Getter
@Setter
public class CustomerDto {
    private String firstname;
    private String lastname;
    private String placeOfBirth;
    private LocalDate dateOfBirth;
    private String nationality;
    private Gender gender;
    private String cin;
    private String email;
}
