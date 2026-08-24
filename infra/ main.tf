resource "aws_instance" "k3s" {
  ami           = var.ami_id
  instance_type = var.instance_type
  key_name      = var.key_name

  subnet_id                   = var.subnet_id
  associate_public_ip_address = true

  vpc_security_group_ids = [
    aws_security_group.k3s.id
  ]

  user_data                   = file("${path.module}/user-data.sh")
  user_data_replace_on_change = true

  root_block_device {
    volume_size           = 20
    volume_type           = "gp3"
    delete_on_termination = true
  }

  tags = {
    Name        = "${var.project_name}-k3s"
    Environment = var.environment
    ManagedBy   = "Terraform"
  }

  depends_on = [
    aws_route.internet,
    aws_route_table_association.public_subnet
  ]
}