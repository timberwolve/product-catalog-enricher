## Table of contents
* [General info](#general-info)
* [Current status](#current-status)
* [To do](#to-do)
* [Future improvements](#future-improvements)
* [Setup](#setup)
* [Run](#run)

## General info
Main purpose of this project is to enrich cv using opencsv

## Current status
The most challenging part of working with open source technologies is they don't work as they should. 
Currently we are trying to reach out proper behaviour of csv to bean conversions.

## To do
* fix the conversion from csv to bean
* write tests

## Future improvements
* use spring integration payload enricher
* use caching or db mechanism

## Setup
To run this project, you should run the command :bootRun, using gradle.

## Run
curl --data-binary @src/test/resources/static/trade.csv --header 'Content-Type: text/csv' http://localhost:8080/api/v1/enrich

