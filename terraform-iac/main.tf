data "azurerm_subnet" "subnet_shr" {
    name = "operator-net-shr-linux-subnet"
    virtual_network_name = "operator-net-shr-vnet"
    resource_group_name = "operator-lab-rg"
}

data "azurerm_subnet" "subnet_container_apps" {
    name = "operator-net-dev-container-apps-subnet"
    virtual_network_name = "operator-net-dev-vnet"
    resource_group_name = "operator-lab-rg"
}
data "azurerm_subnet" "subnet_container_apps_prd" {
    name = "operator-net-prd-container-apps-subnet"
    virtual_network_name = "operator-net-prd-vnet"
    resource_group_name = "operator-lab-rg"
}

/*
module "vm" {
    depends_on = [
        data.azurerm_subnet.subnet_shr
    ]
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
}*/

resource "azurerm_mssql_server" "sqlserver" {
    name                        = "serverless-instance-jasimoes"
    resource_group_name         = "operator-lab-rg"
    location                    = "West Europe"
    version                     = "12.0"

    administrator_login         = "admin_db"
    administrator_login_password = "P@ssw0rd"
}
resource "azurerm_mssql_database" "serverless_db" {
    depends_on = [
        azurerm_mssql_server.sqlserver
    ]
    name                        = "account_db"
    server_id                   = azurerm_mssql_server.sqlserver.id
    collation                   = "SQL_Latin1_General_CP1_CI_AS"

    auto_pause_delay_in_minutes = 60
    max_size_gb                 = 2
    min_capacity                = 0.5
    read_replica_count          = 0
    read_scale                  = false
    sku_name                    = "GP_S_Gen5_2"
    zone_redundant              = false

}
resource "azurerm_mssql_virtual_network_rule" "network_rule" {
    depends_on = [
        azurerm_mssql_server.sqlserver
    ]
    name      = "sql-vnet-rule"
    server_id = azurerm_mssql_server.sqlserver.id
    subnet_id = data.azurerm_subnet.subnet_container_apps.id
    ignore_missing_vnet_service_endpoint = true
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
        azurerm_log_analytics_workspace.log_workspace,
        data.azurerm_subnet.subnet_container_apps
    ]
    name                       = "container-environment-dev"
    location                   = "West Europe"
    resource_group_name        = "operator-lab-rg"
    log_analytics_workspace_id = azurerm_log_analytics_workspace.log_workspace.id

    infrastructure_subnet_id = data.azurerm_subnet.subnet_container_apps.id

}
resource "azurerm_container_app_environment" "app_env_prd" {
    depends_on = [
        azurerm_log_analytics_workspace.log_workspace,
        data.azurerm_subnet.subnet_container_apps_prd
    ]
    name                       = "container-environment-prd"
    location                   = "West Europe"
    resource_group_name        = "operator-lab-rg"
    log_analytics_workspace_id = azurerm_log_analytics_workspace.log_workspace.id

    infrastructure_subnet_id = data.azurerm_subnet.subnet_container_apps_prd.id

}