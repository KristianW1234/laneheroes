#!/bin/sh
set -e  # exit on error

# Ensure directories exist
mkdir -p /app/img/company \
         /app/img/game \
         /app/img/hero \
         /app/img/skill/dota \
         /app/img/skill/lol \
         /app/img/skill/smite \
         /app/img/skill/hots \
         /app/img/skill/hok \
         /app/img/skill/aov \
         /app/img/skill/mlbb \
         /app/img/skill/oa \
         /app/img/skill/hon

# Only copy the default if not already mounted/present
if [ ! -f /app/img/noimage.png ]; then
  cp /app/defaults/noimage.png /app/img/noimage.png
fi

# Start Spring Boot app
exec java -jar app.jar