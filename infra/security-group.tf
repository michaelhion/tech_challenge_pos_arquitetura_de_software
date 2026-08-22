resource "aws_security_group" "k3s" {
  name_prefix = "${lower(var.project_name)}-${var.environment}-k3s-"
  description = "Security Group do no K3s da aplicacao Oficina360"
  vpc_id      = var.vpc_id

  tags = {
    Name = "${var.project_name}-${var.environment}-k3s-sg"
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_vpc_security_group_ingress_rule" "ssh" {
  security_group_id = aws_security_group.k3s.id

  description = "Acesso administrativo por SSH"
  ip_protocol = "tcp"
  from_port   = 22
  to_port     = 22
  cidr_ipv4   = var.ssh_allowed_cidr

  tags = {
    Name = "ssh-administrativo"
  }
}

resource "aws_vpc_security_group_ingress_rule" "api" {
  security_group_id = aws_security_group.k3s.id

  description = "Acesso publico a API Oficina360 via NodePort"
  ip_protocol = "tcp"
  from_port   = var.api_node_port
  to_port     = var.api_node_port
  cidr_ipv4   = var.api_allowed_cidr

  tags = {
    Name = "api-nodeport"
  }
}

resource "aws_vpc_security_group_egress_rule" "internet" {
  security_group_id = aws_security_group.k3s.id

  description = "Saida para atualizacoes, imagens e dependencias"
  ip_protocol = "-1"
  cidr_ipv4   = "0.0.0.0/0"

  tags = {
    Name = "saida-internet"
  }
}