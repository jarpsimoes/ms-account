data "azurerm_container_app_environment" "app_env_dev" {
  name                = "container-environment-dev"
  resource_group_name = "operator-lab-rg"
}
data "azurerm_container_app_environment" "app_env_prd" {
  name                = "container-environment-prd"
  resource_group_name = "operator-lab-rg"
}
variable "tag" {
  type = string
}
variable "sql_user" {
  type = string
}
variable "sql_password" {
  type = string
}
variable "sql_url" {
  type = string
}
resource "azurerm_container_app" "container_app_dev" {
  depends_on = [
    data.azurerm_container_app_environment.app_env_dev
  ]
  name                          = "account-ms-dev"
  container_app_environment_id  = data.azurerm_container_app_environment.app_env_dev.id
  resource_group_name           = "operator-lab-rg"
  revision_mode                 = "Single"

  ingress {
    external_enabled = true
    target_port      = 8080
    traffic_weight   {
      latest_revision = true
      percentage      = 100
    }
  }

  template {
    min_replicas = 1

    container {
      name = "ms-account"
      image = "ghcr.io/jarpsimoes/ms-account:${var.tag}"
      cpu    = "0.5"
      memory = "1Gi"

      liveness_probe {
        port = 8080
        path = "/health/live"
        initial_delay = 10
        transport = "HTTP"
      }
      startup_probe {
        port = 8080
        path = "/health/ready"
        transport = "HTTP"
      }
      readiness_probe {
        port = 8080
        path = "/health/ready"
        transport = "HTTP"
      }

      env {
        name  = "QUARKUS_DATASOURCE_JDBC_URL"
        value = var.sql_url
      }

      env {
          name  = "QUARKUS_DATASOURCE_USERNAME"
          value = var.sql_user
      }

      env {
          name  = "QUARKUS_DATASOURCE_PASSWORD"
          value = var.sql_password
      }

      env {
          name  = "QUARKUS_DATASOURCE_DB_KIND"
          value = "mssql"
      }

      env {
          name  = "QUARKUS_HIBERNATE_ORM_DATABASE_GENERATION"
          value = "update"
      }
    }

  }
}