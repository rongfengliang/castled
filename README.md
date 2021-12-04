## Introduction

Castled is a Reverse ETL solution which enables you to make the valuable customer insights lying on your data warehouse actionable by moving them to sales, markerting and support tools of your choice. We currently support all major data warehouses incluing Snowflake, BigQuery, Redshift and Postgres as the data source and some of the most popular sales, marketing and support solutions such as Salesforce, Hubspot, Intercom, Google Ads etc as the destination.


## Getting Started
```
git clone https://github.com/castledio/castled.git
cd castled
docker-compose up
```
  
You can access castled app at http://localhost:3000. Happy castling!!

## Performance

The throughput/latency of a data pipeline depends mostly on the destination api limitations/rate limits. But to give an indication of the throughput we support, publishing a bench mark we did to transfer 100 million records from BigQuery to Apache Kafka.

## Connector Development

We have spent an insane amount of time customizing our framework in such a way to reduce the amount of time required to build a connector.

1) Implement a couple of interfaces(DataSink and ExternalAppConnector)
