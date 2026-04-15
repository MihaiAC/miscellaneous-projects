### Keywords
- Horizontal vs Vertical Scaling
- Cold/warm standby
- MongoDB vs Cassandra sharding approaches ("master", replica set, mongos, config servers vs ring, eventual consistency, decentralized)
- Denormalizing
- Data lakes - (creating schema Glue, querying Athena/Redshift)
- ACID (Atomicity, Consistency, Isolation, Durability)
- CAP theorem - Availability, Consistency, Partition-Tolerance - choose two - which two are the most important for the given use case?
- Caching - expiration policy (LRU, LFU, FIFO), hotspots, cold-start
- Caching techs: Redis, ElastiCache, memcached, ncache, ehcache
- CDNs for static content, expensive
- AZs, regions redundancy
- Distributed storage
- HDFS
- Message queues - (producers - queue - consumers)
- Spark - YARN, Spark streaming with Kinesis/Kafka; Streaming, SQL, MLLib, Core.

### CCI
#### horizontal vs vertical scaling
- vertical == increasing the resources of a specific node/instance (e.g: add additional memory to a server);
- horizontal == # nodes ++

#### load balancer
have multiple servers with the same code + access to the same data

#### database denormalization, SQL vs noSQL
- denorm. == adding extra info to avoid expensive joins;
- noSQL => no joins, but data is structured differently; the queries you want to answer decide how you'll structure the DB;

#### database partitioning
- sharding == splitting data across machines + having a way to understand which machine has the data you're interested in;
- vertical partitioning == partitioning a DB by feature; e.g: one partition for tables related to profiles, another for messages, etc.
- hash-based partitioning == allocate N servers, use part of the data to compute a hash, which decides what server it goes on;
- directory-based partitioning == lookup table, which can become the single point of failure;

#### caching + async processing and queues
- remember to consider stale data;

### networking
- metrics to keep in mind: bandwidth (max amount of data that can be transferred / time), throughput (actual amount of data that is transferred), latency;
- conveyor belt metaphor;

#### general considerations
- failures: every part can fail, plan accordingly;
- availability == percentage of time the system is operational;
- reliability == probability that the system is operational for a certain unit of time;
- read-heavy vs write-heavy applications (former: caching, latter: queues);
- security;