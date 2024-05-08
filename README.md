# 서울시 정류장 혼잡도 조회 서비스 (back-end)
정류장이 붐비는건 싫어!

공공 데이터를 이용한 서울시 정류장의 혼잡도를 조회하는 서비스 입니다.

정류장 혼잡도를 사전에 파악하여 출퇴근 시간에 지하철 파업, 시위 등 돌발상황 발생 시 대중교통 이용률을 분산시킬 수 있도록 버스 정류장과 버스 내 혼잡도를 손쉽게 확인할 수 있으면 좋겠다는 생각에 개발하게 되었습니다.

## 배포 URL
https://transit-watch.vercel.app/

## 프로젝트 구조
![](https://i.imgur.com/Y8fa1V1.png)


## 기술 스택

- Spring Boot (3.2.5)
- Java (21)
- Gradle
- Spring Data JPA
- Spring Rest Docs
- QueryDSL
- Redis
- MySQL
- Prometheus 
- Grafana 
- K6
- Github Actions 
- Docker 
- NCP

## ERD 
![](https://i.imgur.com/7i3RRMK.png)

[https://dbdiagram.io/d/transit-watch-65d425b4ac844320ae87c131](https://dbdiagram.io/d/transit-watch-65d425b4ac844320ae87c131)


## 사용한 공공 데이터
- 버스 승강장 혼잡 정보 서비스
	- [https://t-data.seoul.go.kr/dataprovide/trafficdataviewopenapi.do?data_id=10111](https://t-data.seoul.go.kr/dataprovide/trafficdataviewopenapi.do?data_id=10111)
- 서울특별시 버스 도착 정보 조회 서비스 - 경유노선 전체 정류소 도착 예정 정보
	- https://www.data.go.kr/data/15000314/openapi.do
- 서울시 버스 노선 정보 조회
	- [http://data.seoul.go.kr/dataList/OA-1095/F/1/datasetView.do](http://data.seoul.go.kr/dataList/OA-1095/F/1/datasetView.do)
- 정류장 정보
	- [T Data 서울교통빅데이터플랫폼 > 파일데이터 상세정보 (seoul.go.kr)](https://t-data.seoul.go.kr/dataprovide/trafficdataviewfile.do?data_id=64)
- 서울시 버스정류소 위치정보
	- https://data.seoul.go.kr/dataList/OA-15067/S/1/datasetView.do 
- 서울특별시 정류소 정보 조회 서비스 - 좌표 기반 근접 정류소 조회
	- [https://www.data.go.kr/data/15000303/openapi.do](https://www.data.go.kr/data/15000303/openapi.do)

## 팀원
- Front-end
	- [@sydney-choi](https://github.com/sydney-choi)
- Back-end
	- [@yeyounglim](https://www.github.com/yeyounglim)


## Badges
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fgjbae1212%2Fhit-counter&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
