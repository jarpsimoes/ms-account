data "azurerm_subnet" "subnet" {
  name = "operator-net-shr-linux-subnet"
  virtual_network_name = "operator-net-shr-vnet"
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
  ip_address_type     = "Public"
  dns_name_label      = "ms-account-dev"


  container {
    cpu    = "0.2"
    image  = "ghcr.io/jarpsimoes/ms-account:${var.tag}"
    memory = "1.5"
    name   = "ms-account"

    ports {
      port     = 8080
      protocol = "TCP"
    }
  }
}