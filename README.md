# kafka-adminclient
## 개발환경 구성
#### Single Zookeeper, Single Kafka Cluster

- Docker Image : Confluent/Zookeeper:5.2.1, Confluent/Kafka:5.2.1
- Confuent Compatibility
    - Confluent : v.7.0, Apache Kafka : v.3.0
    - Confluent : v.5.2.1, Apahce Kafka : v.2.2.1
    
#### Kafka AdminClient API (v.2.2.1)
Kafka AdminClient v.2.2.1 는 Topic 에 대해서만 설정 변경을 지원합니다.
## 테스트 방법
~~~
create {topicName} {numPartitions} {replicationFactor}
list
describe {topicName}
describe-config {topicName}
alter-config {topicName} {configKey} {configValue}
delete {topicNmae}
~~~

## 구현 내용
Kafka AdminClient API (v.2.2.1) 을 활용하여 Management Plane 구현

- 토픽 생성
- 모든 토픽 조회
- 지정 토픽 설정 조회
- 지정 자원(broker/topic) 의 설정 조회
- 토픽 삭제
- 토픽 설정 변경