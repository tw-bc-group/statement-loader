1. 修改从API到MysqlLoader中有关AccountCenter的名字 **√**
2. 完成MysqlLoader正确的时间处理 **√**
3. RestJSonReader配置化 **√**
4. drop statement中有关job的表，修改配置，让job的信息只记录在loader中 **√**
5. 抽取JobService -> 分别针对两个Bean，在REST API中注入
6. 查看Job运行是否是异步的，考虑CompletableFuture或Await
7. 查看Reader的实现机制，修改REST reader的实现，采取分页load数据的方式
8. chunkSize配置化
9. application的dockerfile
10. docker-compose文件
    - application
    - loader-mysql
    - payment-records-mysql
    - mock server(moco)