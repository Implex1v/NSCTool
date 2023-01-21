#!/bin/sh
env
OTEL_EXPORTER_OTLP_ENDPOINT=$OTEL_EXPORTER_OTLP_ENDPOINT OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://tempo.observability.svc.cluster.local:4318/v1/traces java -javaagent:/app/otelagent.jar -jar /app/api.jar