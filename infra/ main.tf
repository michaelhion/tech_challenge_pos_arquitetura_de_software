resource "aws_instance" "k3s" {
  ami           = var.ami_id
  instance_type = var.instance_type
  key_name      = var.key_name

  iam_instance_profile = var.iam_instance_profile_name

  subnet_id                   = var.subnet_id
  associate_public_ip_address = true

  vpc_security_group_ids = [
    aws_security_group.k3s.id
  ]

  user_data                   = file("${path.module}/user-data.sh")
  user_data_replace_on_change = true

  depends_on = [
    aws_route.internet,
    aws_route_table_association.public_subnet
  ]

  tags = {
    Name        = "${var.project_name}-k3s"
    Environment = var.environment
    ManagedBy   = "Terraform"
  }
}