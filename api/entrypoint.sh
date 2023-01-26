#!/bin/sh
export OTEL_EXPORTER_OTLP_ENDPOINT=${OTEL_EXPORTER_OTLP_ENDPOINT:-http://tempo.observability.svc.cluster.local:4318}
env
java -javaagent:/app/otelagent.jar \
  -Dotel.resource.attributes=service.name=nsctool-api-agent \
  -Dotel.traces.exporter=otlp \
  -Dotel.exporter.otlp.protocol=http/protobuf \
  -Dotel.metrics.exporter=none \
  -Dotel.logs.exporter=none \
  -Dotel.exporter.otlp.traces.endpoint=$OTEL_EXPORTER_OTLP_ENDPOINT/v1/traces \
  -jar /app/api.jar