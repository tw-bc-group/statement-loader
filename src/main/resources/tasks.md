1. 修改从API到MysqlLoader中有关AccountCenter的名字 **√**
2. 完成MysqlLoader正确的时间处理 **√**
3. RestJSonReader配置化 **√**
4. drop statement中有关job的表，修改配置，让job的信息只记录在loader中 **√**
5. 抽取LoadingService -> 分别针对两个Bean，在REST API中注入 **√**
6. bug: loading bridge数据第二次为空（commit 2c800改坏了）**√**
7. 修改REST reader的实现，过滤时间，同时修改moco的配置
8. 查看Reader的实现机制，修改REST reader的实现，采取分页load数据的方式
9. 查看Job运行是否是异步的，考虑CompletableFuture或Await
10. chunkSize配置化
11. application的dockerfile
12. docker-compose文件
    - application
    - loader-mysql
    - payment-records-mysql
    - mock server(moco)