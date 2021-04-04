# Experiment 1

## Instance Details

| Service           | Instance Type   | AMI / Engine   |
|:------------------|:----------------|:---------------|
| Server            |  t2.micro       | Amazon Linux 2 |
| RabbitMQ          |  t2.micro       | Ubuntu 20.04   |
| Database Consumer |  t2.micro       | Amazon Linux 2 |
| Store             |  t2.micro       | Amazon Linux 2 |
| RDS               |  db.t2.micro    | MySQL Community|

------------------------------------------------------

## Settings Details

| Setting Type                | Setting Value   |
|:----------------------------|:---------------:|
| Channel Pool mix idle       |  100            |
| Channel Pool max total      |  200            |
| DB Consumer thread count    |  25             |
| Store Consumer tread count  |  25             |
| RabbitMQ CPU memory use     |  70%            |


-----------------------------------------------------

## Results

| Thread Count | Wall Time (s)| Median Latency (ms) |  Mean Latency (ms) | 
|:-------------|:--------------:|:-----------------:|:------------------:|
|  256         |  187.72        | 29.0              | 52.19              |  
|  512         |  404.63        |    37.0           | 118.82             |