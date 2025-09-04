# Real Time Inventory and Price Service

![CI](https://github.com/GrandhiSahithi/realtime-inventory/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-21-green)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-brightgreen)
![Cassandra](https://img.shields.io/badge/Cassandra-4.1-blue)
![Kafka](https://img.shields.io/badge/Kafka-3.7-blueviolet)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

A JVM service that answers product availability and price for a given store or region with sub-second latency.  
Backed by **Cassandra** for data and **Kafka** for event ingestion. Local cache handles hot keys.

<p align="center">
  <img src="https://raw.githubusercontent.com/github/explore/main/topics/spring-boot/spring-boot.png" alt="Spring" width="72"/>
  <img src="https://raw.githubusercontent.com/github/explore/main/topics/apache-cassandra/apache-cassandra.png" alt="Cassandra" width="72"/>
  <img src="https://raw.githubusercontent.com/github/explore/main/topics/apache-kafka/apache-kafka.png" alt="Kafka" width="72"/>
</p>

---

## 🚀 Quick start

```bash
# 1. Start infra
docker compose up -d

# 2. Run the app
./gradlew bootRun
