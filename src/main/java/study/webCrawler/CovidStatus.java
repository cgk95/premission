package study.webCrawler;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CovidStatus {
    private String region;
    private int total;
    private int domestic;
    private int abroad;
    private int confirmed;
    private int deaths;
    private double rate;
}