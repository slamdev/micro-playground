FROM graylog2/server:latest

HEALTHCHECK --interval=5s --timeout=3s --retries=10 CMD curl --fail http://localhost:9000/system/lbstatus || exit 1
