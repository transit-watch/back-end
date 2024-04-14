package transit.transitwatch.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
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


@RequiredArgsConstructor
@Component
public class ApiUtil {
    private final RestTemplate restTemplate;

    @Value("${app.file.path}")
    private String filePath;

    /**
     * URI를 사용하여 외부 API로부터 데이터를 가져오는 메서드.
     * @param uri API의 URI
     * @return API로부터 받은 응답 데이터를 문자열로 반환한다.
     * @throws Exception 네트워크 오류 또는 다른 이유로 데이터를 가져오는데 실패한 경우
     */
    public String getApiUri(URI uri) throws Exception {
        String forObject = restTemplate.getForObject(uri, String.class);

        return forObject;
    }

    /**
     * 지정된 URL에서 파일을 다운로드하고 저장하는 메서드.
     * 파일 크기가 1MB 이상일 때만 성공으로 간주한다.
     * @param inputUrl 파일을 다운로드할 URL
     * @param fileName 저장할 파일의 이름
     * @return 파일 다운로드 및 저장이 성공하면 true, 실패하면 false를 반환한다.
     */
    public boolean fileDownload(String inputUrl, String fileName) {

        try (FileOutputStream fos = new FileOutputStream(filePath + fileName)) {
            URL url = new URL(inputUrl);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            long fileSize = Files.size(Path.of(filePath + fileName));
            if (fileSize < 1000000) { // 1mb 이상
                System.out.println("파일이 비어있습니다.");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * BufferedReader를 통해 읽은 CSV 데이터에서 필요한 정보를 파싱하는 메서드.
     * @param br BufferedReader 인스턴스
     * @param header CSV 파일의 헤더에 해당하는 ENUM 클래스
     * @return 파싱된 CSV 레코드의 Iterable
     * @throws IOException 파일 읽기 중 오류 발생 시
     */
    public Iterable<CSVRecord> getCsvRecords(BufferedReader br, Class<? extends Enum<?>> header) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.Builder.create()
                .setHeader(header)
                .setSkipHeaderRecord(true)
                .build()
                .parse(br);
        return records;
    }

    /**
     * XLSX 파일을 CSV 파일로 변환하는 메서드.
     * @param inputUrl 파일이 위치한 경로
     * @param fileName 변환할 파일의 이름 (확장자 없음)
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Excel 셀의 데이터를 문자열로 가져오는 메서드.
     * @param cell 데이터를 가져올 Excel 셀
     * @return 셀의 데이터를 문자열로 반환한다.
     */
    public String getCell(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
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

    /**
     * 주어진 URL 문자열을 {@link URI} 객체로 변환하는 메서드.
     * <p>이 메서드는 문자열 형태의 URL을 받아 {@link URI} 객체로 변환한다.</p>
     * @param url 변환하고자 하는 URL 문자열
     * @return 변환된 {@link URI} 객체
     * @throws RuntimeException URL 문자열의 형식이 잘못되어 {@link URISyntaxException}이 발생한 경우
     */
    public URI getConvertUri(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }
}
