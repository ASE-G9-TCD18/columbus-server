# Columbus Server Application

Columbus is a Journey Sharing App that allows users with similar interests to connect with other users who share a common destination. Users in a shared journey can collaborate with each other to set meeting points, mode of transport and book a taxi. All decisions in a group are made by majority voting that allows distribution of control. Users may set preferences for a journey such as M/F, user rating, etc.


Columbus allows two types of trips:

* Scheduled Trips
* Immediate Trips
 
## Running Development Server
i
mvn spring-boot:run

## Maven build, and test
mvn clean package
i
## Postman Request collection:
https://www.getpostman.com/collections/61cc634a6b549795cf42
i
## Test Properties
spring.data.mongodb.port=0


## Set up LB and Auto-scaling in GCP

1. Create Instance template which will be used for auto-scaling in instance group
   Define the instance image
   Define the cpu, disk size

```
gcloud compute instance-templates create columbus-template \
    --machine-type n1-standard-1 \
    --image columbus-image \
    --boot-disk-size 10GB \
    --zone [ZONE]

```

2. Create Instance group based on instance templates
```
gcloud compute instance-groups managed create columbus-group \
    --size 2  \
    --template columbus-template \
    --zone [ZONE]
```
3. Set up Auto-scaling for instance group. Max replica is 10, and CPU utilization higher 75%, auto-scaling happens.
```
gcloud compute instance-groups managed set-autoscaling  columbus-group \
      --max-num-replicas 10 \
      --target-cpu-utilization 0.75 \
      --cool-down-period 90
```

4. Create Target pool for redirecting requests from load balancer
```
gcloud compute target-pools create columbus-pool
```

5. Create Firewall rules to allow tcp connection on port 8080
```
gcloud compute firewall-rules create columbus-firewall --allow tcp:8080
```

6. Set up the load balancer
```
gcloud compute forwarding-rules create columbus-rule \
    --ports 8080 \
    --address columbus-lb \
    --target-pool columbus-pool
    --zone [ZONE]
```

7. Set up MongoDB cluster
