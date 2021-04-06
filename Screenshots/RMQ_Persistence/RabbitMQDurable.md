# Experiment 1

## Instance Details

| Service           | Instance Type   | AMI / Engine   |
|:------------------|:----------------|:---------------|
| Server            |  t2.micro       | Amazon Linux 2 |
| RabbitMQ          |  t2.micro       | Ubuntu 20.04   |
| Database Consumer |  t2.micro       | Amazon Linux 2 |
| Store             |  t2.medium      | Amazon Linux 2 |
| RDS               |  db.t2.micro    | MySQL Community|

------------------------------------------------------

## Settings Details

| Setting Type                | Setting Value   |
|:----------------------------|:---------------:|
| Channel Pool mix idle       |  10             |
| Channel Pool max total      |  50             |
| DB Consumer thread count    |  25             |
| Store Consumer thread count |  25             |
| RabbitMQ CPU memory use     |  70%            |
| RabbitMQ Durable Queues     | True            |


-----------------------------------------------------

## Results

| Thread Count | Wall Time (s)| Median Latency (ms) |  Mean Latency (ms) | 
|:-------------|:--------------:|:-----------------:|:------------------:|
|  256         |  255.90        | 35.0              | 61.06              |  
|  512         |  421.37        | 43.0              | 120.66             |