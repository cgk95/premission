package study.excel.resume.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@AllArgsConstructor@ToString
public class Career {
    private String workPeriod;
    private String companyName;
    private String jobTitle;
    private String employmentYears;
}
