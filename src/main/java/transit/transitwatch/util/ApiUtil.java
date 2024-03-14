package transit.transitwatch.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;


@Component
public class ApiUtil {

    /*
    * 공통_파일 다운로드
    * */
    public void fileDownload(String inputUrl, String fileName){
        String saveDir = "/Users/yeyoung/Desktop/api 데이터 파일/";

//        Path savePath = Paths.get(saveDir + fileName);
        try(FileOutputStream fos = new FileOutputStream(saveDir + fileName)) {
            URL url = new URL(inputUrl);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());

            fos.getChannel().transferFrom(rbc,0,Long.MAX_VALUE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
     * 공통_파일 파싱하기
     * */
    public Iterable<CSVRecord> getCsvRecords(BufferedReader br, Class<? extends Enum<?>> header) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.Builder.create()
                .setHeader(header)
                .setSkipHeaderRecord(true)
                .build()
                .parse(br);
        return records;
    }

    /*
     * 공통_xlsx파일 csv로 변환
     * */
    public void convertXlsxToCSV(String inputUrl, String fileName) {
        Path savePath = Paths.get(inputUrl + fileName + ".csv");
        Path loadPath = Paths.get(inputUrl + fileName + ".xlsx");
        try (
                InputStream is = Files.newInputStream(loadPath);
                XSSFWorkbook workbook = new XSSFWorkbook(is);
                BufferedWriter writer = Files.newBufferedWriter(savePath, StandardCharsets.UTF_8)
        ){
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIter = sheet.iterator();
            while(rowIter.hasNext()){
                Row row = rowIter.next();
                boolean firstCell = true;
                Iterator<Cell> cellIter = row.cellIterator();

                while (cellIter.hasNext()) {
                    if (!firstCell) writer.write(",");

                    Cell cell = cellIter.next();
                    String cellValue = getCell(cell);
                    writer.write(cellValue);

                    firstCell = false;
                }
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * excel의 셀 데이터를 문자열로 변환
     * */
    public String getCell(Cell cell) {
        //
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    /*
     * BOM 문자 제거 - 컬럼 하나하나 제거
     * */
    public String removeBOM(String value) {
        return value.replace("\ufeff", "");
    }
}
