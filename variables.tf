variable "project_id" {
  description = "Project identifier used for tagging and naming."
  type        = string
  default     = "113702ee-935c-4559-be29-3089dc710724"
}

variable "environment" {
  description = "Deployment environment (dev/staging/prod)."
  type        = string
  default     = "dev"
}

variable "region" {
  description = "AWS region to deploy EKS into."
  type        = string
  default     = "eu-west-1"
}

variable "cluster_name" {
  description = "EKS cluster name."
  type        = string
  default     = "ngmb-eks-dev"
}

variable "vpc_cidr" {
  description = "CIDR for the EKS VPC."
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnet_cidrs" {
  description = "Public subnet CIDRs."
  type        = list(string)
  default = [
    "10.0.1.0/24",
    "10.0.2.0/24",
    "10.0.3.0/24"
  ]
}

variable "private_subnet_cidrs" {
  description = "Private subnet CIDRs."
  type        = list(string)
  default = [
    "10.0.101.0/24",
    "10.0.102.0/24",
    "10.0.103.0/24"
  ]
}

variable "kubernetes_version" {
  description = "Kubernetes version for the EKS cluster."
  type        = string
  default     = "1.31"
}

variable "node_instance_types" {
  description = "Instance types for the managed node group."
  type        = list(string)
  default     = ["m6i.large"]
}

variable "node_desired_capacity" {
  description = "Desired node count in the managed node group."
  type        = number
  default     = 3
}

variable "node_min_capacity" {
  description = "Minimum node count."
  type        = number
  default     = 1
}

variable "node_max_capacity" {
  description = "Maximum node count."
  type        = number
  default     = 5
}

variable "allowed_api_cidrs" {
  description = "CIDR blocks allowed to access the EKS public API endpoint."
  type        = list(string)
  default     = ["0.0.0.0/0"]
}

variable "cluster_log_retention_days" {
  description = "Retention in days for CloudWatch control plane logs."
  type        = number
  default     = 90
}