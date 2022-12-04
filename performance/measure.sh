#!/bin/bash
PORT=8089
PID=$(lsof -iTCP:${PORT} | grep ${PORT} | tr -s ' ' | cut -d' ' -f2)
cat /proc/$PID/smaps | grep -i pss |  awk '{Total+=$2} END {print Total/1024" MB"}'