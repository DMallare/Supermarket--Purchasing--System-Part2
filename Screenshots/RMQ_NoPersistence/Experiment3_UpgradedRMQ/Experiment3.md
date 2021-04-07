# Experiment 3 (Upgraded RMQ Instance)

## Instance Details

| Service           | Instance Type   | AMI / Engine   |
|:------------------|:----------------|:---------------|
| Server            |  t2.micro       | Amazon Linux 2 |
| RabbitMQ          |  t2.large       | Ubuntu 20.04   |
| Database Consumer |  t2.micro       | Amazon Linux 2 |
| Store             |  t2.medium      | Amazon Linux 2 |
| RDS               |  db.t2.micro    | MySQL Community|

------------------------------------------------------

## Settings Details

| Setting Type                | Setting Value   |
|:----------------------------|:---------------:|
| Channel Pool mix idle       |  10             |
| Channel Pool max total      |  50             |
| DB Consumer thread count    |  75             |
| Store Consumer thread count |  75             |
| RabbitMQ CPU memory use     |  70%            |
| RabbitMQ Durable Queues     | False           |


-----------------------------------------------------

## Results

| Thread Count | Wall Time (s)  | Median Latency (ms)| Mean Latency (ms) | 
|:-------------|:--------------:|:-----------------:|:------------------:|
|  256         |  197.41        |  33.0             |  55.20             |
|  512         |  434.63        |  43.0             |  123.78            |