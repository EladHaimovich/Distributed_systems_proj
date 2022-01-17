RUN echo "$ShardID $ServerID $ZookeeperPort" > ./parameters && cat ./parameters
java -jar ./build/libs/SystemServer-0.0.1.jar $ShardID $ServerID $ZookeeperPort