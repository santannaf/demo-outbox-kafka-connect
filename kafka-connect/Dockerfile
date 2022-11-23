FROM confluentinc/cp-kafka-connect-base:7.1.0

ENV TZ America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV CONNECT_PLUGIN_PATH="/usr/share/java,/usr/share/confluent-hub-components"

RUN echo 'Installing connector plugins'

RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:latest \
    && confluent-hub install --no-prompt debezium/debezium-connector-postgresql:latest \
    && confluent-hub install --no-prompt debezium/debezium-connector-mysql:latest \
    && confluent-hub install --no-prompt confluentinc/connect-transforms:latest

ENTRYPOINT ["sh","-c","export CONNECT_REST_ADVERTISED_HOST_NAME=$(hostname -I);/etc/confluent/docker/run"]