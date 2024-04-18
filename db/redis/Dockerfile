FROM redis/redis-stack-server:latest

COPY conf.d/redis-stack.conf /redis-stack.conf
ENV REDISEARCH_ARGS="MAXSEARCHRESULTS 10000 MAXAGGREGATERESULTS 10000"
#ENV BASEDIR=/opt/redis-stack
#ENV CMD=${BASEDIR}/bin/redis-server
ENV CONFFILE=/redis-stack.conf

CMD ["redis-server", "--loadmodule", "/opt/redis-stack/lib/redisearch.so"]
EXPOSE 6379

