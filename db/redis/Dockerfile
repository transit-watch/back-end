FROM redis/redis-stack-server:latest

COPY conf.d/redis-stack.conf /usr/local/etc/redis/redis.conf

EXPOSE 6379

CMD [ "/usr/bin/redis-stack-server", "/usr/local/etc/redis/redis.conf" ]