# LanParty Manager

[![Travis-CI Build Status](https://travis-ci.org/thehellnet/lanparty-manager.svg?branch=master)](https://travis-ci.org/thehellnet/lanparty-manager)
[![Github Action Check](https://github.com/thehellnet/lanparty-manager/workflows/Check/badge.svg)](https://github.com/thehellnet/lanparty-manager/actions)
[![CodeCov](https://codecov.io/gh/thehellnet/lanparty-manager/branch/master/graph/badge.svg)](https://codecov.io/gh/thehellnet/lanparty-manager)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=thehellnet_lanparty-manager&metric=alert_status)](https://sonarcloud.io/dashboard?id=thehellnet_lanparty-manager)

> Custom tool for managing Lan Parties and Tournaments

## Requirements for Development

- PostgreSQL
- ActiveMQ

### PostgreSQL

Install PostgreSQL:

```shell script
apt install postgresql postgresql-client
```

Create roles and databases:

```postgresql
CREATE USER lanparty PASSWORD 'lanparty';
CREATE DATABASE lanparty OWNER lanparty;

CREATE USER test PASSWORD 'test';
CREATE DATABASE test OWNER test;
```

### ActiveMQ

Install and activate default ActiveMQ:

```shell script
apt install activemq
ln -sv ../instances-available/main /etc/activemq/instances-enabled/
systemctl restart activemq.service
```

## Build & Tests

To generate WAR for Apache Tomcat WebServer:

```shell script
./gradlew war
```

To run all tests and produce Jacoco reports:

```shell script
./gradlew check
```
