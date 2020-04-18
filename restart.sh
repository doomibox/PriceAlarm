#!/bin/bash

# stop current containers
docker stop $(docker ps -f status=running -q)
# remove dangling containers
yes | docker system prune
# remove all images
docker image rm $(docker images -q)
# pull and run container with new image
docker run -d -p 8080:8080 605490103309.dkr.ecr.us-west-2.amazonaws.com/pricealarmrepo:LATEST
# follow logs (ctrl+c to exit)
docker logs --follow $(docker ps -f status=running -q)