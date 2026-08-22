output "instance_id" {
  description = "ID da EC2"
  value       = aws_instance.k3s.id
}

output "public_ip" {
  description = "IP publico da EC2"
  value       = aws_instance.k3s.public_ip
}

output "public_dns" {
  description = "DNS publico da EC2"
  value       = aws_instance.k3s.public_dns
}

output "security_group_id" {
  description = "ID do Security Group do no K3s"
  value       = aws_security_group.k3s.id
}

output "spring_boot_url" {
  description = "URL externa da aplicacao Spring Boot"
  value       = "http://${aws_instance.k3s.public_ip}:${var.api_node_port}"
}

output "swagger_url" {
  description = "URL externa do Swagger UI"
  value       = "http://${aws_instance.k3s.public_ip}:${var.api_node_port}/swagger-ui/index.html"
}

output "ssh_command" {
  description = "Comando-base para acesso SSH"
  value       = "ssh ubuntu@${aws_instance.k3s.public_ip}"
}