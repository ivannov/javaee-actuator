#!/bin/bash

cd javaee-actuator
mvn clean install
cd ../javaee-actuator-showcase
mvn clean install
cd ..

