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
| Channel Pool mix idle       |  10             |
| Channel Pool max total      |  50             |
| DB Consumer thread count    |  25             |
| Store Consumer tread count  |  25             |
| RabbitMQ CPU memory use     |  70%            |


-----------------------------------------------------

## Results

| Thread Count | Wall Time (s)| Median Latency (ms) |  Mean Latency (ms) | 
|:-------------|:--------------:|:-----------------:|:------------------:|
|  256         |  179.92        |  28.0             |  50.75             |
|  512         |  413.22        |  37.0             |  124.29            |