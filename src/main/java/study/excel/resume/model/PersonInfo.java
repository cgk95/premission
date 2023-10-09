package study.excel.resume.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString@AllArgsConstructor
public class PersonInfo {
    private String photo;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private String birthDate;

}
