terraform {
  required_providers {
    azurerm = {
      source = "hashicorp/azurerm"
      version = "3.48.0"
    }
  }
  backend "azurerm" {
    resource_group_name = "operator-lab-rg"
    storage_account_name = "operatorlabsa"
    container_name = "operator-lab-state-container"
    key = "devdatabase.tfsate"
  }
}

provider "azurerm" {
  features {
    
  }
}