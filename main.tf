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
    region  = "us-east-1"
}

resource "aws_default_vpc" "default" {
#     cidr_block       = "10.0.0.0/24"
#     instance_tenancy = "default"
}

resource "aws_internet_gateway" "main" {
    vpc_id = aws_default_vpc.default.id
    
    depends_on = [aws_default_vpc.default]
}

resource "aws_egress_only_internet_gateway" "main" {
    vpc_id = aws_default_vpc.default.id
    
    depends_on = [aws_default_vpc.default]
}

resource "aws_subnet" "main" {
    vpc_id     = aws_default_vpc.default.id
    cidr_block = "10.0.0.0/24"
    
    depends_on = [aws_default_vpc.default]
}

resource "aws_default_route_table" "main" {
    default_route_table_id = aws_default_vpc.default.default_route_table_id

    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_internet_gateway.main.id
    }

    route {
        ipv6_cidr_block        = "::/0"
        egress_only_gateway_id = aws_egress_only_internet_gateway.main.id
    }
    
    depends_on = [aws_default_vpc.default]
}

resource "aws_instance" "app_server" {
    ami           = "ami-0440d3b780d96b29d"
    instance_type = "t2.micro"
    
    depends_on = [aws_default_vpc.default]
}
