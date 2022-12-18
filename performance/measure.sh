#!/bin/bash
PORT=8089
PID=$(sudo lsof -iTCP:${PORT} | head -n 2 | grep ${PORT} | tr -s ' ' | cut -d' ' -f2)
sudo cat /proc/$PID/smaps | grep -i pss |  awk '{Total+=$2} END {print Total/1024" MB"}'

