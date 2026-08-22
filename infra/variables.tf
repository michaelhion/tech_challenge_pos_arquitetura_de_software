variable "aws_region" {
  description = "Região AWS onde os recursos serão criados"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Nome do projeto"
  type        = string
  default     = "Oficina360"
}

variable "environment" {
  description = "Ambiente"
  type        = string
  default     = "lab"
}

variable "instance_type" {
  description = "Tipo da instância EC2"
  type        = string
  default     = "t3.micro"
}

variable "ami_id" {
  description = "AMI Amazon Linux 2023 fornecida pelo laboratório"
  type        = string
  default     = "ami-0332d564d76dbd8d6"
}

variable "vpc_id" {
  description = "VPC existente fornecida pelo laboratório"
  type        = string
}

variable "subnet_id" {
  description = "Subnet existente fornecida pelo laboratório"
  type        = string
}

variable "ssh_allowed_cidr" {
  description = "CIDR autorizado a acessar a instancia por SSH"
  type        = string

  validation {
    condition     = can(cidrnetmask(var.ssh_allowed_cidr))
    error_message = "ssh_allowed_cidr deve ser um CIDR IPv4 valido, por exemplo 200.100.50.25/32."
  }
}

variable "api_node_port" {
  description = "NodePort utilizado para expor a API Oficina360"
  type        = number
  default     = 30080

  validation {
    condition = (
      var.api_node_port >= 30000 &&
      var.api_node_port <= 32767
    )
    error_message = "api_node_port deve estar entre 30000 e 32767."
  }
}

variable "api_allowed_cidr" {
  description = "CIDR autorizado a acessar publicamente a API"
  type        = string
  default     = "0.0.0.0/0"

  validation {
    condition     = can(cidrnetmask(var.api_allowed_cidr))
    error_message = "api_allowed_cidr deve ser um CIDR IPv4 valido."
  }
}