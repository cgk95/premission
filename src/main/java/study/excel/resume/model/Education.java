package study.excel.resume.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString@AllArgsConstructor
public class Education {
    private String graduatedYear;
    private String schoolName;
    private String major;
    private String graduationStatus;

}
