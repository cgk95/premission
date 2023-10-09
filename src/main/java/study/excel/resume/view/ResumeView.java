package study.excel.resume.view;

import study.excel.resume.model.Career;
import study.excel.resume.model.Education;
import study.excel.resume.model.PersonInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ResumeView {
    private final Scanner sc;

    public ResumeView() {
        sc = new Scanner(System.in);
    }

    public PersonInfo inputPersonInfo() {
        System.out.println("증명 사진 파일명을 입력하세요: ");
        String photo = sc.nextLine();

        System.out.println("이름을 입력하세요: ");
        String personName = sc.nextLine();

        System.out.println("이메일을 입력하세요: ");
        String email = sc.nextLine();

        System.out.println("주소를 입력하세요: ");
        String address = sc.nextLine();

        System.out.println("휴대전화 번호를 입력하세요: ");
        String phoneNumber = sc.nextLine();

        System.out.println("생년월일을 입력하세요 (예: 1991-07-23): ");
        String birthDate = sc.nextLine();

        return new PersonInfo(photo, personName, email, address, phoneNumber, birthDate);
    }

    public List<Education> inputEducationList() {
        List<Education> educationList = new ArrayList<>();

        while (true) {
            System.out.println("학력 정보를 입력하세요 (종료는 q): ");
            System.out.println("졸업년도 학교명 전공 졸업여부 (종료는 q)");

            String input = sc.nextLine();
            if (input.equalsIgnoreCase("q")) {
                break;
            }

            StringTokenizer st = new StringTokenizer(input);
            if (st.countTokens() != 4) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }
            educationList.add(new Education(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken()));
        }
        return educationList;
    }

    public List<Career> inputCareerList() {
        List<Career> careerList = new ArrayList<>();
        while (true) {
            System.out.println("경력 정보를 입력하세요 (종료는 q): ");
            System.out.println("경력기간 회사명 직무 고용기간 (종료는 q)");

            String input = sc.nextLine();
            if (input.equalsIgnoreCase("q")) {
                break;
            }

            StringTokenizer st = new StringTokenizer(input);
            if (st.countTokens() != 4) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }
            careerList.add(new Career(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken()));
        }
        return careerList;
    }

    public String inputSelfIntroduce() {
        System.out.println("자기소개서를 입력하세요. 여러 줄을 입력하려면 빈 줄을 입력하세요.");
        StringBuilder sb = new StringBuilder();
        String line;
        while (!(line = sc.nextLine()).trim().isEmpty()) {
            sb.append(line).append("\n");
        }
        return sb.toString().trim();
    }
}
