terraform {
    required_providers {
        aws = {
            source  = "hashicorp/aws"
            version = "~> 4.16"
        }
    }

    required_version = ">= 1.2.0"
}

provider "aws" {
    region  = var.aws_region
}

data "aws_availability_zones" "available" {
  state = "available"
}

resource "aws_vpc" "default_vpc" {
    cidr_block           = var.vpc_cidr_block
    enable_dns_hostnames = true
}

resource "aws_internet_gateway" "default_gateway" {
    vpc_id = aws_vpc.default_vpc.id
}

resource "aws_subnet" "default_public_subnet" {
  count             = var.subnet_count.public
  vpc_id            = aws_vpc.default_vpc.id
  cidr_block        = var.public_subnet_cidr_blocks[count.index]
  availability_zone = data.aws_availability_zones.available.names[count.index]
}

resource "aws_subnet" "default_private_subnet" {
  count             = var.subnet_count.private
  vpc_id            = aws_vpc.default_vpc.id
  cidr_block        = var.private_subnet_cidr_blocks[count.index]
  availability_zone = data.aws_availability_zones.available.names[count.index]
}

resource "aws_route_table" "default_public_rt" {
  vpc_id = aws_vpc.default_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.default_gateway.id
  }
}

resource "aws_route_table_association" "public" {
  count          = var.subnet_count.public
  route_table_id = aws_route_table.default_public_rt.id
  subnet_id      = aws_subnet.default_public_subnet[count.index].id
}

resource "aws_route_table" "default_private_rt" {
  vpc_id = aws_vpc.default_vpc.id
}

resource "aws_route_table_association" "private" {
  count          = var.subnet_count.private
  route_table_id = aws_route_table.default_private_rt.id
  subnet_id      = aws_subnet.default_private_subnet[count.index].id
}

resource "aws_security_group" "default_ec2" {
  name        = "aws_security_group_ec2"
  description = "Security group for tutorial web servers"
  vpc_id      = aws_vpc.default_vpc.id

  ingress {
    description = "Allow all traffic through HTTP"
    from_port   = "80"
    to_port     = "80"
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow SSH from my computer"
    from_port   = "22"
    to_port     = "22"
    protocol    = "tcp"
    cidr_blocks = ["185.128.9.198/32"]
#     cidr_blocks = ["${var.my_ip}/32"]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "default_rds" {
  name        = "aws_security_group_rds"
  description = "Security group for tutorial databases"
  vpc_id      = aws_vpc.default_vpc.id

  ingress {
    description     = "Allow PostgreSQL traffic from only the ec2 sg"
    from_port       = "5432"
    to_port         = "5432"
    protocol        = "tcp"
    security_groups = [aws_security_group.default_ec2.id]
  }
}

resource "aws_db_subnet_group" "default_rds_subnet_group" {
  name        = "tutorial_db_subnet_group"
  description = "DB subnet group for tutorial"
  subnet_ids  = [for subnet in aws_subnet.default_private_subnet : subnet.id]
}

resource "aws_db_instance" "application_database" {
  instance_class         = "db.t3.micro"
  allocated_storage      = 5
  engine                 = "postgres"
  engine_version         = "16"
  username               = "postgres"
  password               = "postgres"
  db_subnet_group_name   = aws_db_subnet_group.default_rds_subnet_group.id
  vpc_security_group_ids = [aws_security_group.default_rds.id]
  skip_final_snapshot    = true
}

resource "aws_instance" "application_server" {
  count                  = 1
  ami                    = "ami-0f403e3180720dd7e"
  instance_type          = "t2.micro"
  subnet_id              = aws_subnet.default_public_subnet[count.index].id
  vpc_security_group_ids = [aws_security_group.default_ec2.id]
}

resource "aws_eip" "tutorial_web_eip" {
  count    = 1
  instance = aws_instance.application_server[count.index].id
  vpc      = true
}