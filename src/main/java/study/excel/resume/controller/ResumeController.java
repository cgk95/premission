package study.excel.resume.controller;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import study.excel.resume.model.Career;
import study.excel.resume.model.Education;
import study.excel.resume.model.PersonInfo;
import study.excel.resume.view.ResumeView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class ResumeController {
    private final ResumeView resumeView;
    private final Workbook workbook;
    private int educationRowNum;

    public ResumeController() {
        resumeView = new ResumeView();
        workbook = new XSSFWorkbook();
    }

    public static void main(String[] args) {
        ResumeController controller = new ResumeController();
        controller.createResume();
    }

    public void createResume() {
        PersonInfo personInfo = resumeView.inputPersonInfo();
        List<Education> educationList = resumeView.inputEducationList();
        List<Career> careerList = resumeView.inputCareerList();
        String selfIntroduction = resumeView.inputSelfIntroduce();

        createResumeSheet(personInfo, educationList, careerList);
        createSelfIntoductionSheet(selfIntroduction);
        saveWorkbook2File();
    }

    private void createResumeSheet(PersonInfo personInfo, List<Education> educationList, List<Career> careerList) {
        Sheet sheet = workbook.createSheet("이력서");

        makePersonInfoToExcelFile(personInfo, sheet);

        makeEducationInfoToExcelFile(educationList, sheet);

        makeCareerInfoToExcelFile(careerList, sheet);
    }

    private void makeCareerInfoToExcelFile(List<Career> careerList, Sheet sheet) {
        int careerStartRow = educationRowNum + 1;
        maekCareerHeader(sheet, careerStartRow);
        int careerRowNum = careerStartRow;
        for (Career career : careerList) {
            Row carrerDataRow = sheet.createRow(careerRowNum++);
            injectCareer(career, carrerDataRow);
        }
    }

    private void makeEducationInfoToExcelFile(List<Education> educationList, Sheet sheet) {
        int educationStartRow = 3;
        Row educationHeaderRow = sheet.createRow(educationStartRow - 1);
        makeEducationHeader(educationHeaderRow);
        educationRowNum = educationStartRow;
        for (Education edu : educationList) {
            Row educationDataRow = sheet.createRow(educationRowNum++);
            injectEducation(edu, educationDataRow);
        }
    }

    private void makePersonInfoToExcelFile(PersonInfo personInfo, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        makePersonInfoHeader(headerRow);
        Row dataRow = sheet.createRow(1);
        injectPhoto(sheet, dataRow);
        injectPersonInfo(personInfo, dataRow);
    }

    private static void injectCareer(Career career, Row carrerDataRow) {
        carrerDataRow.createCell(0).setCellValue(career.getWorkPeriod());
        carrerDataRow.createCell(1).setCellValue(career.getCompanyName());
        carrerDataRow.createCell(2).setCellValue(career.getJobTitle());
        carrerDataRow.createCell(3).setCellValue(career.getEmploymentYears());
    }

    private static void injectEducation(Education edu, Row educationDataRow) {
        educationDataRow.createCell(0).setCellValue(edu.getGraduatedYear());
        educationDataRow.createCell(1).setCellValue(edu.getSchoolName());
        educationDataRow.createCell(2).setCellValue(edu.getMajor());
        educationDataRow.createCell(3).setCellValue(edu.getGraduationStatus());
    }

    private static void maekCareerHeader(Sheet sheet, int careerStartRow) {
        Row careerHeaderRow = sheet.createRow(careerStartRow - 1);
        careerHeaderRow.createCell(0).setCellValue("근무기간");
        careerHeaderRow.createCell(1).setCellValue("근무처");
        careerHeaderRow.createCell(2).setCellValue("담당업무");
        careerHeaderRow.createCell(3).setCellValue("근속년수");
    }

    private static void makeEducationHeader(Row educationHeaderRow) {
        educationHeaderRow.createCell(0).setCellValue("졸업년도");
        educationHeaderRow.createCell(1).setCellValue("학교명");
        educationHeaderRow.createCell(2).setCellValue("전공");
        educationHeaderRow.createCell(3).setCellValue("졸업여부");
    }

    private static void makePersonInfoHeader(Row headerRow) {
        headerRow.createCell(0).setCellValue("사진");
        headerRow.createCell(1).setCellValue("이름");
        headerRow.createCell(2).setCellValue("이메일");
        headerRow.createCell(3).setCellValue("주소");
        headerRow.createCell(4).setCellValue("전화번호");
        headerRow.createCell(5).setCellValue("생년월일");
    }

    private static void injectPersonInfo(PersonInfo personInfo, Row dataRow) {
        dataRow.createCell(1).setCellValue(personInfo.getName());
        dataRow.createCell(2).setCellValue(personInfo.getEmail());
        dataRow.createCell(3).setCellValue(personInfo.getAddress());
        dataRow.createCell(4).setCellValue(personInfo.getPhoneNumber());
        dataRow.createCell(5).setCellValue(personInfo.getBirthDate());
    }

    private void injectPhoto(Sheet sheet, Row dataRow) {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream photoStream = classLoader.getResourceAsStream("sample.png")) {
            assert photoStream != null;
            BufferedImage originalImage = ImageIO.read(photoStream);
            //-- 증명사진 사이즈로 이미지 재조정 --//
            int newWidth = (int) (35 * 2.83465);
            int newHeight = (int) (45 * 2.83465);
            Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage resizedBufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_4BYTE_ABGR);
            //-- 이미지를 엑셀시트에 삽입하고 사진이 들어간 셀의 크기를 조정 --//
            insertResizedImageIntoSheet(sheet, resizedBufferedImage, resizedImage);
            adjustRowHeightAndFirstColumnWidth(sheet, dataRow, newHeight, (float) newWidth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void adjustRowHeightAndFirstColumnWidth(Sheet sheet, Row dataRow, int newHeight, float newWidth) {
        dataRow.setHeightInPoints((float) (newHeight * 72) / 96); // 픽셀을 포인트로
        int columeWidth = (int) Math.floor((newWidth / (float) 8) * 256);
        sheet.setColumnWidth(0, columeWidth);
    }

    private void insertResizedImageIntoSheet(Sheet sheet, BufferedImage resizedBufferedImage, Image resizedImage) throws IOException {
        Graphics2D g2d = resizedBufferedImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedBufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        int imageIndex = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 0, 1, 1, 2);
        drawing.createPicture(anchor, imageIndex);
    }

    private void createSelfIntoductionSheet(String selfIntroduction) {
        Sheet sheet = workbook.createSheet("자기소개서");

        Row dataRow = sheet.createRow(0);
        Cell selfIntroductionCell = dataRow.createCell(0);
        selfIntroductionCell.setCellStyle(getWrapCellStyle());
        selfIntroductionCell.setCellValue(new XSSFRichTextString(selfIntroduction.replaceAll("\n", String.valueOf((char) 10))));
    }

    private XSSFCellStyle getWrapCellStyle() {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setWrapText(true);
        return style;
    }

    private void saveWorkbook2File() {
        try (FileOutputStream fileOutputStream = new FileOutputStream("이력서.xlsx")) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
