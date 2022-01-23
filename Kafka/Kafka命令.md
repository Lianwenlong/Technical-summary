# Kafka常用命令

## Topic

### 创建topic

```shell
./kafka-topics.sh --create --zookeeper localhost:2181 --partitions 3 --replication-factor 3 --topic topicName
```

### 删除topic

```shell
./kafka-topics.sh --delete --zookeeper localhost:2181 --topic topicname 
```

### 修改topic

```shell
./kafka-topics.sh --alter --zookeeper localhost:2181 --partitions 6 --topic topicname 
```

### 查看topic信息

```shell
./kafka-topics.sh --zookeeper localhost:2181 --describe --topic topicname 
```

### 查看topic列表

```shell
./kafka-topics.sh --zookeeper localhost:2181 --list
```

### 查看topic当前消息数

```shell
./kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic topicname --time -1
# --time -1表示最大位移 --time -2表示最早位移
```



## Consumer-Group

### 查看消费组列表

```shell
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list 
```

### 删除消费组

```shell
./kafka-consumer-groups.sh --zookeeper localhost:2181 --delete --group groupname
```

### 查看消费组详情

```shell
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group groupname 
```

### 重设消费者组位移(0.11版本之前没有)

```shell
## earliest
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group groupname --reset-offsets --all-topics --to-earliest --execute
## latest
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group groupname --reset-offsets --all-topics --to-latest --execute
## 指定位置
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group groupname --reset-offsets --all-topics --to-offset 1000 --execute
## 调整到某个时间之后得最早位移
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group groupname --reset-offsets --all-topics --to-datetime 2022-01-23T10:03:00.000
```

### 查询_consumer_offsets

```shell
./kafka-simple-consumer-shell.sh --topic _consumer_offsets --partition 12 --broker-list localhost:9092 --formatter "kafka.coorfinator.GroupMetadataManager\$OffsetsMessageFormatter"
```

