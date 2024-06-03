FROM maven:latest as builder
COPY --chown=maven:maven . /home/simon/1_Coding/java/JavaGame
