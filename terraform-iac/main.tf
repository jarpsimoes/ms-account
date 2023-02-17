data "azurerm_subnet" "subnet" {
    name = "operator-net-shr-linux-subnet"
    virtual_network_name = "operator-net-shr-vnet"
    resource_group_name = "operator-lab-rg"
}

module "vm" {
    source = "github.com/jarpsimoes/tf-modules/virtual-machine-linux"

    name = "database-dev"
    resource_group_name = "operator-lab-rg"
    location = "West Europe"

    environment = "shr"

    vm_size = "Standard_b2s"

    network = {
        internal_address_allocation = "Dynamic"
        private_ip_address = "10.4.0.100"
        public_ip = false
        subnet_id = data.azurerm_subnet.subnet.id
    }

    image = {
        publisher = "Canonical"
        offer     = "0001-com-ubuntu-server-focal"
        sku       = "20_04-lts-gen2"
        version   = "20.04.202301130"
    }

    authentication = {
        admin_username = "admin_db"
        pub_key_path = "./key.pub"
    }
}
resource "azurerm_container_group" "container_app" {

    name = "account-ms"
    location = module.vm.location
    resource_group_name = module.vm.resource_group_name
    ip_address_type = "Public"
    os_type = "Linux"
    dns_name_label = "account-ms"

    container {
      name = "account-ms"
      image = "mcr.microsoft.com/azure-sql-edge:latest"
      cpu = "0.5"
      memory = "1"

      ports {
        port = 443
        protocol = "TCP"
      }
    }

    tags = {
        environment = "test"
    }
}