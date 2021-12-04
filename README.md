## Introduction

Castled is a **Reverse ETL** solution which enables you to make the valuable customer insights lying on your data warehouse actionable by moving them to sales, markerting and support tools of your choice. We currently support all major data warehouses incluing *Snowflake*, *BigQuery*, *Redshift* and *Postgres* as the data source and some of the most popular sales, marketing and support solutions such as *Salesforce*, *Hubspot*, *Intercom*, *Google Ads* etc as the destination.


## Getting Started
```
git clone https://github.com/castledio/castled.git
cd castled
docker-compose up
```
  
You can access castled app at http://localhost:3000. Happy castling!!

## Performance

The throughput/latency of a data pipeline depends mostly on the destination api limitations/rate limits. But to give an indication of the throughput we support, publishing a bench mark we did to transfer upto 100 million records from BigQuery to Apache Kafka.

![kafka benchmark](https://cdn.castled.io/content/kafka_benchmark.png)

## Connector Development

We have spent an insane amount of time customizing our framework in such a way, so as to reduce the amount of time required to build a connector with zero changes required on the UI front. Heres all you need to do to add your own connector.

1) We understand that each connector requires a totally different set of input for it to have the flexibility to cover all our usecases and hence we have build a java annotation based framework, which can drive any kind of forms that you need in the connector configuration. You can refer the customerio configuration [here](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/apps/connectors/customerio/CustomerIOAppSyncConfig.java)

2) Implement a couple of interfaces([ExternalAppConnector](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/apps/ExternalAppConnector.java) and [DataSink](github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/apps/DataSink.java)) on the backend.

