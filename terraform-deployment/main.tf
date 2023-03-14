data "azurerm_subnet" "subnet" {
  name = "operator-net-dev-k8s-subnet"
  virtual_network_name = "operator-net-dev-vnet"
  resource_group_name = "operator-lab-rg"
}
variable "tag" {
  type = string
}
resource "azurerm_container_group" "ms_account" {
  location            = "West Europe"
  name                = "ms-account-dev"
  os_type             = "Linux"
  resource_group_name = "operator-lab-rg"
  restart_policy      = "Always"
  ip_address_type     = "Private"
  subnet_ids          = [data.azurerm_subnet.subnet.id]

  container {
    cpu    = "0.5"
    #image  = "ghcr.io/jarpsimoes/ms-account:${var.tag}"
    image = "ghcr.io/jarpsimoes/ms-account:dev_05fd3dd"
    memory = "1"
    name   = "ms-account"

    ports {
      port     = 8080
      protocol = "TCP"
    }

    environment_variables = {
      "QUARKUS_DATASOURCE_URL" = "jdbc:mysql://10.4.0.100:3306/ms_account_dev?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC"
      "QUARKUS_DATASOURCE_USERNAME" = "ms_account"
      "QUARKUS_DATASOURCE_PASSWORD" = "ms_account"
      "QUARKUS_HIBERNATE_ORM_DATABASE_GENERATION" = "update"
    }
  }
}