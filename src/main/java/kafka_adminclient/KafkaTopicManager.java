package kafka_adminclient;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;

import org.apache.kafka.common.protocol.types.Field;


import java.util.*;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;

public class KafkaTopicManager {
    private static AdminClient adminClient;
    public KafkaTopicManager(){
        Properties props = new Properties();
        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        adminClient = adminClient.create(props);
    }

    public static void createTopic(String topicName, int numPartitions, short replicationFactor) throws ExecutionException, InterruptedException {
        NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
        CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));

        createTopicsResult.all().get();
        listTopic();
    }

    public static void listTopic() throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        StringBuilder stringBuilder = new StringBuilder();

        for (String s: listTopicsResult.names().get()){
            stringBuilder.append(s).append("\n");
        }

        System.out.println(stringBuilder.toString());
    }

    public static void describeTopic(String topicName) throws ExecutionException, InterruptedException {
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singleton(topicName));
        describeTopicsResult.all().get();

        TopicDescription topicDescription = describeTopicsResult.values().get(topicName).get();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(topicDescription.name()).append("\n");
        stringBuilder.append(topicDescription.partitions()).append("\n");

        System.out.println(stringBuilder.toString());
    }

    public static void deleteTopic(String topicName) throws ExecutionException, InterruptedException {
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singleton(topicName));
        deleteTopicsResult.all().get();

        listTopic();
    }
    public static void describeConfig(String topicName) throws ExecutionException, InterruptedException {
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singleton(configResource));

        System.out.println(describeConfigsResult.all().get());
    }

    public static void changeConfig(String topicName, String configKey, String configValue) throws ExecutionException, InterruptedException {
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        ConfigEntry configEntry = new ConfigEntry(configKey, configValue);
        Config config = new Config(Collections.singleton(configEntry));

        Map<ConfigResource, Config> configuration = new HashMap<>();
        configuration.put(configResource, config);

        Map<ConfigResource, Config> configMap = new HashMap<ConfigResource, Config>();
        adminClient.alterConfigs(configuration);

        describeTopic(topicName);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaTopicManager kafkaTopicManager = new KafkaTopicManager();

        String command = args[0];

        if ("create".equalsIgnoreCase(command)){
            KafkaTopicManager.createTopic(args[1], Integer.parseInt(args[2]), Short.parseShort(args[3]));
        }
        else if ("list".equalsIgnoreCase(command)){
            KafkaTopicManager.listTopic();
        }
        else if ("describe".equalsIgnoreCase(command)){
            KafkaTopicManager.describeTopic(args[1]);
        }
        else if ("describe-config".equalsIgnoreCase(command)){
            KafkaTopicManager.describeConfig(args[1]);
        }
        else if ("alter-config".equalsIgnoreCase(command)){
            KafkaTopicManager.changeConfig(args[1], args[2], args[3]);
        }
        else if ("delete".equalsIgnoreCase(command)){
            KafkaTopicManager.deleteTopic(args[1]);
        }
        else System.out.println("지원하지 않는 기능입니다.");
    }

}
