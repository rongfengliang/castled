---
sidebar_position: 2
---

# New destination connector

With Castled Form Langauge(CFL), its really easy to write a new destination connector in Castled. Lets take the example of a Kafka connector to explain the process step by step.

### Configuring the app connection

Creating an app connection is a prerequisite to creating a pipeline. [AppConfig](https://github.com/castledio/castled/blob/main/connectors/src/main/java/io/castled/apps/AppConfig.java) class contains the connection params required to establish connection to our destination app. We have to extend the AppConfig class and mention the connection parameters in the sub class.

```
@Getter
@Setter
public class KafkaAppConfig extends AppConfig {

    @FormField(title = "Bootstrap Servers", placeholder = "eg: host1:9092, host2:9092", 
    schema = FormFieldSchema.STRING, type = FormFieldType.TEXT_BOX)
    private String bootstrapServers;
}

```

Here










