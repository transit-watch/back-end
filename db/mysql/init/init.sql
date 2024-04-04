
GRANT ALL PRIVILEGES ON transit.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON transit.* TO 'twuser'@'%';

CREATE TABLE BUS_STOP_INFO (
                               ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '자동 증가하는 기본 키 ID',
                               STATION_ID CHAR(9) UNIQUE COMMENT '정류장 고유 ID',
                               STATION_NAME VARCHAR(255) COMMENT '정류장 이름',
                               ARS_ID CHAR(5) COMMENT '정류장 번호',
                               LINK_ID CHAR(10) COMMENT '링크 ID',
                               X_LATITUDE DOUBLE COMMENT '좌표X 위도',
                               Y_LONGITUDE DOUBLE COMMENT '좌표Y 경도',
                               USE_YN CHAR(1) COMMENT '사용 여부 (1: 사용, 0: 미사용)',
                               VIRTUAL_BUS_STOP_YN CHAR(1) COMMENT '가상 정류장 여부 (1: 가상정류장, 0: 일반정류장)',
                               REGISTER_DATE TIMESTAMP COMMENT '등록일시',
                               EDIT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) COMMENT '정류장 정보 테이블 (T-DATA)';

CREATE TABLE BUS_ROUTE (
                           ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '자동 증가하는 기본 키 ID',
                           ROUTE_ID CHAR(9) COMMENT '경로ID',
                           ROUTE_NAME VARCHAR(30) COMMENT '노선명',
                           ROUTE_ORDER INT COMMENT '정류장 순번',
                           STATION_ID CHAR(9) COMMENT '정류장 고유 ID(노드ID)',
                           ARS_ID CHAR(5) COMMENT '버스 정류장ID',
                           X_LATITUDE DOUBLE COMMENT '좌표X 위도',
                           Y_LONGITUDE DOUBLE COMMENT '좌표Y 경도',
                           REGISTER_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
                           EDIT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
                           UNIQUE(ROUTE_ID, STATION_ID, ARS_ID)
) COMMENT '버스 노선 정보 조회 테이블';

CREATE TABLE BUS_STOP_CROWDING (
                                   ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '자동 증가하는 기본 키 ID',
                                   ITIS_CD CHAR(5) COMMENT 'ITIS코드 (1545：승강장 혼잡 알림-보통 1546：승강장 혼잡알림-혼잡)',
                                   SEND_UTC_TIME TIMESTAMP COMMENT '전송UTC시간',
                                   Y_LONGITUDE DOUBLE COMMENT '좌표Y 경도 (DETC_LOT 검지 경도)',
                                   X_LATITUDE DOUBLE COMMENT '좌표X 위도 (DETC_LAT 검지 위도)',
                                   LINK_ID CHAR(30) COMMENT '링크 ID',
                                   ARS_ID CHAR(5) UNIQUE COMMENT '정류장 ID',
                                   SEND_PACKET_YEAR CHAR(4) COMMENT '패킷전송년도',
                                   SEND_PACKET_MONTH CHAR(2) COMMENT '패킷전송월',
                                   SEND_PACKET_DAY CHAR(2) COMMENT '패킷전송일',
                                   SEND_PACKET_TIME CHAR(6) COMMENT '패킷전송시간',
                                   SEND_PACKET_MILISECOND CHAR(4) COMMENT '패킷전송밀리초',
                                   RECORD_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '기록일시',
                                   REGISTER_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
                                   EDIT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) COMMENT '버스 승강장 혼잡 정보 테이블';

CREATE TABLE NEAR_BY_BUS_STOP (
                                  ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '자동 증가하는 기본 키 ID',
                                  STATION_ID CHAR(9) COMMENT '정류장 고유 ID',
                                  X_LATITUDE DOUBLE COMMENT '좌표X 위도 WGS84',
                                  Y_LONGITUDE DOUBLE COMMENT '좌표Y 경도 WGS84',
                                  ARS_ID CHAR(5) COMMENT '정류장 ID',
                                  DISTANCE INT COMMENT '거리',
                                  REGISTER_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
                                  EDIT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) COMMENT '반경 범위 내 정류장 정보 조회 테이블';

CREATE TABLE BUS_STOP_HOURLY_STATISTICS (
                                            ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '자동 증가하는 기본 키 ID',
                                            DATE_YM CHAR(6) COMMENT '연월',
                                            ROUTE_ID CHAR(9) COMMENT '경로 ID',
                                            STATION_ID CHAR(9) COMMENT '정류장 고유 ID',
                                            ARS_ID CHAR(5) COMMENT '정류장 번호',
                                            REGISTER_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
                                            EDIT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) COMMENT '월별 버스 승하차 인원 통계 테이블';

CREATE TABLE BUS_STOP_HOURLY_DATA (
                                      ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '자동 증가하는 기본 키 ID',
                                      STATISTICS_ID INT COMMENT '통계 ID, BUS_STOP_HOURLY_STATISTICS 테이블의 ID 참조',
                                      HOUR INT COMMENT '시간대: 0 ~ 23',
                                      UP_PEOPLE INT COMMENT '승차 인원',
                                      DOWN_PEOPLE INT COMMENT '하차 인원',
                                      REGISTER_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
                                      EDIT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) COMMENT '시간대별 승하차 인원 정보 테이블';

-- -- COMMENT '버스 승강장별 최근 혼잡정보 뷰';
-- CREATE VIEW BUS_STOP_CROWDING_VIEW AS
-- WITH LATEST_DATA AS (
--     SELECT *,
--            RANK() OVER(PARTITION BY ARS_ID ORDER BY RECORD_DATE DESC, SEND_PACKET_MILISECOND DESC) AS RANKS
--     FROM BUS_STOP_CROWDING
-- )
-- SELECT ID, ITIS_CD, LINK_ID, ARS_ID
-- FROM LATEST_DATA
-- WHERE RANKS = 1;