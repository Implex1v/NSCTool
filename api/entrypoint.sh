#!/bin/sh
OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://tempo.observability.svc.cluster.local:4318/v1/traces
env

java -javaagent:/app/otelagent.jar \
  -Dotel.exporter.otlp.endpoint=$OTEL_EXPORTER_OTLP_TRACES_ENDPOINT \
  -Dotel.exporter.otlp.traces.endpoint=$OTEL_EXPORTER_OTLP_TRACES_ENDPOINT \
  -jar /app/api.jar