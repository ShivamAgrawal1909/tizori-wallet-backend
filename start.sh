#!/bin/sh

keytool -importcert \
-file /etc/secrets/ca.pem \
-keystore kafka.truststore.jks \
-storepass changeit \
-noprompt

openssl pkcs12 -export \
-in /etc/secrets/service.cert \
-inkey /etc/secrets/service.key \
-out kafka.keystore.p12 \
-name kafka-client \
-passout pass:changeit

java \
-Dspring.kafka.properties.ssl.truststore.location=$(pwd)/kafka.truststore.jks \
-Dspring.kafka.properties.ssl.keystore.location=$(pwd)/kafka.keystore.p12 \
-jar app.jar