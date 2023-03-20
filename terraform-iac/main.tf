data "azurerm_subnet" "subnet_shr" {
    name = "operator-net-shr-linux-subnet"
    virtual_network_name = "operator-net-shr-vnet"
    resource_group_name = "operator-lab-rg"
}

data "azurerm_subnet" "subnet_dev" {
    name = "operator-net-shr-linux-subnet"
    virtual_network_name = "operator-net-shr-vnet"
    resource_group_name = "operator-lab-rg"
}

module "vm" {
    source = "github.com/jarpsimoes/tf-modules/virtual-machine-linux"

    name = "vm-db-dev"
    resource_group_name = "operator-lab-rg"
    location = "West Europe"

    environment = "shr"

    vm_size = "Standard_b2s"

    network = {
        internal_address_allocation = "Dynamic"
        private_ip_address = "10.4.0.100"
        public_ip = false
        subnet_id = data.azurerm_subnet.subnet_shr.id
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


resource "azurerm_log_analytics_workspace" "log_workspace" {
    name                = "container-apps-log-workspace"
    resource_group_name = "operator-lab-rg"
    location            = "West Europe"
    sku                 = "PerGB2018"
    retention_in_days   = 30
}

resource "azurerm_container_app_environment" "app_env" {
    depends_on = [
        azurerm_log_analytics_workspace.log_workspace
    ]
    name                       = "container-enviroment"
    location                   = "West Europe"
    resource_group_name        = "operator-lab-rg"
    log_analytics_workspace_id = azurerm_log_analytics_workspace.log_workspace.id

    infrastructure_subnet_id = data.azurerm_subnet.subnet_dev.id

    internal_load_balancer = true
}