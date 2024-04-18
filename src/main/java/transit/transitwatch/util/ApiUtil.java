package transit.transitwatch.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import transit.transitwatch.exception.ApiRequestException;
import transit.transitwatch.exception.CustomException;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static transit.transitwatch.util.ErrorCode.*;

/**
 * 외부 API와 통신하거나 파일을 처리하는 유틸리티 메서드를 제공하는 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ApiUtil {
    private final RestTemplate restTemplate;

    @Value("${app.file.path}")
    private String filePath;

    /**
     * 주어진 URI를 사용하여 외부 API로부터 데이터를 가져온다.
     *
     * @param uri API의 URI
     * @return API로부터 받은 응답 데이터를 문자열로 반환한다.
     * @throws ApiRequestException API 요청 실패 시 발생
     */
    public String getApiUri(URI uri) {
        try {
            return restTemplate.getForObject(uri, String.class);
        } catch (RestClientException e) {
            log.error("API 가져오기 실패" + uri);
            throw new ApiRequestException(API_REQUEST_FAIL);
        }
    }

    /**
     * 지정된 URL에서 파일을 다운로드하고, 지정된 경로에 저장한다.
     * 파일 크기가 1MB 이상인 경우에만 다운로드 성공으로 간주한다.
     *
     * @param inputUrl 파일을 다운로드할 URL
     * @param fileName 저장할 파일의 이름
     * @return 성공적으로 파일이 다운로드되고 저장되었으면 true, 아니면 false 반환
     * @throws CustomException 파일 다운로드 실패 시 발생
     */
    public boolean fileDownload(String inputUrl, String fileName) {
        Path targetPath = Paths.get(filePath + fileName);

        try (ReadableByteChannel rbc = Channels.newChannel(new URL(inputUrl).openStream());
             FileOutputStream fos = new FileOutputStream(targetPath.toFile())) {

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return checkFileSize(targetPath, 1_000_000); // 1MB 이상
        } catch (IOException e) {
            log.error("파일 다운로드 실패 : " + inputUrl);
            throw new CustomException(FILE_DOWNLOAD_FAIL);
        }
    }

    /**
     * 지정된 파일의 크기가 주어진 최소 크기 이상인지 검증한다.
     * 파일 크기가 최소 크기보다 작으면 예외를 발생시킨다.
     *
     * @param file 검사할 파일의 경로
     * @param minSize 파일의 최소 허용 크기 (바이트 단위)
     * @return 파일 크기가 최소 크기 이상이면 true를 반환한다.
     * @throws IOException 파일 크기를 읽는 과정에서 I/O 오류가 발생한 경우
     * @throws CustomException 파일 크기가 비정상적으로 작을 경우 발생
     */
    private boolean checkFileSize(Path file, long minSize) throws IOException {
        long fileSize = Files.size(file);
        if (fileSize < minSize) {
            log.error("다운로드 받은 파일 사이즈가 비정상 입니다. : " + fileSize + " 바이트");
            throw new CustomException(FILE_NOT_CORRECT);
        }
        return true;
    }

    /**
     * BufferedReader를 통해 읽은 CSV 데이터에서 필요한 정보를 파싱한다.
     *
     * @param br 데이터를 읽기 위한 BufferedReader
     * @param header 파싱할 때 사용할 CSV 파일의 헤더 정보
     * @return 파싱된 CSV 레코드의 Iterable
     * @throws CustomException CSV 파싱 실패 시 발생
     */
    public Iterable<CSVRecord> getCsvRecords(BufferedReader br, Class<? extends Enum<?>> header){
        try {
            return CSVFormat.Builder.create()
                    .setHeader(header)
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(br);
        } catch (IllegalArgumentException | IOException e) {
            log.error("CSV 파일 파싱에 실패했습니다. ");
            throw new CustomException(PARSING_FAIL);
        }
    }

    /**
     * XLSX 파일을 CSV 파일로 변환하는 메서드.
     * @param inputUrl 파일이 위치한 경로
     * @param fileName 변환할 파일의 이름 (확장자 없음)
     * @throws CustomException 파일 변환 실패 시 발생
     */
    public void convertXlsxToCSV(String inputUrl, String fileName) {
        Path savePath = Paths.get(inputUrl + fileName + ".csv");
        Path loadPath = Paths.get(inputUrl + fileName + ".xlsx");
        try (
                InputStream is = Files.newInputStream(loadPath);
                XSSFWorkbook workbook = new XSSFWorkbook(is);
                BufferedWriter writer = Files.newBufferedWriter(savePath, StandardCharsets.UTF_8)
        ) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIter = sheet.iterator();
            while (rowIter.hasNext()) {
                Row row = rowIter.next();
                boolean firstCell = true;
                Iterator<Cell> cellIter = row.cellIterator();

                while (cellIter.hasNext()) {
                    if (!firstCell) writer.write(",");

                    Cell cell = cellIter.next();
                    String cellValue = getCell(cell);
                    // 데이터에 쉼표 들어있으면 .으로 변환하기
                    writer.write(cellValue.replaceAll(",", "."));

                    firstCell = false;
                }
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("XLSX 파일을 CSV 파일로 변환에 실패했습니다. ");
            throw new CustomException(PARSING_FAIL);
        }
    }

    /**
     * Excel 셀의 데이터를 문자열로 가져오는 메서드.
     * @param cell 데이터를 가져올 Excel 셀
     * @return 셀의 데이터를 문자열로 반환한다.
     * @throws CustomException 문자열로 변환 실패 시 발생
     */
    public String getCell(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        try {
            return formatter.formatCellValue(cell);
        } catch (Exception e) {
            log.error("Excel 셀의 데이터를 문자열로 변환에 실패했습니다. ");
            throw new CustomException(PARSING_FAIL);
        }
    }

    /**
     * 문자열에서 BOM(Byte Order Mark)을 제거하는 메서드.
     * 항목 하나당 BOM문자 제거.
     * @param value 처리할 문자열
     * @return BOM이 제거된 문자열
     */
    public String removeBOM(String value) {
        return value.replace("\ufeff", "");
    }

    /**
     * 주어진 코드에 해당하는 혼잡도 ENUM을 반환하는 메서드.
     * @param code 혼잡도 코드
     * @return 해당 코드와 일치하는 {@link ItisCdEnum} 값, 일치하는 코드가 없으면 ItisCdEnum.NODATA를 반환
     */
    public ItisCdEnum getCrowdingEnum(String code) {
        for (ItisCdEnum itisCdEnum : ItisCdEnum.values()) {
            if (code.equals(itisCdEnum.getCode()) || code.equals(itisCdEnum.getCode2())) {
                return itisCdEnum;
            }
        }
        return ItisCdEnum.NODATA;
    }

    /**
     * 텍스트에서 정규식에 해당하는 부분을 추출하는 메서드.
     * [ ] 사이의 값을 반환한다.
     * @param text 처리할 전체 텍스트
     * @return 정규식에 일치하는 첫 번째 그룹, 일치하는 부분이 없으면 null 반환
     */
    public String getRegex(String text){

        String regex = "\\[(.*?)\\]";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
           return matcher.group(1);
        }
        return null;
    }
}
