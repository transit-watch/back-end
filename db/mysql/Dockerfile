FROM mysql:latest

# MySQL 설정 파일을 이미지 안의 설정 디렉토리로 복사
COPY conf.d /etc/mysql/conf.d/

# 데이터베이스 초기화에 사용될 SQL 스크립트를 이미지 안으로 복사
COPY init/init.sql /docker-entrypoint-initdb.d/

ENV MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
ENV MYSQL_ROOT_HOST="%"
ENV MYSQL_DATABASE=${MYSQL_DATABASE}
ENV MYSQL_USER=${MYSQL_USER}
ENV MYSQL_PASSWORD=${MYSQL_PASSWORD}
ENV TZ=${MYSQL_TZ}

EXPOSE 3306