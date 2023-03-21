data "azurerm_container_app_environment" "app_env" {
  name                = "container-enviroment"
  resource_group_name = "operator-lab-rg"
}
variable "tag" {
  type = string
}
resource "azurerm_container_app" "container_app" {
  name                          = "container-app"
  container_app_environment_id  = data.azurerm_container_app_environment.app_env.id
  resource_group_name           = "operator-lab-rg"
  revision_mode                 = "Single"

  ingress {
    external_enabled = true
    target_port      = 8080
    traffic_weight {
      percentage = 100
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
        value = "jdbc:mysql://10.4.0.100:3306/ms_account_dev"
      }

      env {
          name  = "QUARKUS_DATASOURCE_USERNAME"
          value = "ms_account"
      }

      env {
          name  = "QUARKUS_DATASOURCE_PASSWORD"
          value = "ms_account"
      }

      env {
          name  = "QUARKUS_DATASOURCE_DB_KIND"
          value = "mysql"
      }

      env {
          name  = "QUARKUS_HIBERNATE_ORM_DATABASE_GENERATION"
          value = "update"
      }
    }

  }
}